package ru.tolsi.aik.geom

import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class CircleTests {
    @Test
    fun testConstructorBasic() {
        val circle = Circle(50.0, 50.0, 25.0)

        assertEquals(50.0, circle.x, 1e-9)
        assertEquals(50.0, circle.y, 1e-9)
        assertEquals(25.0, circle.radius, 1e-9)
        assertEquals(32, circle.totalPoints)
    }

    @Test
    fun testConstructorWithTotalPoints() {
        val circle = Circle(50.0, 50.0, 25.0, 64)

        assertEquals(64, circle.totalPoints)
    }

    @Test
    fun testConstructorOperatorInvoke() {
        val circle = Circle(50, 50, 25)

        assertEquals(50.0, circle.x, 1e-9)
        assertEquals(50.0, circle.y, 1e-9)
        assertEquals(25.0, circle.radius, 1e-9)
    }

    @Test
    fun testArea() {
        val circle = Circle(0.0, 0.0, 10.0)

        assertEquals(PI * 10.0 * 10.0, circle.area, 1e-9)
    }

    @Test
    fun testAreaSmall() {
        val circle = Circle(0.0, 0.0, 1.0)

        assertEquals(PI, circle.area, 1e-9)
    }

    @Test
    fun testClosed() {
        val circle = Circle(0.0, 0.0, 10.0)

        assertTrue(circle.closed)
    }

    @Test
    fun testPointsProperty() {
        val circle = Circle(0.0, 0.0, 10.0, 16)
        val points = circle.points

        assertEquals(16, points.size)
    }

    @Test
    fun testPointsPropertyLazy() {
        val circle = Circle(0.0, 0.0, 10.0, 32)

        // Access points twice to verify lazy initialization
        val points1 = circle.points
        val points2 = circle.points

        assertEquals(points1.size, points2.size)
        assertEquals(32, points1.size)
    }

    @Test
    fun testPointsPropertyDifferentCounts() {
        val circle16 = Circle(0.0, 0.0, 10.0, 16)
        val circle32 = Circle(0.0, 0.0, 10.0, 32)
        val circle64 = Circle(0.0, 0.0, 10.0, 64)

        assertEquals(16, circle16.points.size)
        assertEquals(32, circle32.points.size)
        assertEquals(64, circle64.points.size)
    }

    @Test
    fun testPointsOnCircumference() {
        val radius = 10.0
        val circle = Circle(0.0, 0.0, radius, 32)
        val points = circle.points

        // Check first point is on circumference
        val x0 = points.getX(0)
        val y0 = points.getY(0)
        val dist0 = kotlin.math.hypot(x0, y0)

        assertEquals(radius, dist0, 1e-9)

        // Check a few more points
        for (i in listOf(8, 16, 24)) {
            val x = points.getX(i)
            val y = points.getY(i)
            val dist = kotlin.math.hypot(x, y)
            assertEquals(radius, dist, 1e-9)
        }
    }

    @Test
    fun testCounterClockwiseOrientation() {
        val circle = Circle(0.0, 0.0, 10.0, 32)

        // Compute signed area
        fun signedArea(points: IPointArrayList): Double {
            var area = 0.0
            var j = points.size - 1
            for (i in points.indices) {
                area += (points.getX(j) + points.getX(i)) * (points.getY(j) - points.getY(i))
                j = i
            }
            return area / 2.0
        }

        val signedArea = signedArea(circle.points)

        // Circle with radius 10, 32 points: signed area ≈ -312.14 (clockwise orientation)
        assertEquals(-312.14, signedArea, 0.01)
    }

    @Test
    fun testContainsPointInside() {
        val circle = Circle(50.0, 50.0, 25.0)

        assertTrue(circle.containsPoint(50.0, 50.0))  // Center
        assertTrue(circle.containsPoint(60.0, 50.0))  // Inside
        assertTrue(circle.containsPoint(50.0, 60.0))  // Inside
    }

    @Test
    fun testContainsPointOutside() {
        val circle = Circle(50.0, 50.0, 25.0)

        assertFalse(circle.containsPoint(100.0, 100.0))
        assertFalse(circle.containsPoint(0.0, 0.0))
        assertFalse(circle.containsPoint(76.0, 50.0))  // Just beyond radius
    }

    @Test
    fun testContainsPointOnEdge() {
        val circle = Circle(0.0, 0.0, 10.0)

        // Point exactly at radius distance
        assertFalse(circle.containsPoint(10.0, 0.0))  // Uses < not <=
    }

    @Test
    fun testFirstPointLocation() {
        val circle = Circle(0.0, 0.0, 10.0, 32)
        val points = circle.points

        // First point should be at angle 0 (rightmost point)
        val x0 = points.getX(0)
        val y0 = points.getY(0)

        assertEquals(10.0, x0, 1e-9)
        assertEquals(0.0, y0, 1e-9)
    }

    @Test
    fun testCircleOffset() {
        val circle = Circle(100.0, 200.0, 50.0, 16)
        val points = circle.points

        // All points should be offset by center coordinates
        for (i in points.indices) {
            val x = points.getX(i)
            val y = points.getY(i)

            // Distance from center should be radius
            val dist = kotlin.math.hypot(x - 100.0, y - 200.0)
            assertEquals(50.0, dist, 1e-9)
        }
    }

    @Test
    fun testSmallCircle() {
        val circle = Circle(0.0, 0.0, 0.5, 8)

        assertEquals(0.5, circle.radius, 1e-9)
        assertEquals(8, circle.points.size)
        assertEquals(PI * 0.5 * 0.5, circle.area, 1e-9)
    }

    @Test
    fun testLargeCircle() {
        val circle = Circle(0.0, 0.0, 1000.0, 64)

        assertEquals(1000.0, circle.radius, 1e-9)
        assertEquals(64, circle.points.size)
        assertEquals(PI * 1000.0 * 1000.0, circle.area, 1e-9)
    }

    @Test
    fun testCircleWithMinimalPoints() {
        val circle = Circle(0.0, 0.0, 10.0, 3)

        assertEquals(3, circle.points.size)
        // Circle area = π * r² = π * 100 ≈ 314.159
        assertEquals(314.159, circle.area, 0.01)
    }

    @Test
    fun testCircleAsPolygon() {
        val circle = Circle(0.0, 0.0, 10.0, 32)

        // Circle implements IPolygon
        assertTrue(circle is IPolygon)
        assertTrue(circle.closed)
        assertEquals(32, circle.points.size)
    }
}
