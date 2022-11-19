package me.imdanix.math;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The dictionary for basic math constants and functions.
 * <p>
 * Intended to be immutable after its creation, yet if you're from parallel universe with altered math constants than
 * provided initially, the class is still open for extending.
 */
public class MathDictionary {
    private static final Pattern FLOAT = Pattern.compile("\\d+(\\.\\d+(e[+-]?\\d+)?)?");

    public static final Pattern NAME_PATTERN = Pattern.compile("[a-z][a-z\\d_]+");
    public static final Map<String, Function> BASIC_FUNCTIONS;
    public static final Map<String, Double> BASIC_CONSTANTS;
    static {
        Map<String, Function> basicFunctions = new HashMap<>(SingleArgFunctions.values().length);
        for (SingleArgFunctions func : SingleArgFunctions.values()) {
            basicFunctions.put(func.name().toLowerCase(Locale.ROOT), func.getFunction());
        }
        for (MultiArgFunctions func : MultiArgFunctions.values()) {
            basicFunctions.put(func.name().toLowerCase(Locale.ROOT), func);
        }
        Map<String, Double> basicConstants = new HashMap<>(12);
        basicConstants.put("e", Math.E);
        basicConstants.put("pi", Math.PI);
        basicConstants.put("max_value", Double.MAX_VALUE);
        basicConstants.put("min_value", Double.MIN_VALUE);
        basicConstants.put("infinity", Double.POSITIVE_INFINITY);
        basicConstants.put("nan", Double.NaN);
        basicConstants.put("euler", 0.577215664901533);
        basicConstants.put("phi", 1.618033988749895);
        basicConstants.put("ln2", 0.693147180559945);
        basicConstants.put("ln10", 2.302585092994046);
        basicConstants.put("log2e", 1.442695040888963);
        basicConstants.put("log10e", 0.434294481903252);

        BASIC_FUNCTIONS = Collections.unmodifiableMap(basicFunctions);
        BASIC_CONSTANTS = Collections.unmodifiableMap(basicConstants);
    }

    public static MathDictionary INSTANCE = new MathDictionary();

    private final Map<String, Function> functions;
    private final Map<String, Double> constants;

    private MathDictionary() {
        this.functions = new HashMap<>(BASIC_FUNCTIONS);
        this.constants = new HashMap<>(BASIC_CONSTANTS);
    }

    /**
     * Creates a new dictionary with custom functions and constants.
     * Names are expected to be following the {@link MathDictionary#NAME_PATTERN} pattern.
     * You can't provide duplicate constants or functions.
     * @param functions additional functions to register
     * @param constants additional constants to register
     */
    public MathDictionary(Map<String, Function> functions, Map<String, Double> constants) {
        this();
        tryRegister("Function", this.functions, functions);
        tryRegister("Constant", this.constants, constants);
    }

    private static <T> void tryRegister(String what, Map<String, T> in, Map<String, T> out) {
        if (out == null) return;
        for (Map.Entry<String, T> entry : out.entrySet()) {
            String name = entry.getKey();
            Matcher matcher = NAME_PATTERN.matcher(name);
            if (!matcher.matches()) {
                throw new IllegalArgumentException(what + " name '" + name + "' doesn't " +
                        "follow the name pattern '" + NAME_PATTERN + "'");
            }
            if (in.containsKey(name)) {
                throw new IllegalStateException(what + " under the name '" + name + "' is already registered");
            }
            if (entry.getValue() == null) {
                throw new NullPointerException(what + " under the name '" + name + "' has no value");
            }
            in.put(name, entry.getValue());
        }
    }

    public Function getFunction(String name) {
        return functions.get(name);
    }

    public double getConstant(String name, double def) {
        Double value = constants.get(name);
        return value == null ? def : value;
    }

    public Double getConstant(String name) {
        return constants.get(name);
    }

    public static boolean isDigitChar(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isWordChar(char c) {
        return (c >= 'a' && c <= 'z') || c == '_';
    }

    public static double asDouble(String str, double def) {
        if (!FLOAT.matcher(str).matches()) return def;
        return Double.parseDouble(str);
    }

    @FunctionalInterface
    public interface Function {
        Function SELF = a -> a;

        /**
         * Calculate result of function for desired numbers
         * @param a the first input number of function
         * @param num other input number arguments, expected length >= 2
         * @return result of calculation
         */
        default double accept(double a, double... num) {
            return accept(a, num[0]);
        }

        /**
         * Calculate result of function for desired numbers
         * @param a the first input number
         * @param b the second input number
         * @return result of calculation
         */
        default double accept(double a, double b) {
            return accept(a);
        }

        /**
         * Calculate result of function for desired number
         * @param a input number
         * @return result of calculation
         */
        double accept(double a);
    }

    /**
     * Some default math functions
     */
    private enum SingleArgFunctions {
        COS(a -> Math.cos(a)),
        SIN(a -> Math.sin(a)),
        TAN(a -> Math.tan(a)),
        ACOS(a -> Math.acos(a)),
        ASIN(a -> Math.asin(a)),
        ATAN(a -> Math.atan(a)),
        COSH(a -> Math.cosh(a)),
        SINH(a -> Math.sinh(a)),
        TANH(a -> Math.tanh(a)),
        ABS(a -> Math.abs(a)),
        LOG(a -> Math.log(a)),
        LOG10 (a -> Math.log10(a)),
        LOG1P(a -> Math.log1p(a)),
        CEIL(a -> Math.ceil(a)),
        FLOOR(a -> Math.floor(a)),
        ROUND(a -> Math.round(a)), // TODO Round to specific place, replace FORMAT_FLOAT
        SQRT(a -> Math.sqrt(a)),
        CBRT(a -> Math.cbrt(a)),
        EXP(a -> Math.exp(a)),
        EXPM1 (a -> Math.expm1(a)),
        TO_DEGREES(a -> Math.toDegrees(a)),
        TO_RADIANS(a -> Math.toRadians(a)),
        GET_EXPONENT(a -> Math.getExponent(a)),
        NEXT_DOWN(a -> Math.nextDown(a)),
        NEXT_UP(a -> Math.nextUp(a)),
        RINT(a -> Math.rint(a)),
        SIGNUM(a -> Math.signum(a)),
        ULP(a -> Math.ulp(a)),
        FORMAT_FLOAT((a) -> Math.round(a * 100D) / 100D);

        private final Function internal;

        SingleArgFunctions(Function internal) {
            this.internal = internal;
        }

        public Function getFunction() {
            return internal;
        }
    }

    private enum MultiArgFunctions implements Function {
        MAX {
            @Override
            public double accept(double a, double... num) {
                for (double j : num) {
                    a = Math.max(a, j);
                }
                return a;
            }

            @Override
            public double accept(double a, double b) {
                return Math.max(a, b);
            }
        },
        MIN {
            @Override
            public double accept(double a, double... num) {
                for (double j : num) {
                    a = Math.min(a, j);
                }
                return a;
            }

            @Override
            public double accept(double a, double b) {
                return Math.min(a, b);
            }
        },
        ROOT {
            @Override
            public double accept(double a, double b) {
                return Math.pow(a, 1/b);
            }
        },
        POW {
            @Override
            public double accept(double a, double b) {
                return Math.pow(a, b);
            }
        },
        ATAN2 {
            @Override
            public double accept(double a, double b) {
                return Math.atan2(a, b);
            }
        },
        HYPOT {
            @Override
            public double accept(double a, double b) {
                return Math.hypot(a, b);
            }
        },
        NEXT_AFTER {
            @Override
            public double accept(double a, double b) {
                return Math.nextAfter(a, b);
            }
        },
        IEEE_REMAINDER {
            @Override
            public double accept(double a, double b) {
                return Math.IEEEremainder(a, b);
            }
        },
        COPY_SIGN {
            @Override
            public double accept(double a, double b) {
                return Math.copySign(a, b);
            }
        },
        FMA {
            @Override
            public double accept(double a, double... num) {
                return Math.fma(a, num[0], num[1]);
            }
        },
        SCALB {
            @Override
            public double accept(double a, double b) {
                return Math.scalb(a, (int) Math.round(b));
            }
        },
        RNG {
            @Override
            public double accept(double a, double... num) {
                int choice = ThreadLocalRandom.current().nextInt(num.length + 1);
                return choice == 0 ? a : num[choice];
            }

            @Override
            public double accept(double a, double b) {
                return ThreadLocalRandom.current().nextDouble(a, b);
            }

            @Override
            public double accept(double a) {
                return ThreadLocalRandom.current().nextDouble(a);
            }
        },
        RAW_HYPOT {
            @Override
            public double accept(double a, double b) {
                return a*a + b*b;
            }
        };

        @Override
        public double accept(double a) {
            return a;
        }
    }
}
