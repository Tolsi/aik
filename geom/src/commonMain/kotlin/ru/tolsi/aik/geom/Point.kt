@file:Suppress("NOTHING_TO_INLINE")

package ru.tolsi.aik.geom

import ru.tolsi.aik.geom.internal.niceStr
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.roundToInt

interface IPoint: Comparable<IPoint> {
    val x: Double
    val y: Double

    override fun compareTo(other: IPoint): Int = Point.compareEps(this.x, this.y, other.x, other.y)

    companion object {
        operator fun invoke(): IPoint = Point(0.0, 0.0)
        inline operator fun invoke(x: Number, y: Number): IPoint = Point(x.toDouble(), y.toDouble())
    }
}

data class Point(override var x: Double, override var y: Double): IPoint {

    companion object {
        val Zero: IPoint = Point(0.0, 0.0)
        val One: IPoint = Point(1.0, 1.0)

        val Up: IPoint = Point(0.0, +1.0)
        val Down: IPoint = Point(0.0, -1.0)
        val Left: IPoint = Point(-1.0, 0.0)
        val Right: IPoint = Point(+1.0, 0.0)

        //inline operator fun invoke(): Point = Point(0.0, 0.0) // @TODO: // e: java.lang.NullPointerException at org.jetbrains.kotlin.com.google.gwt.dev.js.JsAstMapper.mapFunction(JsAstMapper.java:562) (val pt = Array(1) { Point() })
        operator fun invoke(): Point = Point(0.0, 0.0)

        operator fun invoke(v: IPoint): Point = Point(v.x, v.y)
        inline operator fun invoke(x: Number, y: Number): Point = Point(x.toDouble(), y.toDouble())
        inline operator fun invoke(xy: Number): Point = Point(xy.toDouble(), xy.toDouble())

        fun middle(a: IPoint, b: IPoint): Point = Point((a.x + b.x) * 0.5, (a.y + b.y) * 0.5)

        fun angleBetweenVectors(a: IPoint, b: IPoint): Angle =
            Angle.fromRadians(acos((a.dot(b)) / (a.length * b.length)))

        fun angleBetweenPoints(ax: Double, ay: Double, bx: Double, by: Double): Angle = Angle.between(ax, ay, bx, by)
        //acos(((ax * bx) + (ay * by)) / (hypot(ax, ay) * hypot(bx, by)))

        fun compare(lx: Double, ly: Double, rx: Double, ry: Double): Int {
            val ret = ly.compareTo(ry)
            return if (ret == 0) lx.compareTo(rx) else ret
        }

        fun compareEps(lx: Double, ly: Double, rx: Double, ry: Double): Int {
            val ret = if (abs(ly - ry) <= Geometry.EPS) 0 else ly.compareTo(ry)
            return if (ret == 0) if (abs(lx - rx) <= Geometry.EPS) 0 else lx.compareTo(rx) else ret
        }

        fun compare(l: IPoint, r: IPoint): Int = compare(l.x, l.y, r.x, r.y)
        fun compareEps(l: IPoint, r: IPoint): Int = compareEps(l.x, l.y, r.x, r.y)

        fun angleBetweenPoints(x1: Double, y1: Double, x2: Double, y2: Double, x3: Double, y3: Double): Angle =
            Angle.between(x1 - x2, y1 - y2, x1 - x3, y1 - y3)

        fun distance(a: Double, b: Double): Double = abs(a - b)
        fun distance(x1: Double, y1: Double, x2: Double, y2: Double): Double = hypot(x1 - x2, y1 - y2)
        inline fun distance(x1: Number, y1: Number, x2: Number, y2: Number): Double = distance(x1.toDouble(), y1.toDouble(), x2.toDouble(), y2.toDouble())

        fun distance(a: IPoint, b: IPoint): Double = distance(a.x, a.y, b.x, b.y)
        fun distance(a: IPointInt, b: IPointInt): Double = distance(a.x, a.y, b.x, b.y)

        fun steps(a: IPoint, b: IPoint): Int = abs(a.x.roundToInt() - b.x.roundToInt()) + abs(a.y.roundToInt() - b.y.roundToInt())

        fun stepsWithDiagonals(a: IPoint, b: IPoint): Int = max(abs(a.x.roundToInt() - b.x.roundToInt()), abs(a.y.roundToInt() - b.y.roundToInt()))

        //val ax = x1 - x2
        //val ay = y1 - y2
        //val al = hypot(ax, ay)
        //val bx = x1 - x3
        //val by = y1 - y3
        //val bl = hypot(bx, by)
        //return acos((ax * bx + ay * by) / (al * bl))
    }

    fun setTo(x: Double, y: Double): Point {
        this.x = x
        this.y = y
        return this
    }

    fun setToZero() = setTo(0.0, 0.0)
    fun neg() = setTo(-x, -y)
    fun mul(s: Double) = setTo(x * s, y * s)
    fun add(p: IPoint) = this.setToAdd(this, p)
    fun sub(p: IPoint) = this.setToSub(this, p)

    fun copyFrom(that: IPoint) = setTo(that.x, that.y)

    fun setToAdd(a: IPoint, b: IPoint): Point = setTo(a.x + b.x, a.y + b.y)
    fun setToSub(a: IPoint, b: IPoint): Point = setTo(a.x - b.x, a.y - b.y)

    fun setToMul(a: IPoint, b: IPoint): Point = setTo(a.x * b.x, a.y * b.y)
    fun setToMul(a: IPoint, s: Double): Point = setTo(a.x * s, a.y * s)
    inline fun setToMul(a: IPoint, s: Number): Point = setToMul(a, s.toDouble())

    fun setToDiv(a: IPoint, b: IPoint): Point = setTo(a.x / b.x, a.y / b.y)
    fun setToDiv(a: IPoint, s: Double): Point = setTo(a.x / s, a.y / s)
    inline fun setToDiv(a: IPoint, s: Number): Point = setToDiv(a, s.toDouble())

    operator fun plusAssign(that: IPoint) {
        setTo(this.x + that.x, this.y + that.y)
    }

    fun normalize(): Point {
        val len = this.length
        return this.setTo(this.x / len, this.y / len)
    }

    override fun toString(): String = "(${x.niceStr}, ${y.niceStr})"

}

class PointEPSKey(val p: Point) {
    override fun hashCode(): Int {
        var result = (p.x / Geometry.EPS).toInt().hashCode()
        result = 31 * result + (p.y / Geometry.EPS).toInt().hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PointEPSKey

        return Point.compareEps(p, other.p) == 0
    }
}

inline fun Point.mul(s: Number) = mul(s.toDouble())

val Point.unit: IPoint get() = this / length

inline fun Point.setTo(x: Number, y: Number): Point = setTo(x.toDouble(), y.toDouble())

// @TODO: mul instead of dot
operator fun IPoint.plus(that: IPoint): IPoint = IPoint(x + that.x, y + that.y)

operator fun IPoint.minus(that: IPoint): IPoint = IPoint(x - that.x, y - that.y)
operator fun IPoint.times(that: IPoint): IPoint = IPoint(x * that.x, y * that.y)
operator fun IPoint.div(that: IPoint): IPoint = IPoint(x / that.x, y / that.y)

inline operator fun IPoint.times(scale: Number): IPoint = IPoint(x * scale.toDouble(), y * scale.toDouble())
inline operator fun IPoint.div(scale: Number): IPoint = IPoint(x / scale.toDouble(), y / scale.toDouble())

infix fun IPoint.dot(that: IPoint): Double = this.x * that.x + this.y * that.y
inline fun IPoint.distanceTo(x: Number, y: Number): Double = hypot(x.toDouble() - this.x, y.toDouble() - this.y)
fun IPoint.distanceTo(that: IPoint): Double = distanceTo(that.x, that.y)

fun IPoint.angleTo(other: IPoint): Angle = Angle.between(this.x, this.y, other.x, other.y)

operator fun IPoint.get(index: Int) = when (index) {
    0 -> x; 1 -> y
    else -> throw IndexOutOfBoundsException("IPoint doesn't have $index component")
}

val IPoint.unit: IPoint get() = this / this.length
val IPoint.length: Double get() = hypot(x, y)
val IPoint.magnitude: Double get() = hypot(x, y)
val IPoint.normalized: IPoint
    get() {
        val imag = 1.0 / magnitude
        return IPoint(x * imag, y * imag)
    }

val IPoint.mutable: Point get() = Point(x, y)
val IPoint.immutable: IPoint get() = IPoint(x, y)
fun IPoint.copy() = IPoint(x, y)
fun IPoint.steps(p: IPoint): Int = Point.steps(this, p)

interface IPointInt {
    val x: Int
    val y: Int

    companion object {
        operator fun invoke(x: Int, y: Int): IPointInt = PointInt(x, y)
    }
}

// todo make int point without double point?
inline class PointInt(val p: Point) : IPointInt, Comparable<IPointInt> {
    override fun compareTo(other: IPointInt): Int = compare(this.x, this.y, other.x, other.y)

    companion object {
        operator fun invoke(): PointInt = PointInt(0, 0)
        operator fun invoke(x: Int, y: Int): PointInt = PointInt(Point(x, y))

        fun compare(lx: Int, ly: Int, rx: Int, ry: Int): Int {
            val ret = ly.compareTo(ry)
            return if (ret == 0) lx.compareTo(rx) else ret
        }
    }

    override var x: Int
        set(value) = run { p.x = value.toDouble() }
        get() = p.x.toInt()
    override var y: Int
        set(value) = run { p.y = value.toDouble() }
        get() = p.y.toInt()

    fun setTo(x: Int, y: Int) = this.apply { this.x = x; this.y = y }
    fun setTo(that: IPointInt) = this.setTo(that.x, that.y)
    override fun toString(): String = "($x, $y)"
}

operator fun IPointInt.plus(that: IPointInt) = PointInt(this.x + that.x, this.y + that.y)
operator fun IPointInt.minus(that: IPointInt) = PointInt(this.x - that.x, this.y - that.y)
operator fun IPointInt.times(that: IPointInt) = PointInt(this.x * that.x, this.y * that.y)
operator fun IPointInt.div(that: IPointInt) = PointInt(this.x / that.x, this.y / that.y)
operator fun IPointInt.rem(that: IPointInt) = PointInt(this.x % that.x, this.y % that.y)

fun Point.asInt(): PointInt = PointInt(this)
fun PointInt.asDouble(): Point = this.p

val IPoint.int get() = PointInt(x.toInt(), y.toInt())
val IPointInt.float get() = IPoint(x.toDouble(), y.toDouble())

fun Iterable<IPoint>.getPolylineLength(): Double {
    var out = 0.0
    var prev: IPoint? = null
    for (cur in this) {
        if (prev != null) out += prev.distanceTo(cur)
        prev = cur
    }
    return out
}

val IPoint.up get(): IPoint = Point(x, this.y + 1)
val IPoint.right get(): IPoint = Point(this.x + 1, y)
val IPoint.down get(): IPoint = Point(x, this.y - 1)
val IPoint.left get(): IPoint = Point(this.x - 1, y)
val IPoint.neighbours get(): List<IPoint> = listOf(up,  down, right, left)

fun List<IPoint>.containsPoint(x: Double, y: Double): Boolean {
    var intersections = 0
    for (n in 0 until this.size - 1) {
        val p1 = this[n + 0]
        val p2 = this[n + 1]
        intersections += HorizontalLine.intersectionsWithLine(x, y, p1.x, p1.y, p2.x, p2.y)
    }
    return (intersections % 2) != 0
}


fun IPoint.projectTo(l: Line): Point {
    // get dot product of e1, e2
    val e1 = Point(l.to.x - l.from.x, l.to.y - l.from.y)
    val e2 = Point(x - l.from.x, y - l.from.y)
    val valDp = e1.dot(e2)
    // get squared length of e1
    val len = e1.dot(e1)
    return Point(
        l.from.x + valDp * e1.x / len,
        l.from.y + valDp * e1.y / len
    )
}

fun IPoint.toZeroAngleLine(): Line {
    return Line(this, this.right)
}

fun List<IPoint>.epsUnique(): List<IPoint> {
    return this.fold(listOf()) { res, p ->
        if (!res.any { it.compareTo(p) == 0 }) {
            res.plus(p)
        } else res
    }
}

fun List<IPoint>.epsRemove(remove: IPoint): List<IPoint> {
    return this.fold(mutableListOf()) { res, p ->
        if (p.compareTo(remove) != 0) {
            res.add(p)
            res
        } else {
            res
        }
    }
}

fun List<IPoint>.epsRemove(remove: List<IPoint>): List<IPoint> {
    return this.fold(mutableListOf()) { res, p ->
        if (remove.all { p.compareTo(it) != 0 }) {
            res.add(p)
            res
        } else {
            res
        }
    }
}

fun List<IPoint>.epsContains(p: IPoint): Boolean {
    return this.any { it.compareTo(p) == 0 }
}

fun IPoint.toRectangleWithCenterInPoint(radius: Double): IRectangle {
    return Rectangle(this.x - radius, this.y - radius, radius * 2, radius * 2)
}