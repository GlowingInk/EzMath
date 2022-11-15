package me.imdanix.math;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import static me.imdanix.math.MathDictionary.*;

/**
 * Better performance for repeating calculations over {@link ExpressionEvaluator}.
 * Unlike {@link ExpressionEvaluator}, accepts custom variables after expression compiling.
 */
public class FormulaEvaluator {
    private static final Term ZERO = (vars) -> 0;
    private static final Double ZERO_VALUE = 0d;

    private final MathDictionary math;
    private final Term term;

    public FormulaEvaluator(String expression) {
        this(expression, MathDictionary.INSTANCE);
    }

    public FormulaEvaluator(String expression, MathDictionary math) {
        this.math = math;
        this.term = thirdImportance(new PointerHolder(expression.replace(" ", "").toLowerCase(Locale.ROOT)));
    }

    public double eval(Map<String, Double> variables) {
        return term.calc(variables);
    }

    private Term thirdImportance(PointerHolder holder) {
        Term x = secondImportance(holder);
        while (true) {
            if (holder.progress('+')) {
                Term a = x;
                Term b = secondImportance(holder);
                x = (vars) -> a.calc(vars) + b.calc(vars);
            } else if (holder.progress('-')) {
                Term a = x;
                Term b = secondImportance(holder);
                x = (vars) -> a.calc(vars) - b.calc(vars);
            } else {
                return x;
            }
        }
    }

    private Term secondImportance(PointerHolder holder) {
        Term x = firstImportance(holder);
        while (true) {
            if (holder.progress('*')) {
                Term a = x;
                Term b = firstImportance(holder);
                x = (vars) -> a.calc(vars) * b.calc(vars);
            } else if (holder.progress('/')) {
                Term a = x;
                Term b = firstImportance(holder);
                x = (vars) -> a.calc(vars) / b.calc(vars);
            } else if (holder.progress('%')) {
                Term a = x;
                Term b = firstImportance(holder);
                x = (vars) -> a.calc(vars) % b.calc(vars);
            } else {
                return x;
            }
        }
    }

    private Term firstImportance(PointerHolder holder) {
        if (holder.progress('-')) { // "-5", "--5"..
            Term a = firstImportance(holder);
            return (vars) -> -a.calc(vars);
        }
        //noinspection StatementWithEmptyBody
        while (holder.progress('+')) /* just skip */; // "+5", "++5"..
        Term x = ZERO;
        int start = holder.pointer;
        if (holder.progress('(')) {
            x = thirdImportance(holder);
            holder.progress(')');
        } else if (isDigitChar(holder.current())) {
            holder.pointer++;
            while (isDigitChar(holder.current())) holder.pointer++;
            if (holder.progress('.')) {
                while (isDigitChar(holder.current())) holder.pointer++;
                if (holder.progress('e')) {
                    if (!holder.progress('-')) holder.progress('+');
                    while (isDigitChar(holder.current())) holder.pointer++;
                }
            }
            double a = asDouble(holder.substring(start, holder.pointer), 0);
            x = (vars) -> a;
        } else if (isWordChar(holder.current())) {
            holder.pointer++;
            while (isWordChar(holder.current()) || isDigitChar(holder.current())) holder.pointer++;
            String str = holder.substring(start, holder.pointer);
            if (holder.progress('(')) {
                Term a = thirdImportance(holder);
                Term[] args = new Term[0];
                while (holder.progress(',')) {
                    args = Arrays.copyOfRange(args, 0, args.length + 1);
                    args[args.length - 1] = thirdImportance(holder);
                }
                Term[] finArgs = args;
                MathDictionary.Function function = math.getFunction(str);
                if (function != null) x = switch (args.length) {
                    case 0 -> (vars) -> function.accept(a.calc(vars));
                    case 1 -> (vars) -> function.accept(a.calc(vars), finArgs[0].calc(vars));
                    default -> (vars) -> {
                        double[] numArgs = new double[finArgs.length];
                        for (int i = 0; i < finArgs.length; i++)
                            numArgs[i] = finArgs[i].calc(vars);
                        return function.accept(a.calc(vars), numArgs);
                    };
                };
                holder.progress(')');
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

        if (holder.progress('^')) {
            Term a = x;
            Term b = firstImportance(holder);
            x = (vars) -> Math.pow(a.calc(vars), b.calc(vars));
        }
        return x;
    }

    @FunctionalInterface
    private interface Term {
        double calc(Map<String, Double> vars);
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

        boolean progress(char c) {
            if (current() == c) {
                pointer++;
                return true;
            }
            return false;
        }
    }
}