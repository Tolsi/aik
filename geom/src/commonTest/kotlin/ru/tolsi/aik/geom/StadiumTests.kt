package ru.tolsi.aik.geom

import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StadiumTests {

    @Test
    fun testConstructorWithCoordinates() {
        val stadium = Stadium(0.0, 0.0, 20.0, 10.0)
        assertEquals(0.0, stadium.x)
        assertEquals(0.0, stadium.y)
        assertEquals(20.0, stadium.width)
        assertEquals(10.0, stadium.height)
    }

    @Test
    fun testConstructorWithPoint() {
        val center = Point(5.0, 5.0)
        val stadium = Stadium(center, 20.0, 10.0)
        assertEquals(5.0, stadium.center.x)
        assertEquals(5.0, stadium.center.y)
    }

    @Test
    fun testDegenerateToCircle() {
        val stadium = Stadium(0.0, 0.0, 10.0, 10.0)
        assertTrue(stadium.isCircle)
        assertEquals(10.0, stadium.width)
        assertEquals(10.0, stadium.height)
        assertEquals(5.0, stadium.radius)
        assertEquals(0.0, stadium.straightLength)
    }

    @Test
    fun testLongStadium() {
        val stadium = Stadium(0.0, 0.0, 30.0, 10.0)
        assertFalse(stadium.isCircle)
        assertEquals(5.0, stadium.radius)
        assertEquals(20.0, stadium.straightLength)
    }

    @Test
    fun testValidationWidthLessThanHeight() {
        assertFails {
            Stadium(0.0, 0.0, 5.0, 10.0)
        }
    }

    @Test
    fun testValidationNegativeHeight() {
        assertFails {
            Stadium(0.0, 0.0, 20.0, -10.0)
        }
    }

    @Test
    fun testValidationZeroHeight() {
        assertFails {
            Stadium(0.0, 0.0, 20.0, 0.0)
        }
    }

    @Test
    fun testAreaCircle() {
        val stadium = Stadium(0.0, 0.0, 10.0, 10.0)
        // Circle: πr² = π * 25 = 25π
        val expected = PI * 25
        assertEquals(expected, stadium.area, 0.01)
    }

    @Test
    fun testAreaLongStadium() {
        val stadium = Stadium(0.0, 0.0, 30.0, 10.0)
        // Area = πr² + 2r*L = π*25 + 2*5*20 = 25π + 200
        val expected = PI * 25 + 200
        assertEquals(expected, stadium.area, 0.01)
    }

    @Test
    fun testContainsPointCenter() {
        val stadium = Stadium(0.0, 0.0, 20.0, 10.0)
        assertTrue(stadium.containsPoint(0.0, 0.0))
    }

    @Test
    fun testContainsPointInRectangleRegion() {
        val stadium = Stadium(0.0, 0.0, 30.0, 10.0)
        // Inside the straight rectangular section
        assertTrue(stadium.containsPoint(5.0, 0.0))
        assertTrue(stadium.containsPoint(-5.0, 0.0))
        assertTrue(stadium.containsPoint(0.0, 4.0))
        assertTrue(stadium.containsPoint(0.0, -4.0))
    }

    @Test
    fun testContainsPointInLeftSemicircle() {
        val stadium = Stadium(0.0, 0.0, 30.0, 10.0)
        // Left semicircle center at (-10, 0), radius 5
        assertTrue(stadium.containsPoint(-13.0, 0.0))
        assertTrue(stadium.containsPoint(-10.0, 4.0))
    }

    @Test
    fun testContainsPointInRightSemicircle() {
        val stadium = Stadium(0.0, 0.0, 30.0, 10.0)
        // Right semicircle center at (10, 0), radius 5
        assertTrue(stadium.containsPoint(13.0, 0.0))
        assertTrue(stadium.containsPoint(10.0, 4.0))
    }

    @Test
    fun testContainsPointOnBoundary() {
        val stadium = Stadium(0.0, 0.0, 30.0, 10.0)
        // On top/bottom edges of rectangle
        assertTrue(stadium.containsPoint(0.0, 5.0))
        assertTrue(stadium.containsPoint(0.0, -5.0))
    }

    @Test
    fun testContainsPointOutsideAbove() {
        val stadium = Stadium(0.0, 0.0, 30.0, 10.0)
        assertFalse(stadium.containsPoint(0.0, 10.0))
    }

    @Test
    fun testContainsPointOutsideBelow() {
        val stadium = Stadium(0.0, 0.0, 30.0, 10.0)
        assertFalse(stadium.containsPoint(0.0, -10.0))
    }

    @Test
    fun testContainsPointOutsideLeftSemicircle() {
        val stadium = Stadium(0.0, 0.0, 30.0, 10.0)
        // Far left of left semicircle
        assertFalse(stadium.containsPoint(-20.0, 0.0))
    }

    @Test
    fun testContainsPointOutsideRightSemicircle() {
        val stadium = Stadium(0.0, 0.0, 30.0, 10.0)
        // Far right of right semicircle
        assertFalse(stadium.containsPoint(20.0, 0.0))
    }

    @Test
    fun testClosedProperty() {
        val stadium = Stadium(0.0, 0.0, 20.0, 10.0)
        assertTrue(stadium.closed)
    }

    @Test
    fun testPointsCount() {
        val stadium = Stadium(0.0, 0.0, 20.0, 10.0, totalPoints = 8)
        // 8 points per semicircle + 2 (start/end) = 18
        // Actually: (8+1) * 2 = 18
        assertTrue(stadium.points.size >= 16)
    }

    @Test
    fun testIntVariant() {
        val stadiumInt = StadiumInt(5, 10, 30, 10)
        assertEquals(5, stadiumInt.x)
        assertEquals(10, stadiumInt.y)
        assertEquals(30, stadiumInt.width)
        assertEquals(10, stadiumInt.height)
        assertEquals(5, stadiumInt.radius)
        assertEquals(20, stadiumInt.straightLength)
        assertEquals(PointInt(5, 10), stadiumInt.center)
    }

    @Test
    fun testIntConversion() {
        val stadium = Stadium(5.7, 10.3, 30.9, 10.2)
        val stadiumInt = stadium.int
        assertEquals(5, stadiumInt.x)
        assertEquals(10, stadiumInt.y)
        assertEquals(30, stadiumInt.width)
        assertEquals(10, stadiumInt.height)
    }

    @Test
    fun testFloatConversion() {
        val stadiumInt = StadiumInt(5, 10, 30, 10)
        val stadiumFloat = stadiumInt.float
        assertEquals(5.0, stadiumFloat.center.x)
        assertEquals(10.0, stadiumFloat.center.y)
        assertEquals(30.0, stadiumFloat.width)
        assertEquals(10.0, stadiumFloat.height)
    }

    @Test
    fun testCounterClockwiseOrientation() {
        val stadium = Stadium(0.0, 0.0, 30.0, 10.0, totalPoints = 16)
        val points = stadium.points

        // Calculate signed area using shoelace formula
        var signedArea = 0.0
        var j = points.size - 1
        for (i in points.indices) {
            signedArea += (points.getX(j) + points.getX(i)) * (points.getY(j) - points.getY(i))
            j = i
        }
        signedArea /= 2.0

        // Positive signed area means counter-clockwise orientation
        assertTrue(
            signedArea > 0,
            "Stadium must have counter-clockwise orientation (positive signed area), got $signedArea"
        )
    }

    @Test
    fun testNoGapsInPoints() {
        val stadium = Stadium(0.0, 0.0, 30.0, 10.0, totalPoints = 16)
        val points = stadium.points

        // Check that we have points
        assertTrue(points.size > 0, "Stadium should have points")

        val radius = stadium.radius
        val straightLength = stadium.straightLength

        // Check consecutive points
        for (i in 0 until points.size) {
            val p1 = Point(points.getX(i), points.getY(i))
            val p2 = Point(points.getX((i + 1) % points.size), points.getY((i + 1) % points.size))
            val distance = p1.distanceTo(p2)
            val dx = kotlin.math.abs(p2.x - p1.x)
            val dy = kotlin.math.abs(p2.y - p1.y)

            // Check no duplicate consecutive points
            assertFalse(
                distance < Geometry.EPS,
                "Duplicate consecutive points at indices $i and ${(i + 1) % points.size}"
            )

            // For stadium, straight edges should be approximately horizontal with length ~2*straightLength
            // Curved sections should have smaller gaps
            if (dx > straightLength * 0.9) {
                // This is a straight edge (horizontal connection between semicircles)
                assertTrue(
                    dy < radius * 0.2,
                    "Straight edge at $i should be mostly horizontal, but dy=$dy is too large"
                )
                assertTrue(
                    distance <= 2 * straightLength + 1.0,
                    "Straight edge gap too large: $distance"
                )
            } else {
                // This is part of a curved section
                val expectedMaxCurveGap = (PI * radius) / (points.size / 2) * 1.5 // Allow 1.5x average on curves
                assertTrue(
                    distance < expectedMaxCurveGap,
                    "Curve gap too large between points $i and ${(i + 1) % points.size}: $distance (expected < $expectedMaxCurveGap)"
                )
            }
        }
    }
}
