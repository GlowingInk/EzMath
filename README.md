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

* `e` - Euler's number - the base of the natural logarithms
* `pi` - the ratio of the circumference of a circle to its diameter
* `infinity` - infinite value
* `nan` - not-a-number value
* `max_value` - the largest finite value that can be used in calculations
* `min_value` - the smallest positive value that can be used in calculations
* `euler` - Euler's (Euler-Mascheroni) constant
* `phi` - the golden ratio value
* `ln2` - natural logarithm of 2
* `ln10` - natural logarithm of 10
* `log2e` - base 2 logarithm of E
* `log10e` - base 10 logarithm of E
</details>

### Functions
`sin(123)`, `sqrt(456)`, `max(78,910,11)`
<details><summary>List of available functions</summary>

* `max(a,b...)` - greater of specified values
* `min(a,b...)` - smaller of specified values
* `cos(a)` - trigonometric cosine of an angle
* `sin(a)` - trigonometric sine of an angle
* `tan(a)` - trigonometric tangent of an angle
* `acos(a)` - arc cosine of `a`
* `asin(a)` - arc sine of `a`
* `atan(a)` - arc tangent of `a`
* `cosh(a)` - hyperbolic cosine of `a`
* `sinh(a)` - hyperbolic sine of `a`
* `tanh(a)` - hyperbolic tangent of `a`
* `atan2(a,b)` - angle theta from the conversion of rectangular coordinates x, y to polar coordinates (r, theta)
* `abs(a)` - absolute value
* `log` - logarithm
  * `log(a)` - natural logarithm (base E) of `a`
  * `log(a,b)` - base `b` logarithm of `a` (shortcut for `log(a)/log(b)`)
* `log10(a)` - base 10 logarithm of `a`
* `log1p(a)` - natural logarithm of `a+1`
* `ceil(a)` - smallest value that is greater than or equal to `a` and is equal to a mathematical integer
* `floor(a)` - largest value that is less than or equal to `a` and is equal to a mathematical integer
* `round(a)` - closest value to `a`, with ties rounding to positive infinity
* `rint(a)` - value that is closest to `a` and is equal to a mathematical integer
* `pow(a,b)` - `a` raised to the power of `b`
* `sqrt(a)` - positive square root of `a`
* `cbrt(a)` - cube root of `a`
* `hypot(a,b)` - `sqrt(a^2+b^2)` without intermediate overflow or underflow
* `exp(a)` - E raised to the power of `a`
* `expm1(a)` - E raised to the power of `a`, minus `1`
* `to_degrees(a)` - angle measured in radians to approximately equivalent angle measured in degrees
* `to_radians(a)` - angle measured in degrees to approximately equivalent angle measured in radians
* `get_exponent(a)` - unbiased exponent used in the representation of `a`
* `next_down(a)` - floating-point value adjacent to a number in the direction of negative infinity
* `next_up(a)` - floating-point value adjacent to a number in the direction of positive infinity
* `next_after(a,b)` - floating-point number adjacent to `a` in the direction of `b`
* `signum(a)` - signum function of `a`
* `ulp(a)` - size of an ulp of `a`
* `ieee_remainder(a,b)` - remainder operation on two arguments as prescribed by the IEEE 754 standard
* `copy_sign(a,b)` - `a` with the sign of `b`
* `fma(a,b,c)` - exact product of `a*b+c` rounded once
* `scalb` - `a*2^b` rounded as if performed by a single correctly rounded floating-point multiply
* `format_float(a)` - shortcut for `round(a*100)/100`
* `root(a,b)` - shortcut for `pow(a,1/b)`
* `random` - random number generator
  * `random(a)` - random number `0 >= x < a`
  * `random(a,b)` - random number `a >= x < b`
* `rng_choice(a,b...)` - random choose between multiple numbers
* `raw_hypot(a,b)` - shortcut for `a^2+b^2`
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
    <version>v3.11</version>
</dependency>
```
