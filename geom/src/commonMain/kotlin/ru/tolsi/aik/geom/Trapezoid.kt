package ru.tolsi.aik.geom

import ru.tolsi.aik.geom.math.almostEquals
import ru.tolsi.aik.geom.math.almostZero
import kotlin.math.abs
import kotlin.math.sqrt

interface ITrapezoid : IPolygon {
    val p0: IPoint
    val p1: IPoint
    val p2: IPoint
    val p3: IPoint
    val base1Length: Double
    val base2Length: Double
    val height: Double
    val isIsosceles: Boolean
}

open class Trapezoid(
    p0: IPoint,
    p1: IPoint,
    p2: IPoint,
    p3: IPoint,
    validate: Boolean = true
) : ITrapezoid {

    protected var _p0: Point = Point(p0.x, p0.y)
    protected var _p1: Point = Point(p1.x, p1.y)
    protected var _p2: Point = Point(p2.x, p2.y)
    protected var _p3: Point = Point(p3.x, p3.y)

    init {
        if (validate) {
            require(hasParallelSides(_p0, _p1, _p2, _p3)) {
                "Trapezoid must have at least one pair of parallel sides"
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
        get() = abs(
            (_p0.x * _p1.y - _p1.x * _p0.y) +
                    (_p1.x * _p2.y - _p2.x * _p1.y) +
                    (_p2.x * _p3.y - _p3.x * _p2.y) +
                    (_p3.x * _p0.y - _p0.x * _p3.y)
        ) / 2.0

    override fun containsPoint(x: Double, y: Double): Boolean {
        // Use ray casting algorithm (inherited from IPolygon)
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

    override val base1Length: Double
        get() = _p0.distanceTo(_p1)

    override val base2Length: Double
        get() = _p2.distanceTo(_p3)

    override val height: Double
        get() {
            val base1Line = Line(_p0, _p1)
            return base1Line.distance(_p3)
        }

    override val isIsosceles: Boolean
        get() = almostEquals(_p0.distanceTo(_p3), _p1.distanceTo(_p2))

    fun setTo(p0: IPoint, p1: IPoint, p2: IPoint, p3: IPoint): Trapezoid {
        _p0.setTo(p0.x, p0.y)
        _p1.setTo(p1.x, p1.y)
        _p2.setTo(p2.x, p2.y)
        _p3.setTo(p3.x, p3.y)
        return this
    }

    fun displaced(dx: Double, dy: Double): Trapezoid =
        Trapezoid(
            _p0 + Point(dx, dy),
            _p1 + Point(dx, dy),
            _p2 + Point(dx, dy),
            _p3 + Point(dx, dy),
            validate = false
        )

    fun displace(dx: Double, dy: Double): Trapezoid {
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
        ): Trapezoid = Trapezoid(p0, p1, p2, p3, validate)

        private fun hasParallelSides(p0: IPoint, p1: IPoint, p2: IPoint, p3: IPoint): Boolean {
            val v1 = p1 - p0
            val v2 = p2 - p3
            val v3 = p2 - p1
            val v4 = p3 - p0

            val cross1 = abs(v1.x * v2.y - v1.y * v2.x)
            val cross2 = abs(v3.x * v4.y - v3.y * v4.x)

            return almostZero(cross1) || almostZero(cross2)
        }

        fun fromBases(
            base1Start: IPoint,
            base1End: IPoint,
            base2Length: Double,
            height: Double,
            offset: Double = 0.0,
            validate: Boolean = true
        ): Trapezoid {
            val base1Vector = base1End - base1Start
            val base1Length = base1Start.distanceTo(base1End)

            // Perpendicular vector (rotate 90 degrees)
            val perpX = -base1Vector.y
            val perpY = base1Vector.x
            val perpLength = sqrt(perpX * perpX + perpY * perpY)

            // Normalize and scale by height
            val heightVector = Point(
                perpX / perpLength * height,
                perpY / perpLength * height
            )

            // Calculate base2 start with offset
            val base2Start = base1Start + heightVector + Point(
                base1Vector.x / base1Length * offset,
                base1Vector.y / base1Length * offset
            )

            val base2End = base2Start + Point(
                base1Vector.x / base1Length * base2Length,
                base1Vector.y / base1Length * base2Length
            )

            return Trapezoid(base1Start, base1End, base2End, base2Start, validate)
        }

        fun isosceles(
            base1Center: IPoint,
            base1Length: Double,
            base2Length: Double,
            height: Double,
            validate: Boolean = true
        ): Trapezoid {
            val halfBase1 = base1Length / 2.0
            val halfBase2 = base2Length / 2.0

            val p0 = base1Center + Point(-halfBase1, 0.0)
            val p1 = base1Center + Point(halfBase1, 0.0)
            val p2 = base1Center + Point(halfBase2, height)
            val p3 = base1Center + Point(-halfBase2, height)

            return Trapezoid(p0, p1, p2, p3, validate)
        }
    }

    override fun toString(): String =
        "Trapezoid(p0=${p0.toString()}, p1=${p1.toString()}, p2=${p2.toString()}, p3=${p3.toString()})"
}

//////////// INT

interface ITrapezoidInt {
    val p0: IPointInt
    val p1: IPointInt
    val p2: IPointInt
    val p3: IPointInt
    val base1Length: Int
    val base2Length: Int
    val height: Int
    val isIsosceles: Boolean
}

inline class TrapezoidInt(val trapezoid: Trapezoid) : ITrapezoidInt {
    override val p0: IPointInt get() = trapezoid.p0.int
    override val p1: IPointInt get() = trapezoid.p1.int
    override val p2: IPointInt get() = trapezoid.p2.int
    override val p3: IPointInt get() = trapezoid.p3.int
    override val base1Length: Int get() = trapezoid.base1Length.toInt()
    override val base2Length: Int get() = trapezoid.base2Length.toInt()
    override val height: Int get() = trapezoid.height.toInt()
    override val isIsosceles: Boolean get() = trapezoid.isIsosceles

    companion object {
        operator fun invoke(
            p0: IPointInt,
            p1: IPointInt,
            p2: IPointInt,
            p3: IPointInt,
            validate: Boolean = true
        ): TrapezoidInt =
            TrapezoidInt(Trapezoid(p0.float, p1.float, p2.float, p3.float, validate))

        fun isosceles(
            base1Center: IPointInt,
            base1Length: Int,
            base2Length: Int,
            height: Int,
            validate: Boolean = true
        ): TrapezoidInt =
            TrapezoidInt(
                Trapezoid.isosceles(
                    base1Center.float,
                    base1Length.toDouble(),
                    base2Length.toDouble(),
                    height.toDouble(),
                    validate
                )
            )
    }

    override fun toString(): String =
        "TrapezoidInt(p0=${p0.toString()}, p1=${p1.toString()}, p2=${p2.toString()}, p3=${p3.toString()})"
}

val ITrapezoid.int get() = TrapezoidInt(Trapezoid(p0, p1, p2, p3, validate = false))
val ITrapezoidInt.float get() = Trapezoid(p0.float, p1.float, p2.float, p3.float, validate = false)
