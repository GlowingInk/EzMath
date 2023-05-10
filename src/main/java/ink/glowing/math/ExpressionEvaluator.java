package ink.glowing.math;

import java.util.Arrays;
import java.util.Locale;

import static ink.glowing.math.MathDictionary.*;

/**
 * Better performance for one-time calculations over {@link FormulaEvaluator}.
 * Also generates much less memory garbage than {@link FormulaEvaluator}.
 */
public class ExpressionEvaluator {
    private final String expression;
    private final MathDictionary math;
    private int pointer;

    private ExpressionEvaluator(String expression, MathDictionary math) {
        this.expression = expression.replace(" ", "").toLowerCase(Locale.ROOT);
        this.math = math;
    }

    public static double eval(String expression) {
        return eval(expression, MathDictionary.INSTANCE);
    }

    public static double eval(String expression, MathDictionary math) {
        return new ExpressionEvaluator(expression, math).eval();
    }

    private double eval() {
        this.pointer = 0;
        return thirdImportance();
    }

    private double thirdImportance() {
        double x = secondImportance();
        while (true) {
            if (progress('+')) x += secondImportance();
            else if (progress('-')) x -= secondImportance();
            else return x;
        }
    }

    private double secondImportance() {
        double x = firstImportance();
        while (true) {
            if (progress('*')) x *= firstImportance();
            else if (progress('/')) x /= firstImportance();
            else if (progress('%')) x %= firstImportance();
            else return x;
        }
    }

    private double firstImportance() {
        if (progress('-')) return -firstImportance(); // "-5", "--5"..
        //noinspection StatementWithEmptyBody
        while (progress('+')) /* just skip */; // "+5", "++5"..
        double x = 0;
        int start = pointer;
        if (progress('(')) {
            x = thirdImportance();
            progress(')');
        } else if (isDigit(current())) {
            pointer++;
            while (isDigit(current())) pointer++;
            if (progress('.')) {
                while (isDigit(current())) pointer++;
                if (progress('e')) {
                    if (!progress('-')) progress('+');
                    while (isDigit(current())) pointer++;
                }
            }
            x = MathDictionary.asDouble(expression.substring(start, pointer), 0);
        } else if (isLetter(current())) {
            pointer++;
            while (isLetter(current()) || isDigit(current())) pointer++;
            String str = expression.substring(start, pointer);
            if (progress('(')) {
                MathFunction function = math.getFunction(str);
                x = thirdImportance();
                if (!progress(',')) {
                    x = function == null
                            ? 0
                            : function.accept(x);
                } else {
                    double b = thirdImportance();
                    if (!progress(',')) {
                        x = function == null
                                ? 0
                                : function.accept(x, b);
                    } else {
                        double[] args = {b, thirdImportance()};
                        while (progress(',')) {
                            args = Arrays.copyOfRange(args, 0, args.length + 1);
                            args[args.length - 1] = thirdImportance();
                        }
                        x = function == null
                                ? 0
                                : function.accept(x, args);
                    }
                }
                progress(')');
            } else {
                x = math.getConstant(str, 0);
            }
        }

        if (progress('^')) x = Math.pow(x, firstImportance());
        return x;
    }

    private char current() {
        return expression.length() > pointer ? expression.charAt(pointer) : ' ';
    }

    private boolean progress(char c) {
        if (current() == c) {
            pointer++;
            return true;
        }
        return false;
    }
}
