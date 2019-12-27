package ru.tolsi.aik.geom

import kotlin.math.abs

data class Polygon(override val points: IPointArrayList) : GeometricFigure2D, WithArea {
    override val closed: Boolean = true
    val closedPoints by lazy {
        this.points.plus(this.points.first())
    }

    override fun containsPoint(x: Double, y: Double): Boolean = this.points.contains(x, y)
    override val area: Double
        get() {
            // Initialize area
            var area = 0.0

            // Calculate value of shoelace formula
            var j = this.points.size - 1
            this.points.indices.forEach { i ->
                area += (this.points.getX(j) + this.points.getX(i)) * (this.points.getY(j) - this.points.getY(i))
                j = i  // j is previous vertex to i
            }

            // Return absolute value
            return abs(area / 2.0)
        }
}

fun Polygon.simplify(): Polygon {
    val result =
        closedPoints.windowed(2).fold(null as Direction? to listOf<Point>()) { (lastDirection, points), (f, s) ->
            val newDirection = f.directionsTo(s).first()
            val newPoints = if (lastDirection == null || lastDirection != newDirection) {
                points.plus(f)
            } else {
                points
            }
            newDirection to newPoints
        }.second
    return Polygon(PointArrayList(result))
}

fun List<IPoint>.toPolygon(): Polygon {
    return Polygon(PointArrayList(this))
}

// https://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm
fun Polygon.intersection(clipper: Line): List<Point> {
    return this.closedPoints.windowed(2)
        .mapNotNull { Line(it.get(0), it.get(1)).intersects(clipper) }
}

// Implements Sutherland–Hodgman algorithm
fun Polygon.clip(clipper: Polygon): Polygon {
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
private fun clip(polygon: Polygon, p1: Point, p2: Point): Polygon {
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
            newPolygonPoints.add(l1.intersectsAsLine(l2)!!)
            newPolygonPoints.add(Point(kx, ky))
        }
        // Case 3: When only second point is outside
        else if (i_pos < 0 && k_pos >= 0) {
            //Only point of intersection with edge is added
            val l1 = Line(Point(x1, y1), Point(x2, y2))
            val l2 = Line(Point(ix, iy), Point(kx, ky))
            newPolygonPoints.add(l1.intersectsAsLine(l2)!!)
        }
        // Case 4: When both points are outside
        else {
            //No points are added
        }
    }

    return Polygon(PointArrayList(newPolygonPoints.toList()))
}

fun Rectangle.toPolygon(): Polygon = this.points.toPolygon()

fun Polygon.edgeIntersections(clipper: Line): Sequence<Point> {
    return this.closedPoints.windowed(2).asSequence()
        .mapNotNull { Line(it.get(0), it.get(1)).intersects(clipper) }
}

fun Polygon.lineView(clipper: Line): Line? {
    val linesInsidePoly = clipper.points().filter { isPointInside(it) }
    return when (linesInsidePoly.size) {
        1 -> {
            val edgeIntersection = this.closedPoints.windowed(2).asSequence()
                .mapNotNull { Line(it.get(0), it.get(1)).intersects(clipper) }
            edgeIntersection.firstOrNull()?.let { Line(linesInsidePoly.first(), it) }
        }
        2 -> clipper
        else -> null
    }
}

fun Polygon.isPointInside(point: Point): Boolean {
    return this.closedPoints.windowed(2).asSequence()
        .any { Line(it.get(0), it.get(1)).intersectsAsLine(point) } && edgeIntersections(
        Line(
            point,
            point.right
        )
    ).toList().size % 2 != 0
}
