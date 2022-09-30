package me.imdanix.math;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathDictionary {
    private static final Pattern FLOAT = Pattern.compile("-?\\d+(\\.\\d+)?");
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-z][a-z\\d_]+");

    private static final Map<String, Function> BASIC_FUNCTIONS;
    private static final Map<String, Double> BASIC_CONSTANTS;
    static {
        BASIC_FUNCTIONS = new HashMap<>(DefaultFunctions.values().length);
        for (DefaultFunctions func : DefaultFunctions.values())
            BASIC_FUNCTIONS.put(func.name().toLowerCase(Locale.ROOT), func);
        BASIC_CONSTANTS = new HashMap<>(10);
        BASIC_CONSTANTS.put("e", Math.E);
        BASIC_CONSTANTS.put("ln2", 0.693147180559945);
        BASIC_CONSTANTS.put("ln10", 2.302585092994046);
        BASIC_CONSTANTS.put("log2e", 1.442695040888963);
        BASIC_CONSTANTS.put("euler", 0.577215664901533);
        BASIC_CONSTANTS.put("log10e", 0.434294481903252);
        BASIC_CONSTANTS.put("phi", 1.618033988749895);
        BASIC_CONSTANTS.put("pi", Math.PI);
        BASIC_CONSTANTS.put("max_value", Double.MAX_VALUE);
        BASIC_CONSTANTS.put("min_value", Double.MIN_VALUE);
    }

    private final Map<String, Function> functions;
    private final Map<String, Double> constants;

    public MathDictionary() {
        this.functions = new HashMap<>(BASIC_FUNCTIONS);
        this.constants = new HashMap<>(BASIC_CONSTANTS);
    }

    public MathDictionary(Map<String, Function> functions, Map<String, Double> constants) {
        this();
        tryRegister("Function", functions, this.functions);
        tryRegister("Constant", constants, this.constants);
    }

    private static <T> void tryRegister(String what, Map<String, T> out, Map<String, T> in) {
        for (Map.Entry<String, T> entry : out.entrySet()) {
            String name = entry.getKey().toLowerCase(Locale.ROOT);
            Matcher matcher = NAME_PATTERN.matcher(name);
            if (!matcher.matches()) {
                throw new IllegalArgumentException(what + " name '" + name + "' doesn't " +
                        "follow the name pattern '" + NAME_PATTERN + "'");
            }
            if (in.containsKey(name)) {
                throw new IllegalStateException(what + " under the name '" + name + "' is already registered");
            }
            in.put(name, entry.getValue());
        }
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
        return (c >= 'a' && c <= 'z') || c == '_';
    }

    public static double getDouble(String str, double def) {
        if (!FLOAT.matcher(str).matches()) return def;
        return Double.parseDouble(str);
    }

    @FunctionalInterface
    public interface Function {
        Function SELF = a -> a;

        default double eval(double a, double... num) {
            return eval(a, num[0]);
        }

        default double eval(double a, double b) {
            return eval(a);
        }

        double eval(double a);
    }

    /**
     * Some default math functions
     */
    private enum DefaultFunctions implements Function {
        ABS(a -> Math.abs(a)),
        ACOS(a -> Math.acos(a)),
        ASIN(a -> Math.asin(a)),
        ATAN(a -> Math.atan(a)),
        ATAN2 {
            @Override
            public double eval(double a, double b) {
                return Math.atan2(a, b);
            }
        },
        CBRT(a -> Math.cbrt(a)),
        CEIL(a -> Math.ceil(a)),
        COS(a -> Math.cos(a)),
        COSH(a -> Math.cosh(a)),
        EXP(a -> Math.exp(a)),
        EXPM1 (a -> Math.expm1(a)),
        FLOOR(a -> Math.floor(a)),
        GET_EXPONENT(a -> Math.getExponent(a)),
        LOG(a -> Math.log(a)),
        LOG10 (a -> Math.log10(a)),
        LOG1P(a -> Math.log1p(a)),
        MAX {
            @Override
            public double eval(double a, double... num) {
                for (double j : num)
                    a = Math.max(a, j);
                return a;
            }

            @Override
            public double eval(double a, double b) {
                return Math.max(a, b);
            }
        },
        MIN {
            @Override
            public double eval(double a, double... num) {
                for (double j : num)
                    a = Math.min(a, j);
                return a;
            }

            @Override
            public double eval(double a, double b) {
                return Math.min(a, b);
            }
        },
        NEXT_AFTER {
            @Override
            public double eval(double a, double b) {
                return Math.nextAfter(a, b);
            }
        },
        NEXT_DOWN(a -> Math.nextDown(a)),
        NEXT_UP(a -> Math.nextUp(a)),
        ROUND(a -> Math.round(a)),
        RINT(a -> Math.rint(a)),
        SIGNUM(a -> Math.signum(a)),
        SIN(a -> Math.sin(a)),
        SINH(a -> Math.sinh(a)),
        SQRT(a -> Math.sqrt(a)),
        TAN(a -> Math.tan(a)),
        TANH(a -> Math.tanh(a)),
        TO_DEGREES(a -> Math.toDegrees(a)),
        TO_RADIANS(a -> Math.toRadians(a)),
        ULP(a -> Math.ulp(a)),
        HYPOT {
            @Override
            public double eval(double a, double b) {
                return Math.hypot(a, b);
            }
        },
        IEEE_REMAINDER {
            @Override
            public double eval(double a, double b) {
                return Math.IEEEremainder(a, b);
            }
        },
        FORMAT_FLOAT((a) -> Math.round(a * 100D) / 100D);

        private final Function internal;

        DefaultFunctions() {
            internal = SELF;
        }

        DefaultFunctions(Function internal) {
            this.internal = internal;
        }

        @Override
        public double eval(double a) {
            return internal.eval(a);
        }
    }
}
