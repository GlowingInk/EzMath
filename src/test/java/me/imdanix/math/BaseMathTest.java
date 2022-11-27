package me.imdanix.math;

import org.testng.annotations.DataProvider;

import static java.lang.Math.*;

public abstract class BaseMathTest {
    @DataProvider
    public static Object[][] expressions() {
        return new Object[][] {
                {"5+5", 5+5},
                {"10-8.2", 10-8.2},
                {"10--8", 10-(-8)},
                {"123*456/(7+8-9)", 123*456d/(7+8-9)},
                {"123.0123E123", 123.0123E123},
                {"123.0123E-12 + 3", 123.0123E-12 + 3},
                {"e", E},
                {"-1/0 + 1", Double.NEGATIVE_INFINITY},
                {"-infinity + infinity", Double.NaN},
                {"(pi+e)/13", (PI + E) / 13d},
                {"max(10,e,20)", max(10, max(E, 20))},
                {"sin(sqrt(5))^2", pow(sin(sqrt(5)), 2)},
                {"cos(234.66+5)", cos(234.66+5)},
                {"hypot(12,sin(34))/56", hypot(12, sin(34)) / 56d},
                {"fake_funct(5)+other_fake(1,2)+1", 1}
        };
    }
}
