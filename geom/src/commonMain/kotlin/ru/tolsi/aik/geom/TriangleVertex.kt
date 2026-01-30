@file:Suppress("NOTHING_TO_INLINE")

package ru.tolsi.aik.geom

import kotlin.math.acos
import kotlin.math.sqrt

/**
 * Interface for a triangle vertex with geometric properties.
 * Provides access to angles, bisectors, medians, altitudes and other triangle-specific data.
 */
interface ITriangleVertex : IVertex {
    val triangle: ITriangle
    val index: Int

    // Geometric properties
    val angle: Angle
    val oppositeEdge: LineSegment
    val adjacentEdges: Pair<LineSegment, LineSegment>
    val bisector: Ray
    val median: LineSegment
    val altitude: LineSegment
    val altitudeLength: Double
}

/**
 * Triangle vertex with rich geometric information.
 *
 * Represents a vertex of a triangle with computed properties including:
 * - Interior angle at the vertex
 * - Angle bisector (ray dividing the angle in half)
 * - Median (line to midpoint of opposite edge)
 * - Altitude (perpendicular to opposite edge)
 * - References to adjacent and opposite edges
 *
 * @property triangle The parent triangle
 * @property index Vertex index in the triangle (0, 1, or 2)
 * @property metadata Optional metadata storage
 */
class TriangleVertex(
    override val triangle: ITriangle,
    override val index: Int,
    metadata: Map<String, Any> = emptyMap()
) : Vertex(triangle.point(index), metadata), ITriangleVertex {

    init {
        require(index in 0..2) { "Triangle vertex index must be 0, 1, or 2, got $index" }
    }

    /**
     * Interior angle at this vertex in radians.
     * Computed using the dot product of adjacent edges.
     */
    override val angle: Angle by lazy {
        val curr = point
        val prev = triangle.point((index + 2) % 3)
        val next = triangle.point((index + 1) % 3)

        // Vectors from current vertex to adjacent vertices
        val v1 = Point(prev.x - curr.x, prev.y - curr.y)
        val v2 = Point(next.x - curr.x, next.y - curr.y)

        // Compute angle using dot product: cos(θ) = (v1 · v2) / (|v1| * |v2|)
        val dotProduct = v1.x * v2.x + v1.y * v2.y
        val len1 = sqrt(v1.x * v1.x + v1.y * v1.y)
        val len2 = sqrt(v2.x * v2.x + v2.y * v2.y)

        val cosAngle = (dotProduct / (len1 * len2)).coerceIn(-1.0, 1.0)
        Angle.fromRadians(acos(cosAngle))
    }

    /**
     * Edge opposite to this vertex.
     */
    override val oppositeEdge: LineSegment by lazy {
        val i1 = (index + 1) % 3
        val i2 = (index + 2) % 3
        LineSegment(triangle.point(i1), triangle.point(i2))
    }

    /**
     * Pair of edges adjacent to this vertex (touching it).
     * First edge goes to previous vertex, second to next vertex.
     */
    override val adjacentEdges: Pair<LineSegment, LineSegment> by lazy {
        val curr = point
        val prev = triangle.point((index + 2) % 3)
        val next = triangle.point((index + 1) % 3)

        Pair(
            LineSegment(curr, prev),
            LineSegment(curr, next)
        )
    }

    /**
     * Angle bisector ray from this vertex.
     * Divides the interior angle into two equal parts.
     */
    override val bisector: Ray by lazy {
        val curr = point
        val prev = triangle.point((index + 2) % 3)
        val next = triangle.point((index + 1) % 3)

        // Unit vectors to adjacent vertices
        val v1 = Point(prev.x - curr.x, prev.y - curr.y)
        val len1 = sqrt(v1.x * v1.x + v1.y * v1.y)
        val u1 = Point(v1.x / len1, v1.y / len1)

        val v2 = Point(next.x - curr.x, next.y - curr.y)
        val len2 = sqrt(v2.x * v2.x + v2.y * v2.y)
        val u2 = Point(v2.x / len2, v2.y / len2)

        // Bisector direction is the sum of unit vectors
        val bisectorDir = Point(u1.x + u2.x, u1.y + u2.y)
        val bisectorLen = sqrt(bisectorDir.x * bisectorDir.x + bisectorDir.y * bisectorDir.y)

        if (bisectorLen < Geometry.EPS) {
            // Degenerate case: angle is 180°, use perpendicular
            Ray(curr, Point(curr.x - u1.y, curr.y + u1.x))
        } else {
            val bisectorUnit = Point(bisectorDir.x / bisectorLen, bisectorDir.y / bisectorLen)

            // Extend ray far enough (use triangle's bounding box diagonal)
            val extend = 1000.0 // Large enough for practical purposes
            val endPoint = Point(curr.x + bisectorUnit.x * extend, curr.y + bisectorUnit.y * extend)

            Ray(curr, endPoint)
        }
    }

    /**
     * Median from this vertex to the midpoint of the opposite edge.
     */
    override val median: LineSegment by lazy {
        val oppEdge = oppositeEdge
        val midpoint = Point(
            (oppEdge.from.x + oppEdge.to.x) / 2.0,
            (oppEdge.from.y + oppEdge.to.y) / 2.0
        )
        LineSegment(point, midpoint)
    }

    /**
     * Altitude from this vertex perpendicular to the opposite edge.
     */
    override val altitude: LineSegment by lazy {
        val oppEdge = oppositeEdge
        val curr = point

        // Vector along opposite edge
        val edgeVec = Point(oppEdge.to.x - oppEdge.from.x, oppEdge.to.y - oppEdge.from.y)
        val edgeLenSq = edgeVec.x * edgeVec.x + edgeVec.y * edgeVec.y

        // Vector from edge start to current vertex
        val toVertex = Point(curr.x - oppEdge.from.x, curr.y - oppEdge.from.y)

        // Project vertex onto edge line: t = (toVertex · edgeVec) / |edgeVec|²
        val t = (toVertex.x * edgeVec.x + toVertex.y * edgeVec.y) / edgeLenSq

        // Foot of perpendicular on the opposite edge
        val footPoint = Point(
            oppEdge.from.x + t * edgeVec.x,
            oppEdge.from.y + t * edgeVec.y
        )

        LineSegment(curr, footPoint)
    }

    /**
     * Length of the altitude (height from this vertex to opposite edge).
     */
    override val altitudeLength: Double by lazy {
        val alt = altitude
        val dx = alt.to.x - alt.from.x
        val dy = alt.to.y - alt.from.y
        sqrt(dx * dx + dy * dy)
    }

    override fun toString(): String =
        "TriangleVertex(index=$index, point=$point, angle=$angle)"

    companion object {
        /**
         * Creates all three vertices of a triangle.
         */
        fun verticesOf(triangle: ITriangle): List<TriangleVertex> =
            listOf(
                TriangleVertex(triangle, 0),
                TriangleVertex(triangle, 1),
                TriangleVertex(triangle, 2)
            )
    }
}

/**
 * Extension property to get all three vertices of a triangle.
 */
val ITriangle.vertices: List<TriangleVertex>
    get() = TriangleVertex.verticesOf(this)

/**
 * Extension property to get a specific vertex by index.
 */
fun ITriangle.vertex(index: Int): TriangleVertex = TriangleVertex(this, index)
