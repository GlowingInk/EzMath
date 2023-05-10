package ink.glowing.math;

import org.testng.annotations.Test;

import java.util.Map;

import static java.lang.Math.sqrt;
import static org.testng.Assert.assertEquals;

public class FormulaEvaluatorTest extends BaseMathTest {
    @Test(dataProvider = "expressions")
    public void testEval(String expression, double expected) {
        assertEquals(new FormulaEvaluator(expression).eval(Map.of()), expected);
    }

    @Test
    public void testVariables() {
        FormulaEvaluator evaluator = new FormulaEvaluator("sqrt(leet*13)+other");
        assertEquals(evaluator.eval(Map.of("leet", 1337D, "other", 5D)), sqrt(1337*13)+5);
        assertEquals(evaluator.eval(Map.of("leet", 7331D, "other", 2D)), sqrt(7331*13)+2);
    }
}