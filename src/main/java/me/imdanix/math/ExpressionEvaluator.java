package me.imdanix.math;

import java.util.Arrays;
import java.util.Locale;

/**
 * Best performance for one-time calculations over {@link FormulaEvaluator}.
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
            if (check('+')) x += secondImportance();
            else if (check('-')) x -= secondImportance();
            else return x;
        }
    }

    private double secondImportance() {
        double x = firstImportance();
        while (true) {
            if (check('*')) x *= firstImportance();
            else if (check('/')) x /= firstImportance();
            else if (check('%')) x %= firstImportance();
            else return x;
        }
    }

    private double firstImportance() {
        if (check('-')) return -firstImportance(); // "-5", "--5"..
        //noinspection StatementWithEmptyBody
        while (check('+')) /* just skip */; // "+5", "++5"..
        double x = 0;
        int start = pointer;
        if (check('(')) {
            x = thirdImportance();
            check(')');
        } else if (MathDictionary.isNumberChar(current())) {
            pointer++;
            while (MathDictionary.isNumberChar(current())) pointer++;
            x = MathDictionary.getDouble(expression.substring(start, pointer), 0);
        } else if (MathDictionary.isWordChar(current())) {
            pointer++;
            while (MathDictionary.isWordChar(current()) || MathDictionary.isNumberChar(current())) pointer++;
            String str = expression.substring(start, pointer);
            if (check('(')) {
                MathDictionary.Function function = math.getFunction(str);
                x = thirdImportance();
                double[] args = {};
                while (check(',')) {
                    args = Arrays.copyOfRange(args, 0, args.length + 1);
                    args[args.length - 1] = thirdImportance();
                }
                if (function == null) {
                    x = 0;
                } else {
                    x = switch (args.length) {
                        case 0 -> function.eval(x);
                        case 1 -> function.eval(x, args[0]);
                        default -> function.eval(x, args);
                    };
                }
                check(')');
            } else {
                x = math.getConstant(str, 0);
            }
        }

        if (check('^')) x = Math.pow(x, firstImportance());
        return x;
    }

    private char current() {
        return expression.length() > pointer ? expression.charAt(pointer) : ' ';
    }

    private boolean check(char c) {
        if (current() == c) {
            pointer++;
            return true;
        }
        return false;
    }
}
