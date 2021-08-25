package me.imdanix.math;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
   All the code is inspired by Boann's answer on https://stackoverflow.com/questions/3422673
   You can use this code however you want to
*/

/**
 * Pretty much the same as {@link MathEvaluator}, but better performance for expressions that used more than one time
 * The main difference is that custom variables can be used
 */
public class FormulasEvaluator {
    private static final Expression ZERO = () -> 0;

    private final Expression expression;
    private final Map<String, Double> variables;
    private final MathBase math;

    public FormulasEvaluator(String expression, MathBase math) {
        this.variables = new ConcurrentHashMap<>();
        this.expression = thirdImportance(new PointerHolder(expression.replace(" ", "").toLowerCase(Locale.ENGLISH)));
        this.math = math;
    }

    public void setVariable(String variable, Double value) {
        variables.put(variable, value);
    }

    public void setVariables(Map<String, Double> variables) {
        this.variables.putAll(variables);
    }

    public double eval() {
        return expression.eval();
    }

    private Expression thirdImportance(PointerHolder holder) {
        Expression x = secondImportance(holder);
        while (true) {
            if (holder.tryNext('+')) {
                Expression a = x;
                Expression b = secondImportance(holder);
                x = () -> a.eval() + b.eval();
            } else if (holder.tryNext('-')) {
                Expression a = x;
                Expression b = secondImportance(holder);
                x = () -> a.eval() - b.eval();
            } else {
                return x;
            }
        }
    }

    private Expression secondImportance(PointerHolder holder) {
        Expression x = firstImportance(holder);
        while (true) {
            if (holder.tryNext('*')) {
                Expression a = x;
                Expression b = firstImportance(holder);
                x = () -> a.eval() * b.eval();
            } else if (holder.tryNext('/')) {
                Expression a = x;
                Expression b = firstImportance(holder);
                x = () -> a.eval() / b.eval();
            } else if (holder.tryNext('%')) {
                Expression a = x;
                Expression b = firstImportance(holder);
                x = () -> a.eval() % b.eval();
            } else {
                return x;
            }
        }
    }

    private Expression firstImportance(PointerHolder holder) {
        if (holder.tryNext('-')) { // "-5", "--5"..
            Expression a = firstImportance(holder);
            return () -> -a.eval();
        }
        if (holder.tryNext('+')) // "+5", "++5"..
            return firstImportance(holder);
        Expression x = ZERO;
        int start = holder.pointer;
        if (holder.tryNext('(')) {
            x = thirdImportance(holder);
            holder.tryNext(')');
        } else if (MathBase.isNumberChar(holder.current())) {
            holder.pointer++;
            while (MathBase.isNumberChar(holder.current())) holder.pointer++;
            double a = MathBase.getDouble(holder.substring(start, holder.pointer), 0);
            x = () -> a;
        } else if (MathBase.isWordChar(holder.current())) {
            holder.pointer++;
            while (MathBase.isWordChar(holder.current()) || MathBase.isNumberChar(holder.current())) holder.pointer++;
            String str = holder.substring(start, holder.pointer);
            if (holder.tryNext('(')) {
                Expression a = thirdImportance(holder);
                Expression[] args = new Expression[0];
                while (holder.tryNext(',')) {
                    args = Arrays.copyOfRange(args, 0, args.length + 1);
                    args[args.length - 1] = thirdImportance(holder);
                }
                Expression[] finArgs = args;
                MathBase.Function function = math.getFunction(str);
                if (function == null) {
                    x = ZERO;
                } else switch (args.length) {
                    case 0: x = () -> function.eval(a.eval()); break;
                    case 1: x = () -> function.eval(a.eval(), finArgs[0].eval()); break;
                    default:
                        double[] argsD = new double[finArgs.length];
                        x = () -> {
                            for (int i = 0; i < finArgs.length; i++)
                                argsD[i] = finArgs[i].eval();
                            return function.eval(a.eval(), argsD);
                        };
                }
                holder.tryNext(')');
            } else x = () -> {
                Double var = variables.get(str);
                return var == null ? math.getConstant(str) : var;
            };
        }

        if (holder.tryNext('^')) {
            Expression a = x;
            Expression b = firstImportance(holder);
            x = () -> Math.pow(a.eval(), b.eval());
        }
        return x;
    }

    public MathBase getMath() {
        return math;
    }

    @FunctionalInterface
    private interface Expression {
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

        boolean tryNext(char c) {
            if (current() == c) {
                pointer++;
                return true;
            }
            return false;
        }
    }
}