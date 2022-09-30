package me.imdanix.math;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class FormulaEvaluatorTest extends BaseMathTest {
    @Test(dataProvider = "expressions")
    public void testEval(String expression, double expected) {
        assertEquals(new FormulaEvaluator(expression, math).eval(), expected);
    }

    @Test
    public void variables() {
        FormulaEvaluator evaluator = new FormulaEvaluator("leet*13", math);
        evaluator.setVariable("leet", 1337D);
        assertEquals(evaluator.eval(), 1337*13);
        evaluator.setVariable("leet", 7331D);
        assertEquals(evaluator.eval(), 7331*13);
    }
}