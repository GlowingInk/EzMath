package me.imdanix.math;

import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;

public class FormulaEvaluatorTest extends BaseMathTest {
    @Test(dataProvider = "expressions")
    public void testEval(String expression, double expected) {
        assertEquals(new FormulaEvaluator(expression, math).eval(Map.of()), expected);
    }

    @Test
    public void variables() {
        FormulaEvaluator evaluator = new FormulaEvaluator("leet*13", math);
        assertEquals(evaluator.eval(Map.of("leet", 1337D)), 1337*13);
        assertEquals(evaluator.eval(Map.of("leet", 7331D)), 7331*13);
    }
}