package ru.tolsi.aik.geom

import kotlin.math.*

data class Line(val from: Point, val to: Point) {
    val length: Double by lazy { from.distanceTo(to) }

    init {
        require(from != to)
    }

    fun interpolateBy(step: Point) = sequence {
        val current: Point = from.copy()
        while (from.distanceTo(current) < length) {
            current.add(step)
            yield(current)
        }
    }

    fun a(): Double = from.y - to.y
    fun b(): Double = from.x - to.x
    fun c(): Double = from.x * to.y - to.x * from.y

    // todo is it works?
    fun intersects(p: Point): Boolean {
        return infiniteIntersects(p) && isInRectangleBetweenPoints(p)
    }

    fun intersectsInfiniteDirected(p: Point): Boolean {
        val checkF = {
            val lineDirections = from.directionTo(to)
            when (lineDirections.size) {
                0 -> p == from && p == to
                1 -> when (lineDirections.first()) {
                    Direction.UP ->
                        p.y >= from.y
                    Direction.DOWN ->
                        p.y <= from.y
                    Direction.LEFT ->
                        p.x <= from.x
                    Direction.RIGHT ->
                        p.x >= from.x
                }
                2 -> {
                    val sorted = lineDirections.sortedBy { it.ordinal }
                    when (sorted.first() to sorted.last()) {
                        (Direction.UP to Direction.RIGHT) ->
                            p.x >= from.x && p.y >= from.y
                        (Direction.UP to Direction.LEFT) ->
                            p.x <= from.x && p.y >= from.y
                        (Direction.RIGHT to Direction.DOWN) ->
                            p.x >= from.x && p.y <= from.y
                        (Direction.DOWN to Direction.LEFT) ->
                            p.x <= from.x && p.y <= from.y
                        else -> false
                    }
                }
                else -> false
            }
        }
        return infiniteIntersects(p) && checkF()
    }

    fun intersectsInfiniteDirected(p: Line): Point? {
        return infiniteIntersects(p)?.takeIf { intersectsInfiniteDirected(it) }
    }

    private fun isInRectangleBetweenPoints(p: Point): Boolean {
        return p.x >= min(from.x, to.x) && p.x <= max(from.x, to.x) &&
                p.y >= min(from.y, to.y) && p.y <= max(from.y, to.y)
    }

    fun infiniteIntersects(p: Point): Boolean =
            if (a() == 0.0) {
                from.y == p.y
            } else if (b() == 0.0) {
                from.x == p.x
            } else {
                (p.x - from.x) / (to.x - from.x) - (p.y - from.y) / (to.y - from.y) <= Geometry.EPS
            }

    fun infiniteIntersects(p: Line): Point? {
        val d = (this.b() * p.a() - this.a() * p.b())
        val x = (this.c() * p.b() - this.b() * p.c()) / d
        val y = (this.c() * p.a() - this.a() * p.c()) / d
        return Point(x, y).takeIf { x.isFinite() && y.isFinite() }
    }

    fun intersects(p: Line): Point? {
        return infiniteIntersects(p)?.takeIf { isInRectangleBetweenPoints(it) }
    }

    fun toLenghtOneLine(): Line {
        val normX = (to.x - from.x) / length
        val normY = (to.y - from.y) / length
        return Line(from, from.plus(Point(normX, normY)).mutable)
    }

    fun normalize(): Line {
        val normX = (to.x - from.x) / length
        val normY = (to.y - from.y) / length
        return Line(Point.Zero.mutable, Point(normX, normY).mutable)
    }

    fun withLength(length: Double): Line {
        return toLenghtOneLine().times(length)
    }

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

    fun infiniteDistance(p: Point): Double {
        return abs((to.y - from.y) * p.x - (to.x - from.x) * p.y + to.x * from.y - to.y * from.x) / sqrt((to.y - from.y).pow(2.0) + (to.x - from.x).pow(2.0))
    }

    fun times(n: Double): Line {
        return Line(from, Point(from.x + (to.x - from.x) * n, from.y + (to.y - from.y) * n))
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
        // todo limit!
        fun createFromPointAimAndSpeed(from: Point, aim: Point, speed: Double): Sequence<Point> {
            val speedPoint = aim.copy()
            speedPoint.normalize()
            // by ticks
            speedPoint.mul(speed / 60)
            return Line(from, from.copy().add(speedPoint)).times(150.0).interpolateBy(speedPoint)
        }

// todo make global bounds?
//        fun fromPointAndAngle(from: Point, angle: Point): Line {
//            val speedPoint = angle.copy().normalize()
//            val infiniteLine = Line(from, speedPoint)
//            val intersectionWithLevel = Global.level.boundLines().map { infiniteLine.infiniteIntersects(it) }.filterNotNull().first()
//            return Line(from, intersectionWithLevel)
//        }

        val OneLenghtZeroAngle = Line(Point(0, 0), Point(1, 0))
    }
}

fun Collection<Point>.toLine(): Line {
    require(this.size == 2)
    return Line(this.elementAt(0), this.elementAt(1))
}