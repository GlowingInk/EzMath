package me.imdanix.math;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;

public class MathDictionaryTest {
    @DataProvider
    public Object[][] invalidNames() {
        return new Object[][] {
                {"fUnc"},
                {"1func"},
                {"_func"},
                {"fun c"}
        };
    }

    @Test(dataProvider = "invalidNames", expectedExceptions = IllegalArgumentException.class)
    public void invalidNamesTest(String funct) {
        new MathDictionary(singletonMap(funct, a -> a), emptyMap());
    }

    @DataProvider
    public Object[][] existingNames() {
        return new Object[][] {
                {"sin"},
                {"cos"},
                {"format_float"}
        };
    }

    @Test(dataProvider = "existingNames", expectedExceptions = IllegalStateException.class)
    public void existingNamesTest(String funct) {
        new MathDictionary(singletonMap(funct, a -> a), emptyMap());
    }
}
