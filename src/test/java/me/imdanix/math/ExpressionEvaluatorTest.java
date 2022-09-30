package me.imdanix.math;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ExpressionEvaluatorTest extends BaseMathTest {
    @Test(dataProvider = "expressions")
    public void testEval(String expression, double expected) {
        assertEquals(ExpressionEvaluator.eval(expression, math), expected);
    }
}