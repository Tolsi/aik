package ru.tolsi.aik.geom

import kotlin.math.*

interface ILine: Figure2D {
    val from: Point
    val to: Point

    fun intersects(p: Point): Boolean
    fun intersects(l: ILine): Point?

    fun isBound(p: IPoint): Boolean {
        return (from == p) || (to == p)
    }

    val length: Double
        get() = from.distanceTo(to)

    fun distance(p: Point): Double {
        val v = to - from
        val w = p - from
        val c1 = w.dot(v)
        val c2 = v.dot(v)
        if (c1 <= 0)
            return p.distanceTo(from)
        if (c2 <= c1)
            return p.distanceTo(to)
        val b = c1 / c2
        val Pb = from + v * b
        return p.distanceTo(Pb)
    }

    fun a(): Double = from.y - to.y
    fun b(): Double = from.x - to.x
    fun c(): Double = from.x * to.y - to.x * from.y

    fun isInRectangleBetweenPoints(p: Point): Boolean {
        return (p.x >= min(from.x, to.x) && p.x <= max(from.x, to.x) &&
                p.y >= min(from.y, to.y) && p.y <= max(from.y, to.y))// || isPointTooClose(p)
    }

    fun intersectsAsSegment(p: Point): Boolean {
        return intersectsAsLine(p) && isInRectangleBetweenPoints(p)
    }

    fun intersectsAsRay(p: ILine): Point? {
        return intersects(p)?.takeIf {
            it.isDirectedTo(from.directionsTo(to))
        }
    }

    fun intersectsAsRay(p: Point): Boolean {
        return intersectsAsLine(p) && p.isDirectedTo(from.directionsTo(to))
    }

    fun intersectsAsLine(p: Point): Boolean =
        if (a() <= Geometry.EPS) {
            from.y - p.y <= Geometry.EPS
        } else if (b() <= Geometry.EPS) {
            from.x - p.x <= Geometry.EPS
        } else {
            (p.x - from.x) / (to.x - from.x) - (p.y - from.y) / (to.y - from.y) <= Geometry.EPS
        }

    fun intersectsAsLine(p: ILine): Point? {
        val d = (this.b() * p.a() - this.a() * p.b())
        val x = (this.c() * p.b() - this.b() * p.c()) / d
        val y = (this.c() * p.a() - this.a() * p.c()) / d
        return Point(x, y).takeIf { x.isFinite() && y.isFinite() }
    }

    fun intersectsAsSegment(l: ILine): Point? {
        return points.find { l.intersects(it) }
            ?: intersectsAsLine(l)?.takeIf { isInRectangleBetweenPoints(it) && l.isInRectangleBetweenPoints(it) }
    }

    fun Point.isDirectedTo(lineDirections: List<Direction>): Boolean {
        // todo не противоположные
        return when (lineDirections.size) {
            0 -> this == from && this == to
            1 -> when (lineDirections.first()) {
                Direction.UP ->
                    this.y > from.y && this.x == from.x
                Direction.DOWN ->
                    this.y < from.y && this.x == from.x
                Direction.LEFT ->
                    this.x < from.x && this.y == from.y
                Direction.RIGHT ->
                    this.x > from.x && this.y == from.y
            }
            2 -> {
                val sorted = lineDirections.sortedBy { it.ordinal }
                when (sorted.first() to sorted.last()) {
                    (Direction.UP to Direction.RIGHT) ->
                        this.x > from.x && this.y > from.y
                    (Direction.UP to Direction.LEFT) ->
                        this.x < from.x && this.y > from.y
                    (Direction.RIGHT to Direction.DOWN) ->
                        this.x > from.x && this.y < from.y
                    (Direction.DOWN to Direction.LEFT) ->
                        this.x < from.x && this.y < from.y
                    else -> false
                }
            }
            else -> false
        }
    }

    fun interpolateBy(step: Point) = sequence {
        val current: Point = from.copy()
        while (from.distanceTo(current) < length) {
            current.add(step)
            yield(current)
        }
    }

    fun times(n: Double): LineSegment {
        return LineSegment(from, Point(from.x + (to.x - from.x) * n, from.y + (to.y - from.y) * n))
    }

    fun normalize(): LineSegment {
        val normX = (to.x - from.x) / length
        val normY = (to.y - from.y) / length
        return LineSegment(Point.Zero.mutable, Point(normX, normY).mutable)
    }

    fun toLenghtOneLine(): LineSegment {
        val normX = (to.x - from.x) / length
        val normY = (to.y - from.y) / length
        return LineSegment(from, from.plus(Point(normX, normY)).mutable)
    }

    fun withLength(length: Double): LineSegment {
        return toLenghtOneLine().times(length)
    }

    override val points get() = PointArrayList(2).apply { add(from).add(to) }
    override val closed: Boolean get() = false

    fun moveTo(point: Point): Line {
        val normalized = this.toLenghtOneLine().normalize()
        return Line(point, point.plus(normalized.to).mutable)
    }

    fun toPair(): Pair<Point, Point> {
        return this.from to this.to
    }
}

open class Line(override val from: Point, override val to: Point): ILine {

    init {
        require(from != to)
    }

//    private fun isPointTooClose(p: Point): Boolean {
//        return (abs(p.x - from.x) <= Geometry.EPS &&
//                abs(p.x - to.x) <= Geometry.EPS) ||
//                (abs(p.y - from.y) <= Geometry.EPS &&
//                        abs(p.y - to.y) <= Geometry.EPS)
//    }


    override fun distance(p: Point): Double {
        return abs((to.y - from.y) * p.x - (to.x - from.x) * p.y + to.x * from.y - to.y * from.x) / sqrt((to.y - from.y).pow(2.0) + (to.x - from.x).pow(2.0))
    }

    fun points(): List<Point> = listOf(from, to)
    override fun toString(): String {
        return "Line(from=$from, to=$to)"
    }

    fun rotate(angle: Angle): Line {
        val diffX = to.x - from.x
        val diffY = to.y - from.y
        val newToPoint = Point(
            diffX * cos(angle.radians) - diffY * sin(angle.radians),
            diffX * sin(angle.radians) + diffY * cos(angle.radians))
        return Line(from, from.plus(newToPoint).mutable)
    }

    companion object {
        operator fun invoke(x1: Number, y1: Number, x2: Number, y2: Number): Line = Line(Point(x1, y1), Point(x2, y2))

        // todo limit!
        fun createFromPointAimAndSpeed(from: Point, aim: Point, speed: Double): Sequence<Point> {
            val speedPoint = aim.copy()
            speedPoint.normalize()
            // by ticks
            speedPoint.mul(speed / 60)
            return Line(from, from.copy().add(speedPoint)).times(150.0).interpolateBy(speedPoint)
        }

        val OneLenghtZeroAngle = Line(Point(0, 0), Point(1, 0))
    }

    override fun intersects(p: Point): Boolean {
        return intersectsAsLine(p)
    }

    override fun intersects(l: ILine): Point? {
        return intersectsAsLine(l)
    }

    override fun containsPoint(x: Double, y: Double) = intersects(Point(x, y))
}

fun Collection<Point>.toLine(): Line {
    require(this.size == 2)
    return Line(this.elementAt(0), this.elementAt(1))
}

fun Pair<Point, Point>.toLine(): Line {
    return Line(this.first, this.second)
}