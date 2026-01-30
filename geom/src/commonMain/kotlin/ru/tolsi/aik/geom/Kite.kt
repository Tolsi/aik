package ru.tolsi.aik.geom

import ru.tolsi.aik.geom.math.almostEquals
import kotlin.math.sqrt

interface IKite : IPolygon {
    val p0: IPoint
    val p1: IPoint
    val p2: IPoint
    val p3: IPoint
    val axis: IPoint
    val diagonal1: Double
    val diagonal2: Double
}

open class Kite(
    p0: IPoint,
    p1: IPoint,
    p2: IPoint,
    p3: IPoint,
    validate: Boolean = true
) : IKite {

    protected var _p0: Point = Point(p0.x, p0.y)
    protected var _p1: Point = Point(p1.x, p1.y)
    protected var _p2: Point = Point(p2.x, p2.y)
    protected var _p3: Point = Point(p3.x, p3.y)

    init {
        if (validate) {
            require(isValidKite(_p0, _p1, _p2, _p3)) {
                "Kite must have two pairs of adjacent equal sides"
            }
        }
    }

    override val p0: IPoint get() = _p0
    override val p1: IPoint get() = _p1
    override val p2: IPoint get() = _p2
    override val p3: IPoint get() = _p3

    override val points: IPointArrayList
        get() = PointArrayList(4).apply {
            add(_p0)
            add(_p1)
            add(_p2)
            add(_p3)
        }

    override val closed: Boolean = true

    override val area: Double
        get() = (diagonal1 * diagonal2) / 2.0

    override fun containsPoint(x: Double, y: Double): Boolean {
        // Use ray casting algorithm
        var inside = false
        val p = Point(x, y)
        var j = 3

        for (i in 0 until 4) {
            val pi = when (i) {
                0 -> _p0
                1 -> _p1
                2 -> _p2
                else -> _p3
            }
            val pj = when (j) {
                0 -> _p0
                1 -> _p1
                2 -> _p2
                else -> _p3
            }

            if ((pi.y > p.y) != (pj.y > p.y) &&
                (p.x < (pj.x - pi.x) * (p.y - pi.y) / (pj.y - pi.y) + pi.x)
            ) {
                inside = !inside
            }
            j = i
        }
        return inside
    }

    override val axis: IPoint
        get() = _p2 - _p0

    override val diagonal1: Double
        get() = _p0.distanceTo(_p2)

    override val diagonal2: Double
        get() = _p1.distanceTo(_p3)

    val isConvex: Boolean
        get() {
            // Check if all cross products have the same sign
            val cross1 = (_p1.x - _p0.x) * (_p2.y - _p1.y) - (_p1.y - _p0.y) * (_p2.x - _p1.x)
            val cross2 = (_p2.x - _p1.x) * (_p3.y - _p2.y) - (_p2.y - _p1.y) * (_p3.x - _p2.x)
            val cross3 = (_p3.x - _p2.x) * (_p0.y - _p3.y) - (_p3.y - _p2.y) * (_p0.x - _p3.x)
            val cross4 = (_p0.x - _p3.x) * (_p1.y - _p0.y) - (_p0.y - _p3.y) * (_p1.x - _p0.x)

            val allPositive = cross1 > 0 && cross2 > 0 && cross3 > 0 && cross4 > 0
            val allNegative = cross1 < 0 && cross2 < 0 && cross3 < 0 && cross4 < 0

            return allPositive || allNegative
        }

    fun setTo(p0: IPoint, p1: IPoint, p2: IPoint, p3: IPoint): Kite {
        _p0.setTo(p0.x, p0.y)
        _p1.setTo(p1.x, p1.y)
        _p2.setTo(p2.x, p2.y)
        _p3.setTo(p3.x, p3.y)
        return this
    }

    fun displaced(dx: Double, dy: Double): Kite =
        Kite(
            _p0 + Point(dx, dy),
            _p1 + Point(dx, dy),
            _p2 + Point(dx, dy),
            _p3 + Point(dx, dy),
            validate = false
        )

    fun displace(dx: Double, dy: Double): Kite {
        _p0.setTo(_p0.x + dx, _p0.y + dy)
        _p1.setTo(_p1.x + dx, _p1.y + dy)
        _p2.setTo(_p2.x + dx, _p2.y + dy)
        _p3.setTo(_p3.x + dx, _p3.y + dy)
        return this
    }

    companion object {
        operator fun invoke(
            p0: IPoint,
            p1: IPoint,
            p2: IPoint,
            p3: IPoint,
            validate: Boolean = true
        ): Kite = Kite(p0, p1, p2, p3, validate)

        private fun isValidKite(p0: IPoint, p1: IPoint, p2: IPoint, p3: IPoint): Boolean {
            val s01 = p0.distanceTo(p1)
            val s12 = p1.distanceTo(p2)
            val s23 = p2.distanceTo(p3)
            val s30 = p3.distanceTo(p0)

            val pair1 = almostEquals(s01, s12) && almostEquals(s23, s30)
            val pair2 = almostEquals(s12, s23) && almostEquals(s30, s01)

            return pair1 || pair2
        }

        fun fromAxisAndSides(
            apex1: IPoint,
            apex2: IPoint,
            side1Length: Double,
            side2Length: Double,
            validate: Boolean = true
        ): Kite {
            // Calculate the midpoint of the axis
            val axis = apex2 - apex1
            val axisLength = apex1.distanceTo(apex2)

            // Find the perpendicular bisector of the axis
            val perpX = -axis.y / axisLength
            val perpY = axis.x / axisLength

            // Calculate the distance from the axis using the Pythagorean theorem
            val d1 = sqrt(side1Length * side1Length - (axisLength / 2.0) * (axisLength / 2.0))
            val d2 = sqrt(side2Length * side2Length - (axisLength / 2.0) * (axisLength / 2.0))

            // Calculate midpoint
            val midpoint = Point((apex1.x + apex2.x) / 2.0, (apex1.y + apex2.y) / 2.0)

            // Calculate the other two vertices
            val p1 = midpoint + Point(perpX * d1, perpY * d1)
            val p3 = midpoint + Point(-perpX * d2, -perpY * d2)

            return Kite(apex1, p1, apex2, p3, validate)
        }

        fun fromDiagonals(
            center: IPoint,
            d1: Double,
            d2: Double,
            offset: Double = 0.0,
            validate: Boolean = true
        ): Kite {
            val halfD1 = d1 / 2.0
            val halfD2 = d2 / 2.0

            val p0 = center + Point(-halfD1, 0.0)
            val p1 = center + Point(offset, halfD2)
            val p2 = center + Point(halfD1, 0.0)
            val p3 = center + Point(offset, -halfD2)

            return Kite(p0, p1, p2, p3, validate)
        }
    }

    override fun toString(): String =
        "Kite(p0=${p0.toString()}, p1=${p1.toString()}, p2=${p2.toString()}, p3=${p3.toString()})"
}

//////////// INT

interface IKiteInt {
    val p0: IPointInt
    val p1: IPointInt
    val p2: IPointInt
    val p3: IPointInt
    val diagonal1: Int
    val diagonal2: Int
}

inline class KiteInt(val kite: Kite) : IKiteInt {
    override val p0: IPointInt get() = kite.p0.int
    override val p1: IPointInt get() = kite.p1.int
    override val p2: IPointInt get() = kite.p2.int
    override val p3: IPointInt get() = kite.p3.int
    override val diagonal1: Int get() = kite.diagonal1.toInt()
    override val diagonal2: Int get() = kite.diagonal2.toInt()

    companion object {
        operator fun invoke(
            p0: IPointInt,
            p1: IPointInt,
            p2: IPointInt,
            p3: IPointInt,
            validate: Boolean = true
        ): KiteInt =
            KiteInt(Kite(p0.float, p1.float, p2.float, p3.float, validate))

        fun fromDiagonals(
            center: IPointInt,
            d1: Int,
            d2: Int,
            offset: Int = 0,
            validate: Boolean = true
        ): KiteInt =
            KiteInt(
                Kite.fromDiagonals(
                    center.float,
                    d1.toDouble(),
                    d2.toDouble(),
                    offset.toDouble(),
                    validate
                )
            )
    }

    override fun toString(): String =
        "KiteInt(p0=${p0.toString()}, p1=${p1.toString()}, p2=${p2.toString()}, p3=${p3.toString()})"
}

val IKite.int get() = KiteInt(Kite(p0, p1, p2, p3, validate = false))
val IKiteInt.float get() = Kite(p0.float, p1.float, p2.float, p3.float, validate = false)
