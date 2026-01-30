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
}
