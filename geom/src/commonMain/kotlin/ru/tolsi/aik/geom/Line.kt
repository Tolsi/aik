package ru.tolsi.aik.geom

import kotlin.math.*

interface ILine: Figure2D {
    val from: IPoint
    val to: IPoint

    fun intersects(p: IPoint): Boolean
    fun intersects(l: ILine): IPoint?

    fun isBound(p: IPoint): Boolean {
        return (from == p) || (to == p)
    }

    val length: Double
        get() = from.distanceTo(to)

    fun distance(p: IPoint): Double {
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

    fun intersectsAsSegment(p: IPoint): Boolean {
        val position = classifyPointRelativeToSegment(from, to, p)
        return position == Position.BETWEEN || position == Position.START || position == Position.END
    }

    fun intersectsAsRay(p: ILine): IPoint? {
        return intersectsAsLine(p)?.takeIf {
            intersectsAsRay(it)
        }
    }

    fun intersectsAsRay(p: IPoint): Boolean {
        val position = classifyPointRelativeToSegment(from, to, p)
        return position != Position.LEFT && position != Position.RIGHT && position != Position.BEHIND
    }

    fun intersectsAsLine(p: IPoint): Boolean {
        val position = classifyPointRelativeToSegment(from, to, p)
        return position != Position.LEFT && position != Position.RIGHT
    }

    fun intersectsAsLine(p: ILine): IPoint? {
        val d = (this.b() * p.a() - this.a() * p.b())
        val x = (this.c() * p.b() - this.b() * p.c()) / d
        val y = (this.c() * p.a() - this.a() * p.c()) / d
        return IPoint(x, y).takeIf { x.isFinite() && y.isFinite() }
    }

    fun intersectsAsSegment(l: ILine): IPoint? {
        return intersectsAsLine(l)?.takeIf { intersectsAsSegment(it) }
    }

    fun IPoint.isDirectedTo(lineDirections: List<Direction>): Boolean {
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

    fun interpolateBy(step: IPoint) = sequence {
        val current: Point = from.copy().mutable
        while (from.distanceTo(current) < length) {
            current.add(step)
            yield(current)
        }
    }

    fun times(n: Double): LineSegment {
        return LineSegment(from, IPoint(from.x + (to.x - from.x) * n, from.y + (to.y - from.y) * n))
    }

    fun normalize(): LineSegment {
        val normX = (to.x - from.x) / length
        val normY = (to.y - from.y) / length
        return LineSegment(Point.Zero.mutable, IPoint(normX, normY).mutable)
    }

    fun toLenghtOneLine(): LineSegment {
        val normX = (to.x - from.x) / length
        val normY = (to.y - from.y) / length
        return LineSegment(from, from.plus(IPoint(normX, normY)).mutable)
    }

    fun withLength(length: Double): LineSegment {
        return toLenghtOneLine().times(length)
    }

    override val points get() = PointArrayList(2).apply { add(from).add(to) }
    override val closed: Boolean get() = false

    fun moveTo(IPoint: IPoint): Line {
        val normalized = this.toLenghtOneLine().normalize()
        return Line(IPoint, IPoint.plus(normalized.to).mutable)
    }

    fun toPair(): Pair<IPoint, IPoint> {
        return this.from to this.to
    }

    fun toRay(): Ray {
        return Ray(from, to)
    }

    fun toLine(): Line {
        return Line(from, to)
    }

    fun toLineSegment(): LineSegment {
        return LineSegment(from, to)
    }

    /*
                /  BEYOND
               /
      LEFT    * END
             /
            / BETWEEN
           /          RIGHT
          * START
         /
        / BEHIND
     */
    enum class Position {
        LEFT, RIGHT, START, END, BEHIND, BEYOND, BETWEEN
    }
    fun classifyPointRelativeToSegment(segmentStart: IPoint, segmentEnd: IPoint, p: IPoint): Position {
        val segmentStart2segmentEnd = segmentEnd.minus(segmentStart)
        val segmentStart2p = p.minus(segmentStart)
        val area = segmentStart2segmentEnd.x * segmentStart2p.y - segmentStart2p.x * segmentStart2segmentEnd.y

        return if (area > Geometry.EPS) {
            Position.LEFT
        } else if (area < -Geometry.EPS) {
            Position.RIGHT
        } else if (segmentStart2p.length < Geometry.EPS) {
            Position.START
        } else if (p.minus(segmentEnd).length < Geometry.EPS) {
            Position.END
        } else if (segmentStart2segmentEnd.x * segmentStart2p.x < 0 || segmentStart2segmentEnd.y * segmentStart2p.y < 0) {
            Position.BEHIND
        } else if (segmentStart2segmentEnd.length < segmentStart2p.length) {
            Position.BEYOND
        } else {
            Position.BETWEEN
        }
    }
}

open class Line(override val from: IPoint, override val to: IPoint): ILine {

    init {
        require(from != to)
    }



    override fun distance(p: IPoint): Double {
        return abs((to.y - from.y) * p.x - (to.x - from.x) * p.y + to.x * from.y - to.y * from.x) / sqrt((to.y - from.y).pow(2.0) + (to.x - from.x).pow(2.0))
    }

    fun points(): List<IPoint> = listOf(from, to)
    override fun toString(): String {
        return "Line(from=$from, to=$to)"
    }

    fun rotate(angle: Angle): Line {
        val diffX = to.x - from.x
        val diffY = to.y - from.y
        val newToPoint = IPoint(
            diffX * cos(angle.radians) - diffY * sin(angle.radians),
            diffX * sin(angle.radians) + diffY * cos(angle.radians))
        return Line(from, from.plus(newToPoint).mutable)
    }

    companion object {
        operator fun invoke(x1: Number, y1: Number, x2: Number, y2: Number): Line = Line(IPoint(x1, y1), IPoint(x2, y2))

        // todo limit!
        fun createFromPointAimAndSpeed(from: IPoint, aim: IPoint, speed: Double): Sequence<IPoint> {
            val speedPoint = aim.copy().mutable
            speedPoint.normalize()
            // by ticks
            speedPoint.mul(speed / 60)
            return Line(from, from.copy().mutable.add(speedPoint)).times(150.0).interpolateBy(speedPoint)
        }

        val OneLenghtZeroAngle = Line(IPoint(0, 0), IPoint(1, 0))
    }

    override fun intersects(p: IPoint): Boolean {
        return intersectsAsLine(p)
    }

    override fun intersects(l: ILine): IPoint? {
        return intersectsAsLine(l)
    }

    override fun containsPoint(x: Double, y: Double) = intersects(IPoint(x, y))
}

fun Collection<IPoint>.toLine(): Line {
    require(this.size == 2)
    return Line(this.elementAt(0), this.elementAt(1))
}

fun Pair<IPoint, IPoint>.toLine(): Line {
    return Line(this.first, this.second)
}