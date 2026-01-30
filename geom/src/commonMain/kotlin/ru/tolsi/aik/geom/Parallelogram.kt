package ru.tolsi.aik.geom

import ru.tolsi.aik.geom.internal.niceStr
import ru.tolsi.aik.geom.math.almostZero
import kotlin.math.abs

interface IParallelogram : IPolygon {
    val p0: IPoint
    val p1: IPoint
    val p2: IPoint
    val p3: IPoint
    val edge1: IPoint
    val edge2: IPoint
}

open class Parallelogram(
    p0: IPoint,
    edge1: IPoint,
    edge2: IPoint,
    validate: Boolean = true
) : IParallelogram {

    protected var _p0: Point = Point(p0.x, p0.y)
    protected var _edge1: Point = Point(edge1.x, edge1.y)
    protected var _edge2: Point = Point(edge2.x, edge2.y)

    init {
        if (validate) {
            val cross = edge1.x * edge2.y - edge1.y * edge2.x
            require(!almostZero(abs(cross))) {
                "Edges cannot be parallel (cross product is zero)"
            }
        }
    }

    override val p0: IPoint get() = _p0
    override val p1: IPoint get() = _p0 + _edge1
    override val p2: IPoint get() = _p0 + _edge1 + _edge2
    override val p3: IPoint get() = _p0 + _edge2

    override val edge1: IPoint get() = _edge1
    override val edge2: IPoint get() = _edge2

    override val points: IPointArrayList
        get() = PointArrayList(4).apply {
            // Counter-clockwise order
            add(p0)
            add(p3)
            add(p2)
            add(p1)
        }

    override val area: Double
        get() = abs(_edge1.x * _edge2.y - _edge1.y * _edge2.x)

    override val closed: Boolean = true

    override fun containsPoint(x: Double, y: Double): Boolean {
        val p = Point(x, y)
        val v = p - _p0
        val denom = _edge1.x * _edge2.y - _edge1.y * _edge2.x
        val u = (v.x * _edge2.y - v.y * _edge2.x) / denom
        val w = (_edge1.x * v.y - _edge1.y * v.x) / denom
        return u >= 0 && u <= 1 && w >= 0 && w <= 1
    }

    val center: IPoint
        get() = _p0 + (_edge1 + _edge2) * 0.5

    fun setTo(p0: IPoint, edge1: IPoint, edge2: IPoint): Parallelogram {
        this._p0.setTo(p0.x, p0.y)
        this._edge1.setTo(edge1.x, edge1.y)
        this._edge2.setTo(edge2.x, edge2.y)
        return this
    }

    fun displaced(dx: Double, dy: Double): Parallelogram =
        Parallelogram(_p0 + Point(dx, dy), _edge1, _edge2, validate = false)

    fun displace(dx: Double, dy: Double): Parallelogram {
        _p0.setTo(_p0.x + dx, _p0.y + dy)
        return this
    }

    companion object {
        operator fun invoke(
            p0: IPoint,
            edge1: IPoint,
            edge2: IPoint,
            validate: Boolean = true
        ): Parallelogram = Parallelogram(p0, edge1, edge2, validate)

        fun fromThreePoints(p0: IPoint, p1: IPoint, p3: IPoint, validate: Boolean = true): Parallelogram =
            Parallelogram(p0, p1 - p0, p3 - p0, validate)

        fun fromFourPoints(p0: IPoint, p1: IPoint, p2: IPoint, p3: IPoint, validate: Boolean = true): Parallelogram {
            val edge1 = p1 - p0
            val edge2 = p3 - p0
            if (validate) {
                val expectedP2 = p0 + edge1 + edge2
                require(almostZero(abs(p2.x - expectedP2.x)) && almostZero(abs(p2.y - expectedP2.y))) {
                    "Points do not form a valid parallelogram"
                }
            }
            return Parallelogram(p0, edge1, edge2, validate)
        }
    }

    override fun toString(): String =
        "Parallelogram(p0=${p0.toString()}, p1=${p1.toString()}, p2=${p2.toString()}, p3=${p3.toString()})"
}

//////////// INT

interface IParallelogramInt {
    val p0: IPointInt
    val p1: IPointInt
    val p2: IPointInt
    val p3: IPointInt
    val edge1: IPointInt
    val edge2: IPointInt
}

inline class ParallelogramInt(val parallelogram: Parallelogram) : IParallelogramInt {
    override val p0: IPointInt get() = parallelogram.p0.int
    override val p1: IPointInt get() = parallelogram.p1.int
    override val p2: IPointInt get() = parallelogram.p2.int
    override val p3: IPointInt get() = parallelogram.p3.int
    override val edge1: IPointInt get() = parallelogram.edge1.int
    override val edge2: IPointInt get() = parallelogram.edge2.int

    companion object {
        operator fun invoke(
            p0: IPointInt,
            edge1: IPointInt,
            edge2: IPointInt,
            validate: Boolean = true
        ): ParallelogramInt =
            ParallelogramInt(Parallelogram(p0.float, edge1.float, edge2.float, validate))
    }

    override fun toString(): String =
        "ParallelogramInt(p0=${p0.toString()}, p1=${p1.toString()}, p2=${p2.toString()}, p3=${p3.toString()})"
}

val IParallelogram.int get() = ParallelogramInt(Parallelogram(p0, edge1, edge2, validate = false))
val IParallelogramInt.float get() = Parallelogram(p0.float, edge1.float, edge2.float, validate = false)
