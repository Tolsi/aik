package ru.tolsi.aik.geom

import ru.tolsi.aik.geom.range.OpenRange
import ru.tolsi.aik.geom.internal.umod
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.atan2

inline class Angle(val radians: Double) : Comparable<Angle> {
    override fun toString(): String = "$degrees.degrees"

    @Suppress("MemberVisibilityCanBePrivate")
    companion object {
        val ZERO = Angle(0.0)

        inline fun fromRadians(radians: Number) = Angle(radians.toDouble())
        inline fun fromDegrees(degrees: Number) = Angle(degreesToRadians(degrees.toDouble()))

        internal const val PI2 = PI * 2

        internal const val DEG2RAD = PI / 180.0
        internal const val RAD2DEG = 180.0 / PI

        internal const val MAX_DEGREES = 360.0
        internal const val MAX_RADIANS = PI2

        internal const val HALF_DEGREES = MAX_DEGREES / 2.0
        internal const val HALF_RADIANS = MAX_RADIANS / 2.0

        fun cos01(ratio: Double) = kotlin.math.cos(PI2 * ratio)
        fun sin01(ratio: Double) = kotlin.math.sin(PI2 * ratio)
        fun tan01(ratio: Double) = kotlin.math.tan(PI2 * ratio)

        fun degreesToRadians(degrees: Double): Double = degrees * DEG2RAD
        fun radiansToDegrees(radians: Double): Double = radians * RAD2DEG

        fun shortDistanceTo(from: Angle, to: Angle): Angle {
            val r0 = from.radians umod MAX_RADIANS
            val r1 = to.radians umod MAX_RADIANS
            val diff = (r1 - r0 + HALF_RADIANS) % MAX_RADIANS - HALF_RADIANS
            return if (diff < -HALF_RADIANS) Angle(diff + MAX_RADIANS) else Angle(diff)
        }

        fun longDistanceTo(from: Angle, to: Angle): Angle {
            val short = shortDistanceTo(from, to)
            return when {
                short == ZERO -> ZERO
                short < ZERO -> 360.degrees + short
                else -> (-360).degrees + short
            }
        }

        fun between(x0: Double, y0: Double, x1: Double, y1: Double): Angle {
            val angle = atan2(y1 - y0, x1 - x0)
            return if (angle < 0) Angle(angle + PI2) else Angle(angle)
        }

        inline fun between(x0: Number, y0: Number, x1: Number, y1: Number): Angle = between(x0.toDouble(), y0.toDouble(), x1.toDouble(), y1.toDouble())

        fun between(p0: IPoint, p1: IPoint): Angle = between(p0.x, p0.y, p1.x, p1.y)
    }

    override fun compareTo(other: Angle): Int = this.radians.compareTo(other.radians)
}

inline fun cos(angle: Angle): Double = kotlin.math.cos(angle.radians)
inline fun sin(angle: Angle): Double = kotlin.math.sin(angle.radians)
inline fun tan(angle: Angle): Double = kotlin.math.tan(angle.radians)

val Angle.cosine get() = cos(this)
val Angle.sine get() = sin(this)
val Angle.tangent get() = tan(this)
val Angle.degrees get() = Angle.radiansToDegrees(radians)

val Angle.absoluteValue: Angle get() = Angle.fromRadians(radians.absoluteValue)
fun Angle.shortDistanceTo(other: Angle): Angle = Angle.shortDistanceTo(this, other)
fun Angle.longDistanceTo(other: Angle): Angle = Angle.longDistanceTo(this, other)

inline operator fun Angle.times(scale: Number): Angle = Angle(this.radians * scale.toDouble())
inline operator fun Angle.div(scale: Number): Angle = Angle(this.radians / scale.toDouble())
inline operator fun Angle.div(other: Angle): Double = this.radians / other.radians // Ratio
inline operator fun Angle.plus(other: Angle): Angle = Angle(this.radians + other.radians)
inline operator fun Angle.minus(other: Angle): Angle = Angle(this.radians - other.radians)
inline operator fun Angle.unaryMinus(): Angle = Angle(-radians)
inline operator fun Angle.unaryPlus(): Angle = Angle(+radians)
inline operator fun Angle.compareTo(other: Angle): Int = this.radians.compareTo(other.radians)
inline operator fun ClosedRange<Angle>.contains(angle: Angle): Boolean = angle.inBetween(this.start, this.endInclusive, inclusive = true)
inline operator fun OpenRange<Angle>.contains(angle: Angle): Boolean = angle.inBetween(this.start, this.endExclusive, inclusive = false)
infix fun Angle.until(other: Angle) = OpenRange(this, other)

inline fun Angle.inBetweenInclusive(min: Angle, max: Angle): Boolean = inBetween(min, max, inclusive = true)
inline fun Angle.inBetweenExclusive(min: Angle, max: Angle): Boolean = inBetween(min, max, inclusive = false)

inline infix fun Angle.inBetween(range: ClosedRange<Angle>): Boolean = inBetween(range.start, range.endInclusive, inclusive = true)
inline infix fun Angle.inBetween(range: OpenRange<Angle>): Boolean = inBetween(range.start, range.endExclusive, inclusive = false)

fun Angle.inBetween(min: Angle, max: Angle, inclusive: Boolean): Boolean {
    val nthis = this.normalized
    val nmin = min.normalized
    val nmax = max.normalized
    @Suppress("ConvertTwoComparisonsToRangeCheck")
    return when {
        nmin > nmax -> nthis >= nmin || (if (inclusive) nthis <= nmax else nthis < nmax)
        else -> nthis >= nmin && (if (inclusive) nthis <= nmax else nthis < nmax)
    }
}

inline val Number.degrees get() = Angle.fromDegrees(this)
inline val Number.radians get() = Angle.fromRadians(this)
val Angle.normalized get() = Angle(radians umod Angle.MAX_RADIANS)
