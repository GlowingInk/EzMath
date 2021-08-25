package me.imdanix.math;

import java.util.Arrays;
import java.util.Locale;

/*
   All the code is inspired from Boann's answer on https://stackoverflow.com/a/26227947/9819521
   You can use this code however you want to
*/

/**
 * Math evaluator for "calc'n'go". Supports functions, constants
 * Actually can be fully replaced by {@link FormulasEvaluator}, but this one doesn't create additional objects
 */
public class MathEvaluator {
    private final String expression;
    private final MathBase math;
    private int pointer;

    private MathEvaluator(String expression, MathBase math) {
        this.expression = expression.replace(" ", "").toLowerCase(Locale.ENGLISH);
        this.math = math;
    }

    public static double eval(String expression, MathBase math) {
        return new MathEvaluator(expression, math).eval();
    }

    private double eval() {
        this.pointer = 0;
        return thirdImportance();
    }

    private double thirdImportance() {
        double x = secondImportance();
        while (true) {
            if (tryNext('+')) x += secondImportance();
            else if (tryNext('-')) x -= secondImportance();
            else return x;
        }
    }

    private double secondImportance() {
        double x = firstImportance();
        while (true) {
            if (tryNext('*')) x *= firstImportance();
            else if (tryNext('/')) x /= firstImportance();
            else if (tryNext('%')) x %= firstImportance();
            else return x;
        }
    }

    private double firstImportance() {
        if (tryNext('-')) return -firstImportance(); // "-5", "--5"..
        while (tryNext('+')) /* just skip */; // "+5", "++5"..
        double x = 0;
        int start = pointer;
        if (tryNext('(')) {
            x = thirdImportance();
            tryNext(')');
        } else if (MathBase.isNumberChar(current())) {
            pointer++;
            while (MathBase.isNumberChar(current())) pointer++;
            x = MathBase.getDouble(expression.substring(start, pointer), 0);
        } else if (MathBase.isWordChar(current())) {
            pointer++;
            while (MathBase.isWordChar(current()) || MathBase.isNumberChar(current())) pointer++;
            String str = expression.substring(start, pointer);
            if (tryNext('(')) {
                MathBase.Function function = math.getFunction(str);
                x = thirdImportance();
                double[] args = {};
                while (tryNext(',')) {
                    args = Arrays.copyOfRange(args, 0, args.length + 1);
                    args[args.length - 1] = thirdImportance();
                }
                if (function == null) {
                    x = 0;
                } else switch (args.length) {
                    case 0: x = function.eval(x); break;
                    case 1: x = function.eval(x, args[0]); break;
                    default: x = function.eval(x, args);
                }
                tryNext(')');
            } else {
                x = math.getConstant(str);
            }
        }

        if (tryNext('^')) x = Math.pow(x, firstImportance());
        return x;
    }

    private char current() {
        return expression.length() > pointer ? expression.charAt(pointer) : ' ';
    }

    private boolean tryNext(char c) {
        if (current() == c) {
            pointer++;
            return true;
        }
        return false;
    }
}
