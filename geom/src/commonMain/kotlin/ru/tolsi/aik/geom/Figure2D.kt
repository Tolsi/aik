package ru.tolsi.aik.geom

interface WithArea {
    val area: Double
}

interface Figure2D {
    val points: IPointArrayList
    val closed: Boolean
    fun containsPoint(x: Double, y: Double): Boolean = false
    fun containsPoint(p: Point): Boolean = containsPoint(p.x, p.y)
}

val List<IPointArrayList>.totalVertices get() = this.map { it.size }.sum()

fun IPointArrayList.toFigure2D(closed: Boolean = true): Figure2D {
    if (closed && this.size == 4) {
        val x0 = this.getX(0)
        val y0 = this.getY(0)
        val x1 = this.getX(2)
        val y1 = this.getY(2)
        if (this.getX(1) == x1 && this.getY(1) == y0 && this.getX(3) == x0 && this.getY(3) == y1) {
            return Rectangle.fromBounds(x0, y0, x1, y1)
        }
    }
    return if (closed) Polygon(this) else Polyline(this)
}

fun Figure2D.getAllPoints(out: PointArrayList = PointArrayList()): PointArrayList =
    out.apply { for (path in this@getAllPoints.points) add(path) }

fun Figure2D.toPolygon(): Polygon = if (this is Polygon) this else Polygon(this.getAllPoints())

// todo for drop same by EPS points
fun Figure2D.merge(figure: Figure2D): Polygon? {
    val allPoints = this.points.plus(figure.points)
    val uniquePoints = allPoints.distinct()
    val canBeMerged = uniquePoints.size <= allPoints.size - 2
    return if (canBeMerged) {
        uniquePoints.toPolygon()
    } else {
        null
    }
}