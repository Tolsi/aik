package ru.tolsi.aik.geom

import ru.tolsi.aik.geom.math.almostEquals
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ParallelogramTests {
    @Test
    fun testConstruction() {
        val p0 = Point(0.0, 0.0)
        val edge1 = Point(4.0, 0.0)
        val edge2 = Point(1.0, 3.0)
        val parallelogram = Parallelogram(p0, edge1, edge2)

        assertEquals(0.0, parallelogram.p0.x)
        assertEquals(0.0, parallelogram.p0.y)
        assertEquals(4.0, parallelogram.p1.x)
        assertEquals(0.0, parallelogram.p1.y)
        assertEquals(5.0, parallelogram.p2.x)
        assertEquals(3.0, parallelogram.p2.y)
        assertEquals(1.0, parallelogram.p3.x)
        assertEquals(3.0, parallelogram.p3.y)
    }

    @Test
    fun testFromThreePoints() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(4.0, 0.0)
        val p3 = Point(1.0, 3.0)
        val parallelogram = Parallelogram.fromThreePoints(p0, p1, p3)

        assertEquals(0.0, parallelogram.p0.x)
        assertEquals(0.0, parallelogram.p0.y)
        assertEquals(4.0, parallelogram.p1.x)
        assertEquals(0.0, parallelogram.p1.y)
        assertEquals(5.0, parallelogram.p2.x)
        assertEquals(3.0, parallelogram.p2.y)
        assertEquals(1.0, parallelogram.p3.x)
        assertEquals(3.0, parallelogram.p3.y)
    }

    @Test
    fun testFromFourPoints() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(4.0, 0.0)
        val p2 = Point(5.0, 3.0)
        val p3 = Point(1.0, 3.0)
        val parallelogram = Parallelogram.fromFourPoints(p0, p1, p2, p3)

        assertEquals(0.0, parallelogram.p0.x)
        assertEquals(0.0, parallelogram.p0.y)
        assertEquals(4.0, parallelogram.p1.x)
        assertEquals(0.0, parallelogram.p1.y)
        assertEquals(5.0, parallelogram.p2.x)
        assertEquals(3.0, parallelogram.p2.y)
        assertEquals(1.0, parallelogram.p3.x)
        assertEquals(3.0, parallelogram.p3.y)
    }

    @Test
    fun testParallelEdgesValidation() {
        val p0 = Point(0.0, 0.0)
        val edge1 = Point(4.0, 0.0)
        val edge2 = Point(8.0, 0.0) // Parallel to edge1

        assertFailsWith<IllegalArgumentException> {
            Parallelogram(p0, edge1, edge2, validate = true)
        }
    }

    @Test
    fun testArea() {
        val p0 = Point(0.0, 0.0)
        val edge1 = Point(4.0, 0.0)
        val edge2 = Point(0.0, 3.0)
        val parallelogram = Parallelogram(p0, edge1, edge2)

        assertEquals(12.0, parallelogram.area)
    }

    @Test
    fun testAreaWithAngledEdges() {
        val p0 = Point(0.0, 0.0)
        val edge1 = Point(4.0, 0.0)
        val edge2 = Point(1.0, 3.0)
        val parallelogram = Parallelogram(p0, edge1, edge2)

        // Area = |cross product| = |4*3 - 0*1| = 12
        assertEquals(12.0, parallelogram.area)
    }

    @Test
    fun testContainsPoint() {
        val p0 = Point(0.0, 0.0)
        val edge1 = Point(4.0, 0.0)
        val edge2 = Point(0.0, 3.0)
        val parallelogram = Parallelogram(p0, edge1, edge2)

        assertTrue(parallelogram.containsPoint(2.0, 1.5))
        assertTrue(parallelogram.containsPoint(0.0, 0.0))
        assertTrue(parallelogram.containsPoint(4.0, 3.0))
    }

    @Test
    fun testCenter() {
        val p0 = Point(0.0, 0.0)
        val edge1 = Point(4.0, 0.0)
        val edge2 = Point(0.0, 4.0)
        val parallelogram = Parallelogram(p0, edge1, edge2)

        val center = parallelogram.center
        assertEquals(2.0, center.x)
        assertEquals(2.0, center.y)
    }

    @Test
    fun testSetTo() {
        val p0 = Point(0.0, 0.0)
        val edge1 = Point(4.0, 0.0)
        val edge2 = Point(0.0, 3.0)
        val parallelogram = Parallelogram(p0, edge1, edge2)

        val newP0 = Point(1.0, 1.0)
        val newEdge1 = Point(5.0, 0.0)
        val newEdge2 = Point(0.0, 5.0)
        parallelogram.setTo(newP0, newEdge1, newEdge2)

        assertEquals(1.0, parallelogram.p0.x)
        assertEquals(1.0, parallelogram.p0.y)
        assertTrue(almostEquals(5.0, parallelogram.edge1.x))
        assertTrue(almostEquals(5.0, parallelogram.edge2.y))
    }

    @Test
    fun testDisplaced() {
        val p0 = Point(0.0, 0.0)
        val edge1 = Point(4.0, 0.0)
        val edge2 = Point(0.0, 3.0)
        val parallelogram = Parallelogram(p0, edge1, edge2)

        val displaced = parallelogram.displaced(5.0, 5.0)
        assertEquals(5.0, displaced.p0.x)
        assertEquals(5.0, displaced.p0.y)
        assertEquals(4.0, displaced.edge1.x)
        assertEquals(3.0, displaced.edge2.y)
    }

    @Test
    fun testDisplace() {
        val p0 = Point(0.0, 0.0)
        val edge1 = Point(4.0, 0.0)
        val edge2 = Point(0.0, 3.0)
        val parallelogram = Parallelogram(p0, edge1, edge2)

        parallelogram.displace(5.0, 5.0)
        assertEquals(5.0, parallelogram.p0.x)
        assertEquals(5.0, parallelogram.p0.y)
    }

    @Test
    fun testPoints() {
        val p0 = Point(0.0, 0.0)
        val edge1 = Point(4.0, 0.0)
        val edge2 = Point(0.0, 3.0)
        val parallelogram = Parallelogram(p0, edge1, edge2)

        val points = parallelogram.points
        assertEquals(4, points.size)
    }

    @Test
    fun testIntVariant() {
        val p0 = Point(0.5, 0.7)
        val edge1 = Point(4.3, 0.2)
        val edge2 = Point(0.8, 3.9)
        val parallelogram = Parallelogram(p0, edge1, edge2)

        val parallelogramInt = parallelogram.int
        assertEquals(0, parallelogramInt.p0.x)
        assertEquals(0, parallelogramInt.p0.y)
        assertEquals(4, parallelogramInt.edge1.x)
        assertEquals(3, parallelogramInt.edge2.y)
    }

    @Test
    fun testIntVariantConstruction() {
        val p0 = PointInt(0, 0)
        val edge1 = PointInt(4, 0)
        val edge2 = PointInt(0, 3)
        val parallelogramInt = ParallelogramInt(p0, edge1, edge2)

        assertEquals(0, parallelogramInt.p0.x)
        assertEquals(0, parallelogramInt.p0.y)
        assertEquals(4, parallelogramInt.p1.x)
        assertEquals(0, parallelogramInt.p1.y)
        assertEquals(4, parallelogramInt.p2.x)
        assertEquals(3, parallelogramInt.p2.y)
        assertEquals(0, parallelogramInt.p3.x)
        assertEquals(3, parallelogramInt.p3.y)
    }

    @Test
    fun testIntToFloat() {
        val p0 = PointInt(0, 0)
        val edge1 = PointInt(4, 0)
        val edge2 = PointInt(0, 3)
        val parallelogramInt = ParallelogramInt(p0, edge1, edge2)

        val parallelogram = parallelogramInt.float
        assertEquals(0.0, parallelogram.p0.x)
        assertEquals(0.0, parallelogram.p0.y)
        assertEquals(4.0, parallelogram.edge1.x)
        assertEquals(3.0, parallelogram.edge2.y)
    }

    @Test
    fun testCounterClockwiseOrientation() {
        val parallelogram = Parallelogram(
            Point(0.0, 0.0),
            Point(4.0, 0.0),
            Point(0.0, 3.0)
        )
        val points = parallelogram.points

        var signedArea = 0.0
        var j = points.size - 1
        for (i in points.indices) {
            signedArea += (points.getX(j) + points.getX(i)) * (points.getY(j) - points.getY(i))
            j = i
        }
        signedArea /= 2.0

        assertTrue(
            signedArea > 0,
            "Parallelogram must have counter-clockwise orientation (positive signed area), got $signedArea"
        )
    }

    @Test
    fun testClosureAndContinuity() {
        val parallelogram = Parallelogram(
            Point(0.0, 0.0),
            Point(4.0, 0.0),
            Point(0.0, 3.0)
        )
        val points = parallelogram.points

        assertTrue(points.size == 4, "Parallelogram should have 4 points")
        assertTrue(parallelogram.closed, "Parallelogram should be marked as closed")

        for (i in 0 until points.size) {
            val p1 = Point(points.getX(i), points.getY(i))
            val p2 = Point(points.getX((i + 1) % points.size), points.getY((i + 1) % points.size))
            val distance = p1.distanceTo(p2)

            assertTrue(
                distance > Geometry.EPS,
                "Consecutive points should not be duplicates at indices $i and ${(i + 1) % points.size}"
            )
        }
    }
}
