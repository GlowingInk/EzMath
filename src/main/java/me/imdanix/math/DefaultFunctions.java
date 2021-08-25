package me.imdanix.math;

/**
 * Some default math functions
 */
// TODO: Move to MathBase?
enum DefaultFunctions implements MathBase.Function {
    ABS {
        @Override
        public double eval(double a) {
            return Math.abs(a);
        }
    },
    ACOS {
        @Override
        public double eval(double a) {
            return Math.acos(a);
        }
    },
    ASIN {
        @Override
        public double eval(double a) {
            return Math.asin(a);
        }
    },
    ATAN {
        @Override
        public double eval(double a) {
            return Math.atan(a);
        }
    },
    ATAN2 {
        @Override
        public double eval(double a, double b) {
            return Math.atan2(a, b);
        }

        @Override
        public double eval(double a) {
            return a;
        }
    },
    CBRT {
        @Override
        public double eval(double a) {
            return Math.cbrt(a);
        }
    },
    CEIL {
        @Override
        public double eval(double a) {
            return Math.ceil(a);
        }
    },
    COS {
        @Override
        public double eval(double a) {
            return Math.cos(a);
        }
    },
    COSH {
        @Override
        public double eval(double a) {
            return Math.cosh(a);
        }
    },
    EXP {
        @Override
        public double eval(double a) {
            return Math.exp(a);
        }
    },
    EXPM1 {
        @Override
        public double eval(double a) {
            return Math.expm1(a);
        }
    },
    FLOOR {
        @Override
        public double eval(double a) {
            return Math.floor(a);
        }
    },
    GETEXPONENT {
        @Override
        public double eval(double a) {
            return Math.getExponent(a);
        }
    },
    LOG {
        @Override
        public double eval(double a) {
            return Math.log(a);
        }
    },
    LOG10 {
        @Override
        public double eval(double a) {
            return Math.log10(a);
        }
    },
    LOG1P {
        @Override
        public double eval(double a) {
            return Math.log1p(a);
        }
    },
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

        @Override
        public double eval(double a) {
            return a;
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

        @Override
        public double eval(double a) {
            return a;
        }
    },
    NEXTAFTER {
        @Override
        public double eval(double a, double b) {
            return Math.nextAfter(a, b);
        }

        @Override
        public double eval(double a) {
            return a;
        }
    },
    NEXTDOWN {
        @Override
        public double eval(double a) {
            return Math.nextDown(a);
        }
    },
    NEXTUP {
        @Override
        public double eval(double a) {
            return Math.nextUp(a);
        }
    },
    ROUND {
        @Override
        public double eval(double a) {
            return Math.round(a);
        }
    },
    RINT {
        @Override
        public double eval(double a) {
            return Math.rint(a);
        }
    },
    SIGNUM {
        @Override
        public double eval(double a) {
            return Math.signum(a);
        }
    },
    SIN {
        @Override
        public double eval(double a) {
            return Math.sin(a);
        }
    },
    SINH {
        @Override
        public double eval(double a) {
            return Math.sinh(a);
        }
    },
    SQRT {
        @Override
        public double eval(double a) {
            return Math.sqrt(a);
        }
    },
    TAN {
        @Override
        public double eval(double a) {
            return Math.tan(a);
        }
    },
    TANH {
        @Override
        public double eval(double a) {
            return Math.tanh(a);
        }
    },
    TODEGREES {
        @Override
        public double eval(double a) {
            return Math.toDegrees(a);
        }
    },
    TORADIANS {
        @Override
        public double eval(double a) {
            return Math.toRadians(a);
        }
    },
    ULP {
        @Override
        public double eval(double a) {
            return Math.ulp(a);
        }
    },
    HYPOT {
        @Override
        public double eval(double a, double b) {
            return Math.hypot(a, b);
        }

        @Override
        public double eval(double a) {
            return a;
        }
    },
    IEEEREMAINDER {
        @Override
        public double eval(double a, double b) {
            return Math.IEEEremainder(a, b);
        }

        @Override
        public double eval(double a) {
            return a;
        }
    },
    FORMATFLOAT {
        @Override
        public double eval(double a) {
            // TODO: Count of chars after dot
            return Math.round(a * 100D) / 100D;
        }
    }
}
