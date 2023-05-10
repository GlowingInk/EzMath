# EzMath

Simple math evaluator. Supports constants, functions, variables.
Evaluators code is based off Boann's answer on [StackOverflow](https://stackoverflow.com/a/26227947/9819521).

It should be noted that the lib uses Java's `double` as its base number type, so it's rather fast. 
That being said, it also means that calculations are affected by imprecision, so big numbers are a no-no. 
Also, for performance reasons, EzMath throws no exceptions, so weird behaviour on invalid inputs is to be expected. 

## Format
### Decimal numbers
`123`, `-45.6`, `7.8E9`, `10.11E-12`
### Basic math operators
`1+2`, `3-4`, `5*6`, `7/8`, `9^10`, `11%12`
### Grouping
`(1+2)`, `3^(4 - 5.67)`, `89 / ((10*11)/121.3)`
### Constants
`pi/2`, `e^3`
<details><summary>List of available constants</summary>

* `e` - Euler's number - the base of the natural logarithms
* `pi` - the ratio of the circumference of a circle to its diameter
* `tau` - the ratio of the circumference of a circle to its radius (shortcut for `pi*2`)
* `infinity` - infinite value
* `nan` - not-a-number value
* `max_value` - the largest finite value that can be used in calculations
* `min_value` - the smallest positive value that can be used in calculations
* `euler` - Euler's (Euler-Mascheroni) constant
* `phi` - the golden ratio
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
* `avg(a,b...)` - average of specified values
* `cos(a)` - trigonometric cosine of an angle
* `sin(a)` - trigonometric sine of an angle
* `tan(a)` - trigonometric tangent of an angle
* `acos(a)` - arc cosine of `a`
* `asin(a)` - arc sine of `a`
* `atan(a)` - arc tangent of `a`
* `cosh(a)` - hyperbolic cosine of `a`
* `sinh(a)` - hyperbolic sine of `a`
* `tanh(a)` - hyperbolic tangent of `a`
* `atan2(a,b)` - angle theta from the conversion of rectangular coordinates x(`b`), y(`a`) to polar coordinates (r, theta)
* `abs(a)` - absolute value
* `log` - logarithm
  * `log(a)` - natural logarithm (base E) of `a`
  * `log(a,b)` - base `b` logarithm of `a` (shortcut for `log(a)/log(b)`)
* `log10(a)` - base 10 logarithm of `a`
* `log1p(a)` - natural logarithm of `a+1`
* `ceil(a)` - smallest value that is greater than or equal to `a` and is equal to a mathematical integer
* `floor(a)` - largest value that is less than or equal to `a` and is equal to a mathematical integer
* `trunc(a)` - `a` with the fractional part removed, leaving the integer part
* `round(a)` - closest value to `a`, with ties rounding to positive infinity
* `rint(a)` - value that is closest to `a` and is equal to a mathematical integer; for `.5` values rounds to closest even number
* `format_float(a)` - round to the first two decimal places (shortcut for `trunc(a*100)/100`); very inaccurate for some numbers
* `pow(a,b)` - `a` raised to the power of `b` (same as `a^b`)
* `sqrt(a)` - positive square root of `a`
* `cbrt(a)` - cube root of `a`
* `root(a,b)` - `b` root of `a` (shortcut for `pow(a,1/b)`)
* `hypot` - hypotenuse
  * `hypot(a,b)` - hypotenuse of `a` and `b` without intermediate overflow or underflow (`sqrt(a^2+b^2)`)
  * `hypot(a,b,c...)` - hypotenuse of lengths  (shortcut for `sqrt(a^2+b^2+c^2...)`)
* `raw_hypot(a,b...)` - hypotenuse of lengths with no square root operation (shortcut for `a^2+b^2...`)
* `exp(a)` - E raised to the power of `a`
* `expm1(a)` - E raised to the power of `a`, minus `1`
* `to_degrees(a)` - angle measured in radians to approximately equivalent angle measured in degrees
* `to_radians(a)` - angle measured in degrees to approximately equivalent angle measured in radians
* `get_exponent(a)` - unbiased exponent used in the representation of `a`
* `next_down(a)` - floating-point value adjacent to `a` in the direction of negative infinity
* `next_up(a)` - floating-point value adjacent to `a` in the direction of positive infinity
* `next_after(a,b)` - floating-point number adjacent to `a` in the direction of `b`
* `signum(a)` - signum function of `a`
* `ulp(a)` - size of an ulp of `a`
* `ieee_remainder(a,b)` - remainder operation `%` on two arguments as prescribed by the IEEE 754 standard
* `copy_sign(a,b)` - `a` with the sign of `b`
* `fma(a,b,c)` - exact product of `a*b+c` rounded once
* `scalb(a,b)` - `a*2^round(b)` rounded as if performed by a single correctly rounded floating-point multiply
* `random` - random number generator
  * `random(a)` - random number `0 <= x < a` (or `a < x <= 0` for negative `a`)
  * `random(a,b)` - random number `a <= x < b`
* `rng_choice(a,b...)` - random choose between multiple numbers
* `log_gamma(a)` - logarithm of gamma function of `a`
* `gamma(a)` - gamma function of `a` (shortcut for `exp(log_gamma(a))`)
</details>

## Get it ![Latest Version](https://img.shields.io/github/v/tag/GlowingInk/EzMath?sort=semver&style=flat&label=release)
Versions in dependency sections may be outdated. Check the badge above for the latest one.
### Maven
Add to repositories
```xml
<repository>
    <id>glowing-ink</id>
    <url>https://repo.glowing.ink/releases</url>
</repository>
```
Or for latest snapshots
```xml
<repository>
    <id>glowing-ink</id>
    <url>http://repo.glowing.ink/snapshots</url>
</repository>
```
Add to dependencies
```xml
<dependency>
    <groupId>ink.glowing</groupId>
    <artifactId>ezmath</artifactId>
    <version>3.21</version>
</dependency>
```
### Gradle
```kotlin
repositories {
    maven {
        url = uri("https://repo.glowing.ink/releases")
        // url = uri("https://repo.glowing.ink/snapshots")
    }
}

dependencies {
    implementation("ink.glowing:ezmath:3.21")
}
```
