package ru.tolsi.aik.geom.shape

import com.soywiz.kds.Array2
import com.soywiz.kds.Queue
import com.soywiz.kds.Stack
import ru.tolsi.aik.geom.*
import ru.tolsi.aik.geom.internal.niceStr
import kotlin.math.*

abstract class Shape2d {
    abstract val paths: List<IPointArrayList>
    abstract val closed: Boolean
    open fun containsPoint(x: Double, y: Double) = false

    interface WithArea {
        val area: Double
    }

    object Empty : Shape2d(), WithArea {
        override val paths: List<PointArrayList> = listOf(PointArrayList(0))
        override val closed: Boolean = false
        override val area: Double = 0.0
        override fun containsPoint(x: Double, y: Double) = false
    }

    data class Line(val x0: Double, val y0: Double, val x1: Double, val y1: Double) : Shape2d(), WithArea {
        companion object {
            inline operator fun invoke(x0: Number, y0: Number, x1: Number, y1: Number) = Line(x0.toDouble(), y0.toDouble(), x1.toDouble(), y1.toDouble())
            inline operator fun invoke(p1: Point, p2: Point) = Line(p1.x, p1.y, p2.x, p2.y)
        }

        override val paths get() = listOf(PointArrayList(2).apply { add(x0, y0).add(x1, y1) })
        override val closed: Boolean = false
        override val area: Double get() = 0.0
        override fun containsPoint(x: Double, y: Double) = false
    }

    data class Circle(val x: Double, val y: Double, val radius: Double, val totalPoints: Int = 32) : Shape2d(), WithArea {
        companion object {
            inline operator fun invoke(x: Number, y: Number, radius: Number, totalPoints: Int = 32) = Circle(x.toDouble(), y.toDouble(), radius.toDouble(), totalPoints)
        }

        override val paths by lazy {
            listOf(PointArrayList(totalPoints) {
                for (it in 0 until totalPoints) {
                    add(
                            x + Angle.cos01(it.toDouble() / totalPoints.toDouble()) * radius,
                            y + Angle.sin01(it.toDouble() / totalPoints.toDouble()) * radius
                    )
                }
            })
        }
        override val closed: Boolean = true
        override val area: Double get() = PI.toDouble() * radius * radius
        override fun containsPoint(x: Double, y: Double) = hypot(this.x - x, this.y - y) < radius
    }

    data class Rectangle(val rect: ru.tolsi.aik.geom.Rectangle) : Shape2d(), WithArea, IRectangle by rect {
        companion object {
            inline operator fun invoke(x: Number, y: Number, width: Number, height: Number) = Rectangle(ru.tolsi.aik.geom.Rectangle(x, y, width, height))
        }

        override val paths = listOf(PointArrayList(4) { add(x, y).add(x, y + height).add(x + width, y + height).add(x + width, y) })
        override val closed: Boolean = true
        override val area: Double get() = width * height
        override fun containsPoint(x: Double, y: Double) = (x in this.left..this.right) && (y in this.top..this.bottom)
        override fun toString(): String =
                "Rectangle(x=${x.niceStr}, y=${y.niceStr}, width=${width.niceStr}, height=${height.niceStr})"
    }

    data class Polygon(val points: IPointArrayList) : Shape2d(), WithArea {
        override val paths = listOf(points)
        override val closed: Boolean = true
        val closedPoints by lazy {
            points.plus(points.first())
        }

        override fun containsPoint(x: Double, y: Double): Boolean = this.points.contains(x, y)
        override val area: Double
            get() {
                // Initialize area
                var area = 0.0

                // Calculate value of shoelace formula
                var j = points.size - 1
                points.indices.forEach { i ->
                    area += (points.getX(j) + points.getX(i)) * (points.getY(j) - points.getY(i))
                    j = i  // j is previous vertex to i
                }

                // Return absolute value
                return abs(area / 2.0)
            }
    }

    data class Polyline(val points: IPointArrayList) : Shape2d(), WithArea {
        override val paths = listOf(points)
        override val closed: Boolean = false
        override val area: Double get() = 0.0
        override fun containsPoint(x: Double, y: Double) = false
    }

    data class Complex(val items: List<Shape2d>) : Shape2d() {
        override val paths by lazy { items.flatMap { it.paths } }
        override val closed: Boolean = false
        override fun containsPoint(x: Double, y: Double): Boolean = this.getAllPoints().contains(x, y)
    }
}

val List<IPointArrayList>.totalVertices get() = this.map { it.size }.sum()

fun Rectangle.toShape() = Shape2d.Rectangle(x, y, width, height)

fun IPointArrayList.toShape2d(closed: Boolean = true): Shape2d {
    if (closed && this.size == 4) {
        val x0 = this.getX(0)
        val y0 = this.getY(0)
        val x1 = this.getX(2)
        val y1 = this.getY(2)
        if (this.getX(1) == x1 && this.getY(1) == y0 && this.getX(3) == x0 && this.getY(3) == y1) {
            return Shape2d.Rectangle(Rectangle.fromBounds(x0, y0, x1, y1))
        }
    }
    return if (closed) Shape2d.Polygon(this) else Shape2d.Polyline(this)
}

fun Shape2d.getAllPoints(out: PointArrayList = PointArrayList()): PointArrayList = out.apply { for (path in this@getAllPoints.paths) add(path) }
fun Shape2d.toPolygon(): Shape2d.Polygon = if (this is Shape2d.Polygon) this else Shape2d.Polygon(this.getAllPoints())
fun Rectangle.toPolygon(): Shape2d.Polygon = this.points.toPolygon()

fun List<IPoint>.containsPoint(x: Double, y: Double): Boolean {
    var intersections = 0
    for (n in 0 until this.size - 1) {
        val p1 = this[n + 0]
        val p2 = this[n + 1]
        intersections += HorizontalLine.intersectionsWithLine(x, y, p1.x, p1.y, p2.x, p2.y)
    }
    return (intersections % 2) != 0
}

// https://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm
fun Shape2d.Polygon.intersection(clipper: Line): List<Point> {
    return this.closedPoints.windowed(2)
        .mapNotNull { Line(it.get(0), it.get(1)).intersects(clipper) }
}

// Implements Sutherland–Hodgman algorithm
fun Shape2d.Polygon.clip(clipper: Shape2d.Polygon): Shape2d.Polygon {
//    require(this.closed && clipper.closed)
    // todo what if it is inside of clipper?
//    if (this.getAllPoints().all { clipper.containsPoint(it.x, it.y) }) {
//        return this.toPolygon()
//    }
    val clipperPolygon = clipper.toPolygon()
    return clipperPolygon.points.indices.fold(this.toPolygon(), { polygon, i ->
        //i and k are two consecutive indexes
        val k = (i + 1) % clipperPolygon.points.size

        // We pass the current array of vertices, it's size
        // and the end points of the selected clipper line
        clip(polygon, clipperPolygon.points.get(i), clipperPolygon.points.get(k))
    })
}

// https://www.geeksforgeeks.org/polygon-clipping-sutherland-hodgman-algorithm-please-change-bmp-images-jpeg-png/
// This functions clips all the edges w.r.t one clip
// edge of clipping area
private fun clip(polygon: Shape2d.Polygon, p1: Point, p2: Point): Shape2d.Polygon {
    val newPolygonPoints = mutableSetOf<Point>()
    val x1 = p1.x
    val y1 = p1.y
    val x2 = p2.x
    val y2 = p2.y

    polygon.points.indices.forEach { i ->
        // i and k form a line in polygon
        val k: Int = (i + 1) % polygon.points.size
        // (ix,iy),(kx,ky) are the co-ordinate values of
        // the points
        val ix = polygon.points.getX(i)
        val iy = polygon.points.getY(i)
        val kx = polygon.points.getX(k)
        val ky = polygon.points.getY(k)

        // Calculating position of first point
        // w.r.t. clipper line
        val i_pos = (x2 - x1) * (iy - y1) - (y2 - y1) * (ix - x1)

        // Calculating position of second point
        // w.r.t. clipper line
        val k_pos = (x2 - x1) * (ky - y1) - (y2 - y1) * (kx - x1)

        // Case 1 : When both points are inside
        if (i_pos < 0 && k_pos < 0) {
            //Only second point is added
            newPolygonPoints.add(Point(kx, ky))
        }
        // Case 2: When only first point is outside
        else if (i_pos >= 0 && k_pos < 0) {
            // Point of intersection with edge
            // and the second point is added
            val l1 = Line(Point(x1, y1), Point(x2, y2))
            val l2 = Line(Point(ix, iy), Point(kx, ky))
            newPolygonPoints.add(l1.infiniteIntersects(l2)!!)
            newPolygonPoints.add(Point(kx, ky))
        }
        // Case 3: When only second point is outside
        else if (i_pos < 0 && k_pos >= 0) {
            //Only point of intersection with edge is added
            val l1 = Line(Point(x1, y1), Point(x2, y2))
            val l2 = Line(Point(ix, iy), Point(kx, ky))
            newPolygonPoints.add(l1.infiniteIntersects(l2)!!)
        }
        // Case 4: When both points are outside
        else {
            //No points are added
        }
    }

    return Shape2d.Polygon(PointArrayList(newPolygonPoints.toList()))
}

fun Shape2d.merge(shape2d: Shape2d): Shape2d.Polygon? {
    val allPoints = this.paths.flatten().plus(shape2d.paths.flatten())
    val uniquePoints = allPoints.distinct()
    val canBeMerged = uniquePoints.size <= allPoints.size - 2
    return if (canBeMerged) {
        uniquePoints.toPolygon()
    } else {
        null
    }
}

fun Collection<Point>.twoWaysBfsTravellingSalesmanProblem(): List<Point>? {

    if (this.size % 2 != 0) return null
    // dfs
    // val allowedPaths = Stack<Pair<List<Point>, List<Point>>>()
    // bfs
    val allowedPaths = Queue<Pair<List<Point>, List<Point>>>()
    val sortedPoints = this.groupBy { it.x }.flatMap { it.value.sortedBy { it.y } }
    val startAndPreEndPoint = sortedPoints.first()
    allowedPaths.enqueue(listOf(startAndPreEndPoint) to listOf())
    do {
        val (leftPath, rightPath) = allowedPaths.dequeue()
        val notUsed = this.toSet().minus(leftPath).minus(rightPath)
        val lastLeftPoint = leftPath.last()
        val lastRightPointOpt = rightPath.lastOrNull()
        if (lastRightPointOpt.let { it == lastLeftPoint }) {
            if (notUsed.isEmpty()) {
                return leftPath.plus(rightPath.dropLast(1).asReversed())
            }
        } else if (lastRightPointOpt == null) {
            val leftNeighboursPoints = lastLeftPoint.neighbours.filter { notUsed.contains(it) }
            allowedPaths.enqueue(leftPath.plus(leftNeighboursPoints[0]) to listOf(leftNeighboursPoints[1]))
        } else {
            val leftNeighboursPoints = lastLeftPoint.neighbours.filter { notUsed.contains(it) }
            val rightNeighboursPoints = lastRightPointOpt.neighbours.filter { notUsed.contains(it) }
            leftNeighboursPoints.forEach {leftNeighbour ->
                rightNeighboursPoints.forEach { rightNeighbour ->
                    allowedPaths.enqueue(leftPath.plus(leftNeighbour) to rightPath.plus(rightNeighbour))
                }
            }
        }
    } while (allowedPaths.isNotEmpty())
    return null
}

fun Collection<Point>.dfsTravellingSalesmanProblem(): List<Point>? {
    // dfs
    val allowedPaths = Stack<List<Point>>()
    // bfs
//    val allowedPaths = Queue<List<Point>>()
    val sortedPoints = this.groupBy { it.x }.flatMap { it.value.sortedBy { it.y } }
    val startAndPreEndPoint = sortedPoints.first()
    allowedPaths.push(listOf(startAndPreEndPoint))
    do {
        val mayBePath = allowedPaths.pop()
        val notUsed = this.toSet().minus(mayBePath)
        val lastPoint = mayBePath.last()
        if (lastPoint.neighbours.contains(startAndPreEndPoint) && notUsed.isEmpty()) {
            return mayBePath
        } else {
            val nextPoints = lastPoint.neighbours.filter { notUsed.contains(it) }
            nextPoints.forEach {
                val mayBePathBack = notUsed.plus(startAndPreEndPoint).dfs(it, startAndPreEndPoint)?.dropLast(1)
                if (mayBePathBack != null) {
                    if (mayBePathBack.size == notUsed.size) {
                        return mayBePath.plus(mayBePathBack)
                    } else {
                        allowedPaths.push(mayBePath.plus(it))
                    }
                }
            }
        }
    } while (allowedPaths.isNotEmpty())
    return null
}

fun Collection<Point>.dfs(from: Point, to: Point): List<Point>? {
    val allowedPaths = Stack<List<Point>>()
//    val allowedPaths = Queue<List<Point>>()
    allowedPaths.push(listOf(from))
    if (from == to) {
        return allowedPaths.first()
    }
    do {
        val mayBePath = allowedPaths.pop()
        val notUsed = this.toSet().minus(mayBePath)
        val lastPoint = mayBePath.last()
        if (lastPoint.neighbours.contains(to)) {
            return mayBePath
        } else {
            val nextPoints = lastPoint.neighbours.filter { notUsed.contains(it) }
            nextPoints.forEach {
                allowedPaths.push(mayBePath.plus(it))
            }
        }
    } while (allowedPaths.isNotEmpty())
    return null
}

fun Collection<Point>.bfs(from: Point, to: Point): List<Point>? {
//    val allowedPaths = Stack<List<Point>>()
    val allowedPaths = Queue<List<Point>>()
    allowedPaths.enqueue(listOf(from))
    if (from == to) {
        return allowedPaths.first()
    }
    do {
        val mayBePath = allowedPaths.dequeue()
        val notUsed = this.toSet().minus(mayBePath)
        val lastPoint = mayBePath.last()
        if (lastPoint.neighbours.contains(to)) {
            return mayBePath
        } else {
            val nextPoints = lastPoint.neighbours.filter { notUsed.contains(it) }
            nextPoints.forEach {
                allowedPaths.enqueue(mayBePath.plus(it))
            }
        }
    } while (allowedPaths.isNotEmpty())
    return null
}

// todo
//fun Collection<Point>.aStar(from: Point, to: Point): List<Point>? {
//    val set = this.toSet()
//    val board = Array2.withGen(Global.level.width.toInt(), Global.level.height.toInt(), { x, y -> !set.contains(Point(x, y)) })
//    return AStar.find(board, from.x.toInt(), from.y.toInt(), to.x.toInt(), to.y.toInt()).toPoints().map { it.asDouble() }
//}

fun List<IPoint>.toPolygon(): Shape2d.Polygon {
    return Shape2d.Polygon(PointArrayList(this))
}

fun Shape2d.Polygon.travellingSalesmanProblem(): Shape2d.Polygon {
    return this.points.twoWaysBfsTravellingSalesmanProblem()!!.toPolygon()
}

fun Shape2d.Polygon.simplify(): Shape2d.Polygon {
    val result = closedPoints.windowed(2).fold(null as Direction? to listOf<Point>()) { (lastDirection, points), (f, s) ->
        val newDirection = f.directionsTo(s).first()
        val newPoints = if (lastDirection == null || lastDirection != newDirection) {
            points.plus(f)
        } else {
            points
        }
        newDirection to newPoints
    }.second
    return Shape2d.Polygon(PointArrayList(result))
}