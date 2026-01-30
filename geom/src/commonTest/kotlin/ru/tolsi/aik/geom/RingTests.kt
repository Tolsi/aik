package ru.tolsi.aik.geom

import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RingTests {

    @Test
    fun testConstructorWithCoordinates() {
        val ring = Ring(0.0, 0.0, 5.0, 10.0)
        assertEquals(0.0, ring.x)
        assertEquals(0.0, ring.y)
        assertEquals(5.0, ring.innerRadius)
        assertEquals(10.0, ring.outerRadius)
    }

    @Test
    fun testConstructorWithPoint() {
        val center = Point(5.0, 5.0)
        val ring = Ring(center, 3.0, 8.0)
        assertEquals(5.0, ring.center.x)
        assertEquals(5.0, ring.center.y)
    }

    @Test
    fun testThinRing() {
        val ring = Ring(0.0, 0.0, 9.0, 10.0)
        assertEquals(1.0, ring.width)
    }

    @Test
    fun testThickRing() {
        val ring = Ring(0.0, 0.0, 2.0, 10.0)
        assertEquals(8.0, ring.width)
    }

    @Test
    fun testDegenerateToCircle() {
        // Inner radius = 0 should work (becomes a circle)
        val ring = Ring(0.0, 0.0, 0.0, 10.0)
        assertEquals(0.0, ring.innerRadius)
        assertEquals(10.0, ring.outerRadius)
    }

    @Test
    fun testValidationOuterSmallerThanInner() {
        assertFails {
            Ring(0.0, 0.0, 10.0, 5.0)
        }
    }

    @Test
    fun testValidationOuterEqualsInner() {
        assertFails {
            Ring(0.0, 0.0, 10.0, 10.0)
        }
    }

    @Test
    fun testValidationNegativeInnerRadius() {
        assertFails {
            Ring(0.0, 0.0, -5.0, 10.0)
        }
    }

    @Test
    fun testAreaThinRing() {
        val ring = Ring(0.0, 0.0, 9.0, 10.0)
        // Area = π(R² - r²) = π(100 - 81) = 19π
        val expected = PI * (100 - 81)
        assertEquals(expected, ring.area, 0.01)
    }

    @Test
    fun testAreaThickRing() {
        val ring = Ring(0.0, 0.0, 2.0, 10.0)
        // Area = π(R² - r²) = π(100 - 4) = 96π
        val expected = PI * (100 - 4)
        assertEquals(expected, ring.area, 0.01)
    }

    @Test
    fun testAreaZeroInnerRadius() {
        val ring = Ring(0.0, 0.0, 0.0, 10.0)
        // Area = π * R² = π * 100
        val expected = PI * 100
        assertEquals(expected, ring.area, 0.01)
    }

    @Test
    fun testContainsPointInsideRing() {
        val ring = Ring(0.0, 0.0, 5.0, 10.0)
        // Point at distance 7 from center
        assertTrue(ring.containsPoint(7.0, 0.0))
    }

    @Test
    fun testContainsPointOnOuterBoundary() {
        val ring = Ring(0.0, 0.0, 5.0, 10.0)
        assertTrue(ring.containsPoint(10.0, 0.0))
        assertTrue(ring.containsPoint(0.0, 10.0))
    }

    @Test
    fun testContainsPointOnInnerBoundary() {
        val ring = Ring(0.0, 0.0, 5.0, 10.0)
        assertTrue(ring.containsPoint(5.0, 0.0))
        assertTrue(ring.containsPoint(0.0, 5.0))
    }

    @Test
    fun testContainsPointInsideInnerCircle() {
        val ring = Ring(0.0, 0.0, 5.0, 10.0)
        // Point at distance 3 from center
        assertFalse(ring.containsPoint(3.0, 0.0))
    }

    @Test
    fun testContainsPointOutsideOuterCircle() {
        val ring = Ring(0.0, 0.0, 5.0, 10.0)
        // Point at distance 12 from center
        assertFalse(ring.containsPoint(12.0, 0.0))
    }

    @Test
    fun testContainsPointAtCenter() {
        val ring = Ring(0.0, 0.0, 5.0, 10.0)
        // Center is inside inner circle
        assertFalse(ring.containsPoint(0.0, 0.0))
    }

    @Test
    fun testContainsPointDifferentAngles() {
        val ring = Ring(0.0, 0.0, 5.0, 10.0)
        // Distance 7, various angles
        assertTrue(ring.containsPoint(7.0, 0.0))    // 0°
        assertTrue(ring.containsPoint(0.0, 7.0))    // 90°
        assertTrue(ring.containsPoint(-7.0, 0.0))   // 180°
        assertTrue(ring.containsPoint(0.0, -7.0))   // 270°
    }

    @Test
    fun testPointsCount() {
        val ring = Ring(0.0, 0.0, 5.0, 10.0, totalPoints = 16)
        // 16 points for outer circle + 16 for inner circle = 32 total
        assertEquals(32, ring.points.size)
    }

    @Test
    fun testClosedProperty() {
        val ring = Ring(0.0, 0.0, 5.0, 10.0)
        assertTrue(ring.closed)
    }

    @Test
    fun testIntVariant() {
        val ringInt = RingInt(5, 10, 3, 8)
        assertEquals(5, ringInt.x)
        assertEquals(10, ringInt.y)
        assertEquals(3, ringInt.innerRadius)
        assertEquals(8, ringInt.outerRadius)
        assertEquals(5, ringInt.width)
        assertEquals(PointInt(5, 10), ringInt.center)
    }

    @Test
    fun testIntConversion() {
        val ring = Ring(5.7, 10.3, 3.2, 8.9)
        val ringInt = ring.int
        assertEquals(5, ringInt.x)
        assertEquals(10, ringInt.y)
        assertEquals(3, ringInt.innerRadius)
        assertEquals(8, ringInt.outerRadius)
    }

    @Test
    fun testFloatConversion() {
        val ringInt = RingInt(5, 10, 3, 8)
        val ringFloat = ringInt.float
        assertEquals(5.0, ringFloat.center.x)
        assertEquals(10.0, ringFloat.center.y)
        assertEquals(3.0, ringFloat.innerRadius)
        assertEquals(8.0, ringFloat.outerRadius)
    }
}
