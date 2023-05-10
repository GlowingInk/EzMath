package ink.glowing.math;

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
    public static final Map<String, MathFunction> BASIC_FUNCTIONS;
    public static final Map<String, Double> BASIC_CONSTANTS;
    static {
        Map<String, MathFunction> basicFunctions = new HashMap<>(
                SingleArgFunctions.values().length + MultiArgFunctions.values().length
        );
        for (SingleArgFunctions func : SingleArgFunctions.values()) {
            basicFunctions.put(func.name().toLowerCase(Locale.ROOT), func.getFunction());
        }
        for (MultiArgFunctions func : MultiArgFunctions.values()) {
            basicFunctions.put(func.name().toLowerCase(Locale.ROOT), func);
        }
        Map<String, Double> basicConstants = new HashMap<>(13);
        basicConstants.put("max_value", Double.MAX_VALUE);
        basicConstants.put("min_value", Double.MIN_VALUE);
        basicConstants.put("infinity", Double.POSITIVE_INFINITY);
        basicConstants.put("nan", Double.NaN);
        basicConstants.put("e", Math.E);
        basicConstants.put("pi", Math.PI);
        basicConstants.put("tau", Math.PI * 2);
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

    private final Map<String, MathFunction> functions;
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
    public MathDictionary(Map<String, MathFunction> functions, Map<String, Double> constants) {
        this();
        tryRegister("Function", this.functions, functions);
        tryRegister("Constant", this.constants, constants);
    }

    private static <T> void tryRegister(String what, Map<String, T> in, Map<String, T> out) {
        if (out == null || out.isEmpty()) return;
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

    public MathFunction getFunction(String name) {
        return functions.get(name);
    }

    public double getConstant(String name, double def) {
        Double value = constants.get(name);
        return value == null ? def : value;
    }

    public Double getConstant(String name) {
        return constants.get(name);
    }

    public static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || c == '_';
    }

    public static double asDouble(String str, double def) {
        if (!FLOAT.matcher(str).matches()) return def;
        return Double.parseDouble(str);
    }

    @FunctionalInterface
    public interface MathFunction {
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
        LOG10 (a -> Math.log10(a)),
        LOG1P(a -> Math.log1p(a)),
        CEIL(a -> Math.ceil(a)),
        FLOOR(a -> Math.floor(a)),
        ROUND(a -> Math.round(a)),
        RINT(a -> Math.rint(a)),
        SQRT(a -> Math.sqrt(a)),
        CBRT(a -> Math.cbrt(a)),
        EXP(a -> Math.exp(a)),
        EXPM1 (a -> Math.expm1(a)),
        TO_DEGREES(a -> Math.toDegrees(a)),
        TO_RADIANS(a -> Math.toRadians(a)),
        GET_EXPONENT(a -> Math.getExponent(a)),
        NEXT_DOWN(a -> Math.nextDown(a)),
        NEXT_UP(a -> Math.nextUp(a)),
        SIGNUM(a -> Math.signum(a)),
        ULP(a -> Math.ulp(a)),
        TRUNC(a -> a > 0 ? Math.floor(a) : Math.ceil(a)),
        FORMAT_FLOAT((a) -> TRUNC.internal.accept(a * 100D) / 100D);

        private final MathFunction internal;

        SingleArgFunctions(MathFunction internal) {
            this.internal = internal;
        }

        public MathFunction getFunction() {
            return internal;
        }
    }

    private enum MultiArgFunctions implements MathFunction {
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
        LOG {
            @Override
            public double accept(double a, double b) {
                return Math.log(a) / Math.log(b);
            }

            @Override
            public double accept(double a) {
                return Math.log(a);
            }
        },
        HYPOT {
            @Override
            public double accept(double a, double b) {
                return Math.hypot(a, b);
            }

            @Override
            public double accept(double a, double... num) {
                a *= a;
                for (double b : num) {
                    a += b*b;
                }
                return Math.sqrt(a);
            }
        },
        RAW_HYPOT {
            @Override
            public double accept(double a, double b) {
                return a*a + b*b;
            }

            @Override
            public double accept(double a, double... num) {
                a *= a;
                for (double b : num) {
                    a += b*b;
                }
                return a;
            }

            @Override
            public double accept(double a) {
                return a*a;
            }
        },
        RANDOM {
            @Override
            public double accept(double a, double b) {
                if (a < b) {
                    return ThreadLocalRandom.current().nextDouble(a, b == Double.POSITIVE_INFINITY ? Double.MAX_VALUE : b);
                } else {
                    return a;
                }
            }

            @Override
            public double accept(double a) {
                if (a > 0) {
                    return ThreadLocalRandom.current().nextDouble(a);
                } else if (a < 0) {
                    return -ThreadLocalRandom.current().nextDouble(0d - a);
                } else {
                    return 0;
                }
            }
        },
        RNG_CHOICE {
            @Override
            public double accept(double a, double... num) {
                int choice = ThreadLocalRandom.current().nextInt(num.length + 1);
                return choice == 0 ? a : num[choice - 1];
            }

            @Override
            public double accept(double a, double b) {
                return ThreadLocalRandom.current().nextBoolean() ? a : b;
            }
        },
        ROOT {
            @Override
            public double accept(double a, double b) {
                return Math.pow(a, 1/b);
            }
        },
        AVG {
            @Override
            public double accept(double a, double... num) {
                for (double b : num) {
                    a += b;
                }
                return a / (num.length + 1);
            }

            @Override
            public double accept(double a, double b) {
                return (a+b) / 2;
            }
        },
        LOG_GAMMA {
            private static final double[] LANCZOS = {
                    1.000000000190015,
                    76.18009172947146,
                    -86.50532032941676,
                    24.01409824083091,
                    -1.2317395724501554,
                    0.0012086509738661786,
                    -5.395239384953128E-6,
            };

            @Override
            public double accept(double a) {
                double tmp = (a - 0.5) * Math.log(a + 4.5) - (a + 4.5);
                double appx = LANCZOS[0];
                for (int i = 1; i < LANCZOS.length; i++) {
                    appx += LANCZOS[i] / (a + i - 1);
                }
                return tmp + Math.log(appx * Math.sqrt(2 * Math.PI));
            }
        },
        GAMMA {
            @Override
            public double accept(double a) {
                if (a > 0) {
                    if (a == 1) {
                        return 1;
                    } else if ((int) a == a) {
                        double res = 1;
                        for (int i = 2, max = (int) a - 1; i <= max; i++) {
                            res *= i;
                        }
                        return res;
                    } else {
                        return Math.exp(LOG_GAMMA.accept(a));
                    }
                } else {
                    return Double.POSITIVE_INFINITY;
                }
            }
        };

        @Override
        public double accept(double a) {
            return a;
        }
    }
}
