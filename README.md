# EzMath ![Latest Version](https://img.shields.io/github/v/tag/imDaniX/EzMath.svg?sort=semver&label=release)

Simple math evaluator. Supports constants, functions, variables.
Evaluators code is based off Boann's answer on [StackOverflow](https://stackoverflow.com/a/26227947/9819521).

It should be noted that the lib uses Java's `double` as its base number type, so it's very fast but not precise. 
That being said, it also means that calculations are affected by the overflow, so big numbers are a no-no. 
Also, EzMath throws no exceptions, so weird behaviour on invalid inputs is to be expected. 

## Format
### Decimal numbers
`123`, `-45.6`, `7.8E9`, `10.11E-12`
### Basic math operators
`1+2`, `3-4`, `5*6`, `7/8`, `9^10`, `11%12`
### Grouping
`(1+2)`, `(3^4) - 567`, `(89/(10*11)) / 1213`
### Constants
`pi/2`, `e^3`
<details><summary>List of available constants</summary>

#### Java provided constants
* `e` - the base of the natural logarithms
* `pi` - the ratio of the circumference of a circle to its diameter
* `infinity` - infinite value
* `nan` - not-a-number value
* `max_value` - the largest finite value that can be used in calculations
* `min_value` - the smallest nonzero value that can be used in calculations

#### EzMath provided constants
* `euler` - Euler's constant
* `phi` - the golden ratio value
* `ln2` - natural logarithm of 2
* `ln10` - natural logarithm of 10
* `log2e` - base-2 logarithm of E
* `log10e` - base-10 logarithm of E
</details>

### Functions
`sin(123)`, `sqrt(456)`, `max(78,910,11)`
<details><summary>List of available functions</summary>

#### Java provided functions
* `max` - greater of specified values
* `min` - smaller of specified values
* `cos` - trigonometric cosine of an angle
* `sin` - trigonometric sine of an angle
* `tan` - trigonometric tangent of an angle
* `acos` - arc cosine of a value
* `asin` - arc sine of a value
* `atan` - arc tangent of a value
* `cosh` - hyperbolic cosine of a value
* `sinh` - hyperbolic sine of a value
* `tanh` - hyperbolic tangent of a value
* `atan2` - angle theta from the conversion of rectangular coordinates x, y to polar coordinates (r, theta)
* `abs` - absolute value
* `log` - natural logarithm (base e)
* `log10` - base 10 logarithm
* `log1p` - natural logarithm of the sum of the argument and 1
* `ceil` - smallest value that is greater than or equal to the argument and is equal to a mathematical integer
* `floor` - largest value that is less than or equal to the argument and is equal to a mathematical integer
* `round` - closest value to the argument, with ties rounding to positive infinity.
* `rint` - value that is closest in value to the argument and is equal to a mathematical integer
* `pow` - the first argument raised to the power of the second argument
* `sqrt` - positive square root of a value
* `cbrt` - cube root of a value
* `hypot` - `sqrt(a^2+b^2)` without intermediate overflow or underflow
* `exp` - Euler's number e raised to the power of a value
* `expm1` - Euler's number e raised to the power of a value, minus 1
* `to_degrees` - angle measured in radians to approximately equivalent angle measured in degrees
* `to_radians` - angle measured in degrees to approximately equivalent angle measured in radians
* `get_exponent` - unbiased exponent used in the representation of a value
* `next_down` - floating-point value adjacent to a number in the direction of negative infinity
* `next_up` - floating-point value adjacent to a number in the direction of positive infinity
* `next_after` - floating-point number adjacent to the first argument in the direction of the second argument
* `signum` - signum function of the argument
* `ulp` - size of an ulp of the argument
* `ieee_remainder` - remainder operation on two arguments as prescribed by the IEEE 754 standard
* `copy_sign` - the first floating-point argument with the sign of the second floating-point argument
* `fma` - exact product of the first two arguments summed with the third argument and then rounded once
* `scalb` - `a*2^b` rounded as if performed by a single correctly rounded floating-point multiply

#### EzMath provided functions
* `format_float` - shortcut for `round(a*100)/100`
* `root` - shortcut for `pow(a,1/b)`
* `rng` - random generator
  * `rng(a)` - random number from 0 to a (exclusive)
  * `rng(a,b)` - random number from a to b (exclusive)
  * `rng(a,b,c...)` - one of random specified numbers
* `raw_hypot` - shortcut for `a^2+b^2`
</details>

## As dependency
Add jitpack repository
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
Add dependency (version might be outdated, see the top)
```xml
<dependency>
    <groupId>com.github.imDaniX</groupId>
    <artifactId>EzMath</artifactId>
    <version>v3.9</version>
</dependency>
```
