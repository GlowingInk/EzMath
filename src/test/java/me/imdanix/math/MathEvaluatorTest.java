package me.imdanix.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MathEvaluatorTest {
    @Test
    void basic() {
        MathBase math = new MathBase();
        assertEquals(10, MathEvaluator.eval("5+5", math));
        assertEquals(9348, MathEvaluator.eval("123*456/(7+8-9)", math));
    }

    @Test
    void constants() {
        MathBase math = new MathBase();
        assertEquals(Math.E, MathEvaluator.eval("e", math));
        assertEquals((Math.PI + Math.E) / 13D, MathEvaluator.eval("(pi+e)/13", math));
    }

    @Test
    void functions() {
        MathBase math = new MathBase();
        assertEquals(Math.cos(234.66), MathEvaluator.eval("cos(234.66)", math));
        assertEquals(Math.hypot(12, 34) / 56D, MathEvaluator.eval("hypot(12,34)/56", math));
    }
}