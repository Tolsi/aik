package ru.tolsi.aik.geom.extensions

import ru.tolsi.aik.geom.*
import ru.tolsi.aik.geom.shape.Shape2d
import kotlin.math.*

// todo refactor it
fun Point.toRectangleWithCenterInPoint(radius: Double): Rectangle {
    return Rectangle(this.x - radius, this.y - radius, radius * 2, radius * 2)
}

fun IRectangle.bottomSide(): Collection<Point> {
    return listOf(Point(round(x), y),
            Point(round(x + width), y))
}

fun IRectangle.topSide(): Collection<Point> {
    return listOf(Point(round(x), y + height),
            Point(round(x + width), y + height))
}

fun IRectangle.leftSide(): Collection<Point> {
    return listOf(Point(x, round(y)),
            Point(x, round(y + height)))
}

fun IRectangle.rightSide(): Collection<Point> {
    return listOf(Point(x + width, round(y)),
            Point(x + width, round(y + height)))
}

fun IRectangle.boundLines(): Collection<Line> {
    return listOf(
            this.leftSide().toLine(),
            this.rightSide().toLine(),
            this.topSide().toLine(),
            this.bottomSide().toLine())
}

fun <T> List<T>.permutations(): Sequence<List<T>> {
    if (this.size == 1) return sequenceOf(this)
    val list = this
    return sequence {
        val sub = list.get(0)
        for (perm in list.drop(1).permutations())
            for (i in 0..perm.size) {
                val newPerm = perm.toMutableList()
                newPerm.add(i, sub)
                yield(newPerm)
            }
    }
}

fun <T : Comparable<T>> ClosedRange<T>.bounds(): Pair<T, T> {
    return start to endInclusive
}

fun Point.toZeroAngleLine(): Line {
    return Line(this, this.right)
}

fun List<Point>.epsUnique(): List<Point> {
    return this.fold(listOf()) { res, p ->
        if (!res.any { it.compareTo(p) == 0 }) {
            res.plus(p)
        } else res
    }
}

fun List<Point>.epsRemove(remove: Point): List<Point> {
    return this.fold(mutableListOf()) { res, p ->
        if (p.compareTo(remove) != 0) {
            res.add(p)
            res
        } else {
            res
        }
    }
}

fun List<Point>.epsRemove(remove: List<Point>): List<Point> {
    return this.fold(mutableListOf()) { res, p ->
        if (remove.all { p.compareTo(it) != 0 }) {
            res.add(p)
            res
        } else {
            res
        }
    }
}

fun List<Point>.epsContains(p: Point): Boolean {
    return this.any { it.compareTo(p) == 0 }
}

fun Point.projectTo(l: Line): Point {
    // get dot product of e1, e2
    val e1 = Point(l.to.x - l.from.x, l.to.y - l.from.y)
    val e2 = Point(x - l.from.x, y - l.from.y)
    val valDp = e1.dot(e2)
    // get squared length of e1
    val len = e1.dot(e1)
    return Point(l.from.x + valDp * e1.x / len,
            l.from.y + valDp * e1.y / len)
}

fun Line.moveTo(point: Point): Line {
    val normalized = this.toLenghtOneLine().normalize()
    return Line(point, point.plus(normalized.to).mutable)
}

fun Shape2d.Polygon.edgeIntersections(clipper: Line): Sequence<Point> {
    return this.closedPoints.windowed(2).asSequence()
        .mapNotNull { Line(it.get(0), it.get(1)).intersects(clipper) }
}

fun Shape2d.Polygon.lineView(clipper: Line): Line? {
    val linesInsidePoly = clipper.points().filter { isPointInside(it) }
    return when (linesInsidePoly.size) {
        1 -> {
            val edgeIntersection = this.closedPoints.windowed(2).asSequence()
                .mapNotNull { Line(it.get(0), it.get(1)).intersects(clipper) }
            edgeIntersection.firstOrNull() ?.let { Line(linesInsidePoly.first(), it) }
        }
        2 -> clipper
        else -> null
    }
}

fun Shape2d.Polygon.isPointInside(point: Point): Boolean {
    return this.closedPoints.windowed(2).asSequence()
        .any { Line(it.get(0), it.get(1)).infiniteIntersects(point) } && edgeIntersections(Line(point, point.right)).toList().size % 2 != 0
}