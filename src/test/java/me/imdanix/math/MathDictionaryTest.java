package me.imdanix.math;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Map;

public class MathDictionaryTest {
    @DataProvider
    public Object[][] invalidNames() {
        return new Object[][] {
                {Collections.singletonMap("Func", MathDictionary.Function.SELF)},
                {Collections.singletonMap("1func", MathDictionary.Function.SELF)},
                {Collections.singletonMap("_func", MathDictionary.Function.SELF)},
                {Collections.singletonMap("fun c", MathDictionary.Function.SELF)}
        };
    }

    @Test(dataProvider = "invalidNames", expectedExceptions = IllegalArgumentException.class)
    public void invalidNamesTest(Map<String, MathDictionary.Function> functions) {
        new MathDictionary(functions, Collections.emptyMap());
    }

    @DataProvider
    public Object[][] existingNames() {
        return new Object[][] {
                {Collections.singletonMap("sin", MathDictionary.Function.SELF)},
                {Collections.singletonMap("cos", MathDictionary.Function.SELF)},
                {Collections.singletonMap("format_float", MathDictionary.Function.SELF)}
        };
    }

    @Test(dataProvider = "existingNames", expectedExceptions = IllegalStateException.class)
    public void existingNamesTest(Map<String, MathDictionary.Function> functions) {
        new MathDictionary(functions, Collections.emptyMap());
    }
}
