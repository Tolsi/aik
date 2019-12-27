package ru.tolsi.aik.geom

data class Edge internal constructor(
    val parent: GeometricFigure2D,
    val p: IPoint,
    val q: IPoint
) {
    @Suppress("unused")
    fun hasPoint(point: IPoint): Boolean = (p == point) || (q == point)

    companion object {
        operator fun invoke(parent: GeometricFigure2D, p1: IPoint, p2: IPoint): Edge {
            val comp = Point.compare(p1, p2)
            if (comp == 0) throw Error("Repeat points")
            val p = if (comp < 0) p1 else p2
            val q = if (comp < 0) p2 else p1
            return Edge(parent, p, q)
        }

        fun getUniquePointsFromEdges(edges: Iterable<Edge>): List<IPoint> =
            edges.flatMap { listOf(it.p, it.q) }.distinct()
    }

    override fun toString(): String = "Edge(${this.p}, ${this.q})"
}

