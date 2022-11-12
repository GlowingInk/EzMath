# EzMath

Simple math evaluator. Supports constants, functions, variables.
Evaluators code is based off Boann's answer on [StackOverflow](https://stackoverflow.com/a/26227947/9819521).

It should be noted that the lib uses `double` as its base number type, so it's very fast but not absolutely precise. 
Also, the lib throws no exceptions, so weird behaviour on invalid inputs is to be expected. 

## Format
### Decimal numbers
`123`, `-45.6`, `7.8E9`, `10.11E-12`
### Basic math operators
`1+2`, `3-4`, `5*6`, `7/8`, `9^10`, `11%12`
### Grouping
`(1+2)`, `(3^4) - 567`, `(89/(10*11)) / 1213`
### Constants
`pi/2`, `e^3`
### Functions
`sin(123)`, `sqrt(456)`, `max(78,910,11)`

## As dependency
Add jitpack repository
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
Add dependency
```xml
<dependency>
    <groupId>com.github.imDaniX</groupId>
    <artifactId>EzMath</artifactId>
    <version>v3.5</version>
</dependency>
```
