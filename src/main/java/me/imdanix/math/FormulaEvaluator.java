package me.imdanix.math;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Pretty much the same as {@link ExpressionEvaluator}, but better performance for expressions that used more than one time
 * The main difference is that custom variables can be used
 */
public class FormulaEvaluator {
    private static final Term ZERO = () -> 0;

    private final MathDictionary math;
    private final Term term;

    private boolean initRequired;
    private Map<String, Double> variables;

    public FormulaEvaluator(String expression, MathDictionary math) {
        this.math = math;
        this.initRequired = true;
        this.variables = Collections.emptyMap();
        this.term = thirdImportance(new PointerHolder(expression.replace(" ", "").toLowerCase(Locale.ENGLISH)));
    }

    public void setVariable(String variable, Double value) {
        initVariables();
        this.variables.put(variable, value);
    }

    public void setVariables(Map<String, Double> variables) {
        initVariables();
        this.variables.putAll(variables);
    }

    private void initVariables() {
        if (initRequired) {
            initRequired = false;
            variables = new ConcurrentHashMap<>();
        }
    }

    public double eval() {
        return term.eval();
    }

    private Term thirdImportance(PointerHolder holder) {
        Term x = secondImportance(holder);
        while (true) {
            if (holder.check('+')) {
                Term a = x;
                Term b = secondImportance(holder);
                x = () -> a.eval() + b.eval();
            } else if (holder.check('-')) {
                Term a = x;
                Term b = secondImportance(holder);
                x = () -> a.eval() - b.eval();
            } else {
                return x;
            }
        }
    }

    private Term secondImportance(PointerHolder holder) {
        Term x = firstImportance(holder);
        while (true) {
            if (holder.check('*')) {
                Term a = x;
                Term b = firstImportance(holder);
                x = () -> a.eval() * b.eval();
            } else if (holder.check('/')) {
                Term a = x;
                Term b = firstImportance(holder);
                x = () -> a.eval() / b.eval();
            } else if (holder.check('%')) {
                Term a = x;
                Term b = firstImportance(holder);
                x = () -> a.eval() % b.eval();
            } else {
                return x;
            }
        }
    }

    private Term firstImportance(PointerHolder holder) {
        if (holder.check('-')) { // "-5", "--5"..
            Term a = firstImportance(holder);
            return () -> -a.eval();
        }
        while (holder.check('+')) /* just skip */; // "+5", "++5"..
        Term x = ZERO;
        int start = holder.pointer;
        if (holder.check('(')) {
            x = thirdImportance(holder);
            holder.check(')');
        } else if (MathDictionary.isNumberChar(holder.current())) {
            holder.pointer++;
            while (MathDictionary.isNumberChar(holder.current())) holder.pointer++;
            double a = MathDictionary.getDouble(holder.substring(start, holder.pointer), 0);
            x = () -> a;
        } else if (MathDictionary.isWordChar(holder.current())) {
            holder.pointer++;
            while (MathDictionary.isWordChar(holder.current()) || MathDictionary.isNumberChar(holder.current())) holder.pointer++;
            String str = holder.substring(start, holder.pointer);
            if (holder.check('(')) {
                Term a = thirdImportance(holder);
                Term[] args = new Term[0];
                while (holder.check(',')) {
                    args = Arrays.copyOfRange(args, 0, args.length + 1);
                    args[args.length - 1] = thirdImportance(holder);
                }
                Term[] finArgs = args;
                MathDictionary.Function function = math.getFunction(str);
                if (function != null) switch (args.length) {
                    case 0 -> x = () -> function.eval(a.eval());
                    case 1 -> x = () -> function.eval(a.eval(), finArgs[0].eval());
                    default -> {
                        double[] numArgs = new double[finArgs.length];
                        x = () -> {
                            for (int i = 0; i < finArgs.length; i++)
                                numArgs[i] = finArgs[i].eval();
                            return function.eval(a.eval(), numArgs);
                        };
                    }
                }
                holder.check(')');
            } else x = () -> {
                Double var = variables.get(str);
                return var == null ? math.getConstant(str) : var;
            };
        }

        if (holder.check('^')) {
            Term a = x;
            Term b = firstImportance(holder);
            x = () -> Math.pow(a.eval(), b.eval());
        }
        return x;
    }

    @FunctionalInterface
    private interface Term {
        double eval();
    }

    /**
     * Used in parse process to unload origin and pointer itself after ending of parse
     * Because of this class everything looks a bit more sh!tty... but still readable
     */
    private static final class PointerHolder {
        final String origin;
        int pointer;

        PointerHolder(String origin) {
            this.origin = origin;
            this.pointer = 0;
        }

        String substring(int start, int end) {
            return origin.substring(start, end);
        }

        char current() {
            return origin.length() > pointer ? origin.charAt(pointer) : ' ';
        }

        boolean check(char c) {
            if (current() == c) {
                pointer++;
                return true;
            }
            return false;
        }
    }
}