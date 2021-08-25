package me.imdanix.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormulasEvaluatorTest {
    @Test
    void basic() {
        MathBase math = new MathBase();
        assertEquals(10, new FormulasEvaluator("5+5", math).eval());
        assertEquals(9348, new FormulasEvaluator("123*456/(7+8-9)", math).eval());
    }

    @Test
    void constants() {
        MathBase math = new MathBase();
        assertEquals(Math.E, new FormulasEvaluator("e", math).eval());
        assertEquals((Math.PI + Math.E) / 13D, new FormulasEvaluator("(pi+e)/13", math).eval());
    }

    /*
    @Test
    void functions() {
        MathBase math = new MathBase();
        assertEquals(Math.cos(234.66), new FormulasEvaluator("cos(234.66)", math).eval());
        assertEquals(Math.hypot(12, 34) / 56D, new FormulasEvaluator("hypot(12,34)/56", math).eval());
    }
    */

    @Test
    void variables() {
        MathBase math = new MathBase();
        FormulasEvaluator evaluator = new FormulasEvaluator("leet*13", math);
        evaluator.setVariable("leet", 1337D);
        assertEquals(1337*13, evaluator.eval());
        evaluator = new FormulasEvaluator("time/pi", math);
        evaluator.setVariable("time", 4.20D);
        assertEquals(4.20/Math.PI, evaluator.eval());
    }
}