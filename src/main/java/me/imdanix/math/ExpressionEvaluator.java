package me.imdanix.math;

import java.util.Arrays;
import java.util.Locale;

import static me.imdanix.math.MathDictionary.isDigitChar;
import static me.imdanix.math.MathDictionary.isWordChar;

/**
 * Better performance for one-time calculations over {@link FormulaEvaluator}.
 * Unlike {@link FormulaEvaluator}, doesn't create additional memory garbage.
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
        } else if (isDigitChar(current())) {
            pointer++;
            while (isDigitChar(current())) pointer++;
            if (progress('.')) {
                while (isDigitChar(current())) pointer++;
                if (progress('e')) {
                    if (!progress('-')) progress('+');
                    while (isDigitChar(current())) pointer++;
                }
            }
            x = MathDictionary.asDouble(expression.substring(start, pointer), 0);
        } else if (isWordChar(current())) {
            pointer++;
            while (isWordChar(current()) || isDigitChar(current())) pointer++;
            String str = expression.substring(start, pointer);
            if (progress('(')) {
                MathDictionary.Function function = math.getFunction(str);
                x = thirdImportance();
                double[] args = {};
                while (progress(',')) {
                    args = Arrays.copyOfRange(args, 0, args.length + 1);
                    args[args.length - 1] = thirdImportance();
                }
                if (function == null) {
                    x = 0;
                } else {
                    x = switch (args.length) {
                        case 0 -> function.accept(x);
                        case 1 -> function.accept(x, args[0]);
                        default -> function.accept(x, args);
                    };
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
