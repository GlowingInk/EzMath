package me.imdanix.math;

import org.testng.annotations.DataProvider;

public abstract class BaseMathTest {
    protected static final MathDictionary math = MathDictionary.INSTANCE;

    @DataProvider
    public static Object[][] expressions() {
        return new Object[][] {
                {"5+5", 5+5},
                {"123*456/(7+8-9)", 123*456d/(7+8-9)},
                {"e", Math.E},
                {"(pi+e)/13", (Math.PI + Math.E) / 13D},
                {"cos(234.66+5)", Math.cos(234.66+5)},
                {"hypot(12,sin(34))/56", Math.hypot(12, Math.sin(34)) / 56D}
        };
    }
}
