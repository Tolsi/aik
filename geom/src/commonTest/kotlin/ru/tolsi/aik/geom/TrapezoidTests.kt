package ru.tolsi.aik.geom

import ru.tolsi.aik.geom.math.almostEquals
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TrapezoidTests {
    @Test
    fun testConstruction() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(4.0, 0.0)
        val p2 = Point(3.0, 3.0)
        val p3 = Point(1.0, 3.0)
        val trapezoid = Trapezoid(p0, p1, p2, p3)

        assertEquals(0.0, trapezoid.p0.x)
        assertEquals(0.0, trapezoid.p0.y)
        assertEquals(4.0, trapezoid.p1.x)
        assertEquals(0.0, trapezoid.p1.y)
        assertEquals(3.0, trapezoid.p2.x)
        assertEquals(3.0, trapezoid.p2.y)
        assertEquals(1.0, trapezoid.p3.x)
        assertEquals(3.0, trapezoid.p3.y)
    }

    @Test
    fun testParallelSidesValidation() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(4.0, 0.0)
        val p2 = Point(3.0, 3.0)
        val p3 = Point(0.0, 2.0) // No parallel sides

        assertFailsWith<IllegalArgumentException> {
            Trapezoid(p0, p1, p2, p3, validate = true)
        }
    }

    @Test
    fun testArea() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(4.0, 0.0)
        val p2 = Point(3.0, 3.0)
        val p3 = Point(1.0, 3.0)
        val trapezoid = Trapezoid(p0, p1, p2, p3)

        // Area using Shoelace formula
        val expectedArea = 9.0 // (base1 + base2) * height / 2 = (4 + 2) * 3 / 2 = 9
        assertTrue(almostEquals(expectedArea, trapezoid.area))
    }

    @Test
    fun testBaseLengths() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(4.0, 0.0)
        val p2 = Point(3.0, 3.0)
        val p3 = Point(1.0, 3.0)
        val trapezoid = Trapezoid(p0, p1, p2, p3)

        assertEquals(4.0, trapezoid.base1Length)
        assertEquals(2.0, trapezoid.base2Length)
    }

    @Test
    fun testHeight() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(4.0, 0.0)
        val p2 = Point(3.0, 3.0)
        val p3 = Point(1.0, 3.0)
        val trapezoid = Trapezoid(p0, p1, p2, p3)

        assertEquals(3.0, trapezoid.height)
    }

    @Test
    fun testIsIsosceles() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(4.0, 0.0)
        val p2 = Point(3.0, 3.0)
        val p3 = Point(1.0, 3.0)
        val trapezoid = Trapezoid(p0, p1, p2, p3)

        assertTrue(trapezoid.isIsosceles)
    }

    @Test
    fun testFromBasesHorizontal() {
        val base1Start = Point(0.0, 0.0)
        val base1End = Point(4.0, 0.0)
        val trapezoid = Trapezoid.fromBases(base1Start, base1End, 2.0, 3.0, 1.0)

        assertEquals(0.0, trapezoid.p0.x)
        assertEquals(0.0, trapezoid.p0.y)
        assertEquals(4.0, trapezoid.p1.x)
        assertEquals(0.0, trapezoid.p1.y)
        assertEquals(3.0, trapezoid.p2.x)
        assertEquals(3.0, trapezoid.p2.y)
        assertEquals(1.0, trapezoid.p3.x)
        assertEquals(3.0, trapezoid.p3.y)
    }

    @Test
    fun testIsoscelesConstructor() {
        val center = Point(2.0, 0.0)
        val trapezoid = Trapezoid.isosceles(center, 4.0, 2.0, 3.0)

        assertEquals(0.0, trapezoid.p0.x)
        assertEquals(0.0, trapezoid.p0.y)
        assertEquals(4.0, trapezoid.p1.x)
        assertEquals(0.0, trapezoid.p1.y)
        assertEquals(3.0, trapezoid.p2.x)
        assertEquals(3.0, trapezoid.p2.y)
        assertEquals(1.0, trapezoid.p3.x)
        assertEquals(3.0, trapezoid.p3.y)
        assertTrue(trapezoid.isIsosceles)
    }

    @Test
    fun testContainsPoint() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(4.0, 0.0)
        val p2 = Point(3.0, 3.0)
        val p3 = Point(1.0, 3.0)
        val trapezoid = Trapezoid(p0, p1, p2, p3)

        assertTrue(trapezoid.containsPoint(2.0, 1.5))
    }

    @Test
    fun testSetTo() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(4.0, 0.0)
        val p2 = Point(3.0, 3.0)
        val p3 = Point(1.0, 3.0)
        val trapezoid = Trapezoid(p0, p1, p2, p3)

        val newP0 = Point(1.0, 1.0)
        val newP1 = Point(5.0, 1.0)
        val newP2 = Point(4.0, 4.0)
        val newP3 = Point(2.0, 4.0)
        trapezoid.setTo(newP0, newP1, newP2, newP3)

        assertEquals(1.0, trapezoid.p0.x)
        assertEquals(1.0, trapezoid.p0.y)
    }

    @Test
    fun testDisplaced() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(4.0, 0.0)
        val p2 = Point(3.0, 3.0)
        val p3 = Point(1.0, 3.0)
        val trapezoid = Trapezoid(p0, p1, p2, p3)

        val displaced = trapezoid.displaced(5.0, 5.0)
        assertEquals(5.0, displaced.p0.x)
        assertEquals(5.0, displaced.p0.y)
    }

    @Test
    fun testDisplace() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(4.0, 0.0)
        val p2 = Point(3.0, 3.0)
        val p3 = Point(1.0, 3.0)
        val trapezoid = Trapezoid(p0, p1, p2, p3)

        trapezoid.displace(5.0, 5.0)
        assertEquals(5.0, trapezoid.p0.x)
        assertEquals(5.0, trapezoid.p0.y)
    }

    @Test
    fun testPoints() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(4.0, 0.0)
        val p2 = Point(3.0, 3.0)
        val p3 = Point(1.0, 3.0)
        val trapezoid = Trapezoid(p0, p1, p2, p3)

        val points = trapezoid.points
        assertEquals(4, points.size)
    }

    @Test
    fun testIntVariant() {
        val p0 = Point(0.5, 0.7)
        val p1 = Point(4.3, 0.2)
        val p2 = Point(3.8, 3.9)
        val p3 = Point(1.1, 3.6)
        val trapezoid = Trapezoid(p0, p1, p2, p3, validate = false)

        val trapezoidInt = trapezoid.int
        assertEquals(0, trapezoidInt.p0.x)
        assertEquals(0, trapezoidInt.p0.y)
        assertEquals(4, trapezoidInt.p1.x)
        assertEquals(0, trapezoidInt.p1.y)
    }

    @Test
    fun testIntVariantConstruction() {
        val p0 = PointInt(0, 0)
        val p1 = PointInt(4, 0)
        val p2 = PointInt(3, 3)
        val p3 = PointInt(1, 3)
        val trapezoidInt = TrapezoidInt(p0, p1, p2, p3)

        assertEquals(0, trapezoidInt.p0.x)
        assertEquals(0, trapezoidInt.p0.y)
        assertEquals(4, trapezoidInt.p1.x)
        assertEquals(0, trapezoidInt.p1.y)
    }

    @Test
    fun testIntVariantIsosceles() {
        val center = PointInt(2, 0)
        val trapezoidInt = TrapezoidInt.isosceles(center, 4, 2, 3)

        assertEquals(0, trapezoidInt.p0.x)
        assertEquals(0, trapezoidInt.p0.y)
        assertEquals(4, trapezoidInt.p1.x)
        assertEquals(0, trapezoidInt.p1.y)
        assertTrue(trapezoidInt.isIsosceles)
    }

    @Test
    fun testIntToFloat() {
        val p0 = PointInt(0, 0)
        val p1 = PointInt(4, 0)
        val p2 = PointInt(3, 3)
        val p3 = PointInt(1, 3)
        val trapezoidInt = TrapezoidInt(p0, p1, p2, p3)

        val trapezoid = trapezoidInt.float
        assertEquals(0.0, trapezoid.p0.x)
        assertEquals(0.0, trapezoid.p0.y)
    }

    @Test
    fun testCounterClockwiseOrientation() {
        val trapezoid = Trapezoid(
            Point(0.0, 0.0),
            Point(4.0, 0.0),
            Point(3.0, 3.0),
            Point(1.0, 3.0)
        )
        val points = trapezoid.points

        var signedArea = 0.0
        var j = points.size - 1
        for (i in points.indices) {
            signedArea += (points.getX(j) + points.getX(i)) * (points.getY(j) - points.getY(i))
            j = i
        }
        signedArea /= 2.0

        assertTrue(
            signedArea > 0,
            "Trapezoid must have counter-clockwise orientation (positive signed area), got $signedArea"
        )
    }

    @Test
    fun testClosureAndContinuity() {
        val trapezoid = Trapezoid(
            Point(0.0, 0.0),
            Point(4.0, 0.0),
            Point(3.0, 3.0),
            Point(1.0, 3.0)
        )
        val points = trapezoid.points

        assertTrue(points.size == 4, "Trapezoid should have 4 points")
        assertTrue(trapezoid.closed, "Trapezoid should be marked as closed")

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
