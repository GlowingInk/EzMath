package me.imdanix.math;

import org.testng.annotations.DataProvider;

import static java.lang.Math.*;

public abstract class BaseMathTest {
    @DataProvider
    public static Object[][] expressions() {
        return new Object[][] {
                {"5+5", 5+5},
                {"10-8", 2},
                {"10--8", 18},
                {"123*456/(7+8-9)", 123*456d/(7+8-9)},
                {"e", E},
                {"-infinity", Double.NEGATIVE_INFINITY},
                {"(pi+e)/13", (PI + E) / 13D},
                {"max(10,e,20)", max(10, max(E, 20))},
                {"sin(sqrt(5))^2", pow(sin(sqrt(5)), 2)},
                {"cos(234.66+5)", cos(234.66+5)},
                {"hypot(12,sin(34))/56", hypot(12, sin(34)) / 56D}
        };
    }
}
