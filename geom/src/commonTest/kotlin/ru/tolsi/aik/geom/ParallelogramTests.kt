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
}
