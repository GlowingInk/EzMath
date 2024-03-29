package ink.glowing.math;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import static ink.glowing.math.MathDictionary.*;

/**
 * Better performance for repeating calculations over {@link ExpressionEvaluator}.
 * Unlike {@link ExpressionEvaluator}, accepts custom variables after expression compiling.
 */
public class FormulaEvaluator {
    private static final Term ZERO = (vars) -> 0;

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
        return eval(variables::get);
    }

    public double eval(Function<String, Double> variables) {
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
        } else if (isDigit(holder.current())) {
            holder.pointer++;
            while (isDigit(holder.current())) holder.pointer++;
            if (holder.progress('.')) {
                while (isDigit(holder.current())) holder.pointer++;
                if (holder.progress('e')) {
                    if (!holder.progress('-')) holder.progress('+');
                    while (isDigit(holder.current())) holder.pointer++;
                }
            }
            double a = asDouble(holder.substring(start, holder.pointer), 0);
            x = (vars) -> a;
        } else if (isLetter(holder.current())) {
            holder.pointer++;
            while (isLetter(holder.current()) || isDigit(holder.current())) holder.pointer++;
            String str = holder.substring(start, holder.pointer);
            if (holder.progress('(')) {
                MathFunction function = math.getFunction(str);
                Term a = thirdImportance(holder);
                if (!holder.progress(',')) {
                    if (function != null) {
                        x = (vars) -> function.accept(a.calc(vars));
                    }
                } else {
                    Term b = thirdImportance(holder);
                    if (!holder.progress(',')) {
                        if (function != null) {
                            x = (vars) -> function.accept(a.calc(vars), b.calc(vars));
                        }
                    } else {
                        Term[] args = {b, thirdImportance(holder)};
                        while (holder.progress(',')) {
                            args = Arrays.copyOfRange(args, 0, args.length + 1);
                            args[args.length - 1] = thirdImportance(holder);
                        }
                        if (function != null) {
                            Term[] finArgs = args;
                            x = (vars) -> {
                                double[] numArgs = new double[finArgs.length];
                                for (int i = 0; i < finArgs.length; i++)
                                    numArgs[i] = finArgs[i].calc(vars);
                                return function.accept(a.calc(vars), numArgs);
                            };
                        }
                    }
                }
                holder.progress(')');
            } else {
                Double cons = math.getConstant(str);
                if (cons == null) {
                    x = (vars) -> {
                        Double value = vars.apply(str);
                        return value == null ? 0 : value;
                    };
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
        double calc(Function<String, Double> vars);
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