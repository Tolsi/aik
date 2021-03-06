package ru.tolsi.aik.geom

open class Edge internal constructor(
    val parent: Figure2D,
    val line: LineSegment
): ILine by line {
    @Suppress("unused")
    fun hasPoint(point: IPoint): Boolean = line.isBound(point)

    companion object {
        operator fun invoke(parent: Figure2D, p1: IPoint, p2: IPoint): Edge {
            val comp = Point.compare(p1, p2)
            if (comp == 0) throw Error("Repeat points")
            val p = if (comp < 0) p1 else p2
            val q = if (comp < 0) p2 else p1
            return Edge(parent, p, q)
        }

        fun getUniquePointsFromEdges(edges: Iterable<Edge>): List<IPoint> =
            edges.flatMap { listOf(it.line.from, it.line.to) }.distinct()
    }

    override fun toString(): String = "Edge(${this.line}) of ${this.parent}"
}

