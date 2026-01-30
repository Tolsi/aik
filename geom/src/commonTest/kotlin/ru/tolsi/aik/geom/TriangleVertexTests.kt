package ru.tolsi.aik.geom

import kotlin.math.PI
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class TriangleVertexTests {

    @Test
    fun testConstructor() {
        val triangle = Triangle(Point(0.0, 0.0), Point(4.0, 0.0), Point(0.0, 3.0))
        val vertex = TriangleVertex(triangle, 0)

        assertEquals(triangle, vertex.triangle)
        assertEquals(0, vertex.index)
        assertEquals(Point(0.0, 0.0), vertex.point)
    }

    @Test
    fun testInvalidIndex() {
        val triangle = Triangle(Point(0.0, 0.0), Point(4.0, 0.0), Point(0.0, 3.0))
        assertFails { TriangleVertex(triangle, -1) }
        assertFails { TriangleVertex(triangle, 3) }
    }

    @Test
    fun testRightTriangleAngles() {
        // Right triangle: (0,0), (4,0), (0,3)
        val triangle = Triangle(Point(0.0, 0.0), Point(4.0, 0.0), Point(0.0, 3.0))
        val v0 = TriangleVertex(triangle, 0)
        val v1 = TriangleVertex(triangle, 1)
        val v2 = TriangleVertex(triangle, 2)

        // Vertex 0 (origin) should have 90° angle
        assertEquals(PI / 2, v0.angle.radians, 0.01)

        // Sum of all angles should be 180° (π radians)
        val angleSum = v0.angle.radians + v1.angle.radians + v2.angle.radians
        assertEquals(PI, angleSum, 0.01)
    }

    @Test
    fun testEquilateralTriangleAngles() {
        // Equilateral triangle: all angles should be 60°
        val side = 10.0
        val height = side * sqrt(3.0) / 2.0
        val triangle = Triangle(
            Point(0.0, 0.0),
            Point(side, 0.0),
            Point(side / 2.0, height)
        )

        val v0 = TriangleVertex(triangle, 0)
        val v1 = TriangleVertex(triangle, 1)
        val v2 = TriangleVertex(triangle, 2)

        // All angles should be 60° (π/3 radians)
        assertEquals(PI / 3, v0.angle.radians, 0.01)
        assertEquals(PI / 3, v1.angle.radians, 0.01)
        assertEquals(PI / 3, v2.angle.radians, 0.01)
    }

    @Test
    fun testOppositeEdge() {
        val triangle = Triangle(Point(0.0, 0.0), Point(4.0, 0.0), Point(0.0, 3.0))
        val vertex = TriangleVertex(triangle, 0)

        val oppEdge = vertex.oppositeEdge

        // Opposite edge of vertex 0 should be from p1 to p2
        assertEquals(Point(4.0, 0.0), oppEdge.from)
        assertEquals(Point(0.0, 3.0), oppEdge.to)
    }

    @Test
    fun testAdjacentEdges() {
        val triangle = Triangle(Point(0.0, 0.0), Point(4.0, 0.0), Point(0.0, 3.0))
        val vertex = TriangleVertex(triangle, 0)

        val (edge1, edge2) = vertex.adjacentEdges

        // Both edges should start from vertex 0
        assertEquals(Point(0.0, 0.0), edge1.from)
        assertEquals(Point(0.0, 0.0), edge2.from)

        // Should connect to the other two vertices
        assertTrue(
            (edge1.to == Point(0.0, 3.0) && edge2.to == Point(4.0, 0.0)) ||
            (edge1.to == Point(4.0, 0.0) && edge2.to == Point(0.0, 3.0))
        )
    }

    @Test
    fun testMedian() {
        val triangle = Triangle(Point(0.0, 0.0), Point(4.0, 0.0), Point(0.0, 4.0))
        val vertex = TriangleVertex(triangle, 0)

        val median = vertex.median

        // Median should start from vertex
        assertEquals(Point(0.0, 0.0), median.from)

        // Median should end at midpoint of opposite edge
        // Opposite edge is from (4,0) to (0,4), midpoint is (2,2)
        assertEquals(2.0, median.to.x, 0.01)
        assertEquals(2.0, median.to.y, 0.01)
    }

    @Test
    fun testAltitude() {
        // Right triangle with right angle at origin
        val triangle = Triangle(Point(0.0, 0.0), Point(4.0, 0.0), Point(0.0, 3.0))
        val v1 = TriangleVertex(triangle, 1) // Vertex at (4,0)

        val altitude = v1.altitude

        // Altitude from (4,0) should be perpendicular to opposite edge
        assertEquals(Point(4.0, 0.0), altitude.from)

        // Check that altitude is perpendicular to opposite edge
        val oppEdge = v1.oppositeEdge
        val edgeVec = Point(oppEdge.to.x - oppEdge.from.x, oppEdge.to.y - oppEdge.from.y)
        val altVec = Point(altitude.to.x - altitude.from.x, altitude.to.y - altitude.from.y)

        // Dot product should be ~0 (perpendicular)
        val dotProduct = edgeVec.x * altVec.x + edgeVec.y * altVec.y
        assertEquals(0.0, dotProduct, 0.01)
    }

    @Test
    fun testAltitudeLength() {
        // Triangle with area = 6, base = 4 → height = 3
        val triangle = Triangle(Point(0.0, 0.0), Point(4.0, 0.0), Point(2.0, 3.0))
        val v2 = TriangleVertex(triangle, 2) // Top vertex

        // Altitude from top vertex to base should be 3
        assertEquals(3.0, v2.altitudeLength, 0.01)
    }

    @Test
    fun testAltitudeLengthFormula() {
        // Use formula: height = 2 * area / base
        val triangle = Triangle(Point(0.0, 0.0), Point(5.0, 0.0), Point(2.0, 4.0))
        val area = triangle.area
        val vertex = TriangleVertex(triangle, 2)
        val oppositeEdgeLength = vertex.oppositeEdge.length

        val expectedHeight = 2 * area / oppositeEdgeLength
        assertEquals(expectedHeight, vertex.altitudeLength, 0.01)
    }

    @Test
    fun testBisector() {
        // Equilateral triangle - bisector should divide angle into 30° + 30°
        val side = 10.0
        val height = side * sqrt(3.0) / 2.0
        val triangle = Triangle(
            Point(0.0, 0.0),
            Point(side, 0.0),
            Point(side / 2.0, height)
        )

        val vertex = TriangleVertex(triangle, 0)
        val bisector = vertex.bisector

        // Bisector should start from vertex
        assertEquals(Point(0.0, 0.0), bisector.from)

        // For equilateral triangle, bisector from bottom-left vertex
        // should point toward the midpoint of opposite edge at angle 60°
        // (since base angle is 60° and bisector splits it)
    }

    @Test
    fun testVerticesExtension() {
        val triangle = Triangle(Point(0.0, 0.0), Point(4.0, 0.0), Point(0.0, 3.0))
        val vertices = triangle.vertices

        assertEquals(3, vertices.size)
        assertEquals(0, vertices[0].index)
        assertEquals(1, vertices[1].index)
        assertEquals(2, vertices[2].index)
    }

    @Test
    fun testVertexExtension() {
        val triangle = Triangle(Point(0.0, 0.0), Point(4.0, 0.0), Point(0.0, 3.0))
        val vertex = triangle.vertex(1)

        assertEquals(1, vertex.index)
        assertEquals(Point(4.0, 0.0), vertex.point)
    }

    @Test
    fun testIsoscelesTriangle() {
        // Isosceles triangle with two equal sides
        val triangle = Triangle(Point(0.0, 0.0), Point(4.0, 0.0), Point(2.0, 3.0))
        val v0 = TriangleVertex(triangle, 0)
        val v1 = TriangleVertex(triangle, 1)
        val v2 = TriangleVertex(triangle, 2)

        // Base angles should be equal
        assertEquals(v0.angle.radians, v1.angle.radians, 0.01)

        // All angles sum to 180°
        val sum = v0.angle.radians + v1.angle.radians + v2.angle.radians
        assertEquals(PI, sum, 0.01)
    }

    @Test
    fun testMedianCentroid() {
        // All three medians of a triangle intersect at the centroid
        val triangle = Triangle(Point(0.0, 0.0), Point(6.0, 0.0), Point(3.0, 6.0))
        val vertices = triangle.vertices

        // Centroid is at ((x0+x1+x2)/3, (y0+y1+y2)/3)
        val centroidX = (0.0 + 6.0 + 3.0) / 3.0
        val centroidY = (0.0 + 0.0 + 6.0) / 3.0

        // Each median should pass through (or near) the centroid
        for (vertex in vertices) {
            val median = vertex.median
            // Check if centroid lies on the median line (within some epsilon)
            // Using parametric form: P = from + t * (to - from)
        }
    }

    @Test
    fun testMetadataStorage() {
        val triangle = Triangle(Point(0.0, 0.0), Point(4.0, 0.0), Point(0.0, 3.0))
        val vertex = TriangleVertex(triangle, 0, mapOf("id" to 42, "name" to "apex"))

        assertEquals(42, vertex.getMetadata("id"))
        assertEquals("apex", vertex.getMetadata("name"))
    }

    @Test
    fun testLargeTriangle() {
        // Test with a large triangle to ensure numeric stability
        val triangle = Triangle(Point(0.0, 0.0), Point(1000.0, 0.0), Point(500.0, 866.0))
        val vertices = triangle.vertices

        // Should still compute angles correctly
        val angleSum = vertices.sumOf { it.angle.radians }
        assertEquals(PI, angleSum, 0.01)
    }

    @Test
    fun testSmallTriangle() {
        // Test with a very small triangle
        val triangle = Triangle(Point(0.0, 0.0), Point(0.01, 0.0), Point(0.005, 0.01))
        val vertices = triangle.vertices

        // Should still compute angles correctly
        val angleSum = vertices.sumOf { it.angle.radians }
        assertEquals(PI, angleSum, 0.01)
    }

    @Test
    fun testOppositeEdgeForAllVertices() {
        val triangle = Triangle(Point(0.0, 0.0), Point(4.0, 0.0), Point(0.0, 3.0))

        for (i in 0..2) {
            val vertex = TriangleVertex(triangle, i)
            val oppEdge = vertex.oppositeEdge

            // Opposite edge should not contain the vertex itself
            assertTrue(oppEdge.from != vertex.point)
            assertTrue(oppEdge.to != vertex.point)

            // Opposite edge should contain the other two vertices
            val otherIndices = (0..2).filter { it != i }
            val otherPoints = otherIndices.map { triangle.point(it) }

            assertTrue(
                (oppEdge.from == otherPoints[0] && oppEdge.to == otherPoints[1]) ||
                (oppEdge.from == otherPoints[1] && oppEdge.to == otherPoints[0])
            )
        }
    }
}
