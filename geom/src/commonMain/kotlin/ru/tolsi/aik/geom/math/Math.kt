package ru.tolsi.aik.geom.math

import ru.tolsi.aik.geom.Geometry
import kotlin.math.*

fun Long.clamp(min: Long, max: Long): Long = if (this < min) min else if (this > max) max else this
fun Int.clamp(min: Int, max: Int): Int = if (this < min) min else if (this > max) max else this
fun Double.clamp(min: Double, max: Double): Double = if (this < min) min else if (this > max) max else this
fun Float.clamp(min: Float, max: Float): Float = if (this < min) min else if (this > max) max else this
fun Double.betweenInclusive(min: Double, max: Double): Boolean = (this >= min) && (this <= max)

fun almostEquals(a: Float, b: Float) = almostZero(a - b)
fun almostZero(a: Float) = abs(a) <= Geometry.EPS

fun almostEquals(a: Double, b: Double) = almostZero(a - b)
fun almostZero(a: Double) = abs(a) <= Geometry.EPS

fun Double.roundDecimalPlaces(places: Int): Double {
    val placesFactor: Double = 10.0.pow(places.toDouble())
    return round(this * placesFactor) / placesFactor
}

fun isEquivalent(a: Double, b: Double, epsilon: Double = Geometry.EPS): Boolean = (a - epsilon < b) && (a + epsilon > b)

fun Double.smoothstep(edge0: Double, edge1: Double): Double {
    val v = (this - edge0) / (edge1 - edge0)
    val step2 = v.clamp(0.0, 1.0)
    return step2 * step2 * (3 - 2 * step2)
}

fun Double.convertRange(minSrc: Double, maxSrc: Double, minDst: Double, maxDst: Double): Double = (((this - minSrc) / (maxSrc - minSrc)) * (maxDst - minDst)) + minDst

fun log(v: Int, base: Int): Int = log(v.toDouble(), base.toDouble()).toInt()
fun ln(v: Int): Int = ln(v.toDouble()).toInt()
fun log2(v: Int): Int = log(v.toDouble(), 2.0).toInt()
fun log10(v: Int): Int = log(v.toDouble(), 10.0).toInt()

fun signNonZeroM1(x: Double): Int = if (x <= 0) -1 else +1
fun signNonZeroP1(x: Double): Int = if (x >= 0) +1 else -1

fun Double.isAlmostZero(): Boolean = kotlin.math.abs(this) <= 1e-19
fun Double.isNanOrInfinite() = this.isNaN() || this.isInfinite()

fun Float.isAlmostZero(): Boolean = kotlin.math.abs(this) <= 1e-19
fun Float.isNanOrInfinite() = this.isNaN() || this.isInfinite()

fun Int.nextMultipleOf(multiple: Int) = if (this.isMultipleOf(multiple)) this else (((this / multiple) + 1) * multiple)
fun Long.nextMultipleOf(multiple: Long) = if (this.isMultipleOf(multiple)) this else (((this / multiple) + 1) * multiple)

fun Int.prevMultipleOf(multiple: Int) = if (this.isMultipleOf(multiple)) this else nextMultipleOf(multiple) - multiple
fun Long.prevMultipleOf(multiple: Long) = if (this.isMultipleOf(multiple)) this else nextMultipleOf(multiple) - multiple

fun Int.isMultipleOf(multiple: Int) = multiple == 0 || (this % multiple) == 0
fun Long.isMultipleOf(multiple: Long) = multiple == 0L || (this % multiple) == 0L