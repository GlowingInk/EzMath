# EzMath
Simple math evaluator. Supports constants, functions, variables.

## MathEvaluator.java
```java
public static void calc(String str) {
    System.out.println(MathEvaluator.eval(str));
}
```

## FormulasEvaluator.java
```java
public static void calc(String str, Map<String, Double> vars, Map<String, Double> vars2) {
    FormualsEvaluator evaluator = new FormualsEvaluator(str);
    evaluator.setVariables(vars);
    System.out.println(evaluator.eval());
    evaluator.setVariables(vars2);
    System.out.println(evaluator.eval());
}
```

## Register functions and constants
```java
public static void constant(String str, Double value) {
    if(MathBase.registerConstant(str, value))
        System.out.println("Constant " + str + " successfully registered with value " + value);
    else
        System.out.println("Constant " + str + " is already registered with va;ue " + MathBase.getConstant(str));    
}

public static void function(String str, MathBase.Function func) {
    if(MathBase.registerFunction(str, func))
        System.out.println("Function " + str + " successfully registered");
    else
        System.out.println("Function " + str + " is already registered");    

}
```