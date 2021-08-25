package me.imdanix.math;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class MathBase {
    private static final Pattern FLOAT = Pattern.compile("-?\\d+(\\.\\d+)?");

    private final Map<String, Function> functions = new HashMap<>();
    private final Map<String, Double> constants = new HashMap<>();

    public MathBase() {
        for (DefaultFunctions func : DefaultFunctions.values())
            registerFunction(func.name(), func);
        registerConstant("e", Math.E);
        registerConstant("ln2", 0.693147180559945);
        registerConstant("ln10", 2.302585092994046);
        registerConstant("log2e", 1.442695040888963);
        registerConstant("euler", 0.577215664901533);
        registerConstant("log10e", 0.434294481903252);
        registerConstant("phi", 1.618033988749895);
        registerConstant("pi", Math.PI);
        registerConstant("dmax", Double.MAX_VALUE);
        registerConstant("dmin", Double.MIN_VALUE);
    }

    public boolean registerFunction(String name, Function function) {
        name = name.toLowerCase(Locale.ENGLISH);
        if (isAllowedName(name) && !functions.containsKey(name)) {
            functions.put(name, function);
            return true;
        }
        return false;
    }

    public boolean registerConstant(String name, double value) {
        name = name.toLowerCase(Locale.ENGLISH);
        if (isAllowedName(name) && !constants.containsKey(name)) {
            constants.put(name, value);
            return true;
        }
        return false;
    }

    public Function getFunction(String name) {
        return functions.get(name);
    }

    public double getConstant(String name) {
        Double value = constants.get(name);
        return value == null ? 0 : value;
    }

    public static boolean isNumberChar(char c) {
        return (c >= '0' && c <= '9') || c == '.';
    }

    public static boolean isWordChar(char c) {
        return (c >= 'a' && c <= 'z');
    }

    private static boolean isAllowedName(String str) {
        for (char c : str.toCharArray())
            if (!(isNumberChar(c) || isWordChar(c))) return false;
        return isWordChar(str.charAt(0));
    }

    public static double getDouble(String str, double def) {
        if (str == null || str.isEmpty() || !FLOAT.matcher(str).matches()) return def;
        return Double.parseDouble(str);
    }

    @FunctionalInterface
    public interface Function {
        default double eval(double a, double... num) {
            return eval(a, num[0]);
        }

        default double eval(double a, double b) {
            return eval(a);
        }

        double eval(double a);
    }
}
