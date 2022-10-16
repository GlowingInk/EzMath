package me.imdanix.math;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

/**
 * Best performance for repeating calculations over {@link ExpressionEvaluator}.
 * Unlike {@link ExpressionEvaluator}, accepts custom variables after expression compiling.
 */
public class FormulaEvaluator {
    private static final Term ZERO = (vars) -> 0;
    private static final Double ZERO_VALUE = 0d;

    private final MathDictionary math;
    private final Term term;

    public FormulaEvaluator(String expression, MathDictionary math) {
        this.math = math;
        this.term = thirdImportance(new PointerHolder(expression.replace(" ", "").toLowerCase(Locale.ROOT)));
    }

    public double eval(Map<String, Double> variables) {
        return term.eval(variables);
    }

    private Term thirdImportance(PointerHolder holder) {
        Term x = secondImportance(holder);
        while (true) {
            if (holder.check('+')) {
                Term a = x;
                Term b = secondImportance(holder);
                x = (vars) -> a.eval(vars) + b.eval(vars);
            } else if (holder.check('-')) {
                Term a = x;
                Term b = secondImportance(holder);
                x = (vars) -> a.eval(vars) - b.eval(vars);
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
                x = (vars) -> a.eval(vars) * b.eval(vars);
            } else if (holder.check('/')) {
                Term a = x;
                Term b = firstImportance(holder);
                x = (vars) -> a.eval(vars) / b.eval(vars);
            } else if (holder.check('%')) {
                Term a = x;
                Term b = firstImportance(holder);
                x = (vars) -> a.eval(vars) % b.eval(vars);
            } else {
                return x;
            }
        }
    }

    private Term firstImportance(PointerHolder holder) {
        if (holder.check('-')) { // "-5", "--5"..
            Term a = firstImportance(holder);
            return (vars) -> -a.eval(vars);
        }
        //noinspection StatementWithEmptyBody
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
            x = (vars) -> a;
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
                if (function != null) x = switch (args.length) {
                    case 0 -> (vars) -> function.eval(a.eval(vars));
                    case 1 -> (vars) -> function.eval(a.eval(vars), finArgs[0].eval(vars));
                    default -> (vars) -> {
                        double[] numArgs = new double[finArgs.length];
                        for (int i = 0; i < finArgs.length; i++)
                            numArgs[i] = finArgs[i].eval(vars);
                        return function.eval(a.eval(vars), numArgs);
                    };
                };
                holder.check(')');
            } else {
                Double cons = math.getConstant(str);
                if (cons == null) {
                    x = (vars) -> vars.getOrDefault(str, ZERO_VALUE);
                } else {
                    double consValue = cons;
                    x = (vars) -> consValue;
                }
            }
        }

        if (holder.check('^')) {
            Term a = x;
            Term b = firstImportance(holder);
            x = (vars) -> Math.pow(a.eval(vars), b.eval(vars));
        }
        return x;
    }

    @FunctionalInterface
    private interface Term {
        double eval(Map<String, Double> vars);
    }

    /**
     * Used in parse process to unload origin after ending of parse
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