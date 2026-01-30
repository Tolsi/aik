package ru.tolsi.aik.geom

import ru.tolsi.aik.geom.math.almostEquals
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class KiteTests {
    @Test
    fun testConstruction() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(3.0, 4.0)
        val p2 = Point(0.0, 8.0)
        val p3 = Point(-3.0, 4.0)
        val kite = Kite(p0, p1, p2, p3)

        assertEquals(0.0, kite.p0.x)
        assertEquals(0.0, kite.p0.y)
        assertEquals(3.0, kite.p1.x)
        assertEquals(4.0, kite.p1.y)
        assertEquals(0.0, kite.p2.x)
        assertEquals(8.0, kite.p2.y)
        assertEquals(-3.0, kite.p3.x)
        assertEquals(4.0, kite.p3.y)
    }

    @Test
    fun testAdjacentEqualSidesValidation() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(3.0, 4.0)
        val p2 = Point(0.0, 8.0)
        val p3 = Point(-3.0, 4.0)
        val kite = Kite(p0, p1, p2, p3, validate = true)

        val s01 = kite.p0.distanceTo(kite.p1)
        val s12 = kite.p1.distanceTo(kite.p2)
        val s23 = kite.p2.distanceTo(kite.p3)
        val s30 = kite.p3.distanceTo(kite.p0)

        // Should have two pairs of adjacent equal sides
        assertTrue(almostEquals(s01, s12))
        assertTrue(almostEquals(s23, s30))
    }

    @Test
    fun testInvalidKiteValidation() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(3.0, 4.0)
        val p2 = Point(6.0, 8.0)
        val p3 = Point(-3.0, 4.0)

        assertFailsWith<IllegalArgumentException> {
            Kite(p0, p1, p2, p3, validate = true)
        }
    }

    @Test
    fun testArea() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(3.0, 4.0)
        val p2 = Point(0.0, 8.0)
        val p3 = Point(-3.0, 4.0)
        val kite = Kite(p0, p1, p2, p3)

        // Area = (d1 * d2) / 2
        // d1 = distance from p0 to p2 = 8
        // d2 = distance from p1 to p3 = 6
        // Area = (8 * 6) / 2 = 24
        assertTrue(almostEquals(24.0, kite.area))
    }

    @Test
    fun testDiagonals() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(3.0, 4.0)
        val p2 = Point(0.0, 8.0)
        val p3 = Point(-3.0, 4.0)
        val kite = Kite(p0, p1, p2, p3)

        assertEquals(8.0, kite.diagonal1)
        assertEquals(6.0, kite.diagonal2)
    }

    @Test
    fun testAxis() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(3.0, 4.0)
        val p2 = Point(0.0, 8.0)
        val p3 = Point(-3.0, 4.0)
        val kite = Kite(p0, p1, p2, p3)

        val axis = kite.axis
        assertEquals(0.0, axis.x)
        assertEquals(8.0, axis.y)
    }

    @Test
    fun testIsConvex() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(3.0, 4.0)
        val p2 = Point(0.0, 8.0)
        val p3 = Point(-3.0, 4.0)
        val kite = Kite(p0, p1, p2, p3)

        assertTrue(kite.isConvex)
    }

    @Test
    fun testFromDiagonals() {
        val center = Point(0.0, 4.0)
        val kite = Kite.fromDiagonals(center, 8.0, 6.0, 0.0)

        assertTrue(almostEquals(8.0, kite.diagonal1))
        assertTrue(almostEquals(6.0, kite.diagonal2))
    }

    @Test
    fun testFromDiagonalsWithOffset() {
        val center = Point(0.0, 4.0)
        val kite = Kite.fromDiagonals(center, 8.0, 6.0, 1.0)

        assertTrue(almostEquals(8.0, kite.diagonal1))
        assertTrue(almostEquals(6.0, kite.diagonal2))
    }

    @Test
    fun testFromAxisAndSides() {
        val apex1 = Point(0.0, 0.0)
        val apex2 = Point(0.0, 8.0)
        val kite = Kite.fromAxisAndSides(apex1, apex2, 5.0, 5.0)

        val s01 = kite.p0.distanceTo(kite.p1)
        val s12 = kite.p1.distanceTo(kite.p2)
        val s23 = kite.p2.distanceTo(kite.p3)
        val s30 = kite.p3.distanceTo(kite.p0)

        // Should have two pairs of adjacent equal sides
        assertTrue(almostEquals(s01, s12))
        assertTrue(almostEquals(s23, s30))
    }

    @Test
    fun testContainsPoint() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(3.0, 4.0)
        val p2 = Point(0.0, 8.0)
        val p3 = Point(-3.0, 4.0)
        val kite = Kite(p0, p1, p2, p3)

        assertTrue(kite.containsPoint(0.0, 4.0))
    }

    @Test
    fun testSetTo() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(3.0, 4.0)
        val p2 = Point(0.0, 8.0)
        val p3 = Point(-3.0, 4.0)
        val kite = Kite(p0, p1, p2, p3)

        val newP0 = Point(1.0, 1.0)
        val newP1 = Point(4.0, 5.0)
        val newP2 = Point(1.0, 9.0)
        val newP3 = Point(-2.0, 5.0)
        kite.setTo(newP0, newP1, newP2, newP3)

        assertEquals(1.0, kite.p0.x)
        assertEquals(1.0, kite.p0.y)
    }

    @Test
    fun testDisplaced() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(3.0, 4.0)
        val p2 = Point(0.0, 8.0)
        val p3 = Point(-3.0, 4.0)
        val kite = Kite(p0, p1, p2, p3)

        val displaced = kite.displaced(5.0, 5.0)
        assertEquals(5.0, displaced.p0.x)
        assertEquals(5.0, displaced.p0.y)
    }

    @Test
    fun testDisplace() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(3.0, 4.0)
        val p2 = Point(0.0, 8.0)
        val p3 = Point(-3.0, 4.0)
        val kite = Kite(p0, p1, p2, p3)

        kite.displace(5.0, 5.0)
        assertEquals(5.0, kite.p0.x)
        assertEquals(5.0, kite.p0.y)
    }

    @Test
    fun testPoints() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(3.0, 4.0)
        val p2 = Point(0.0, 8.0)
        val p3 = Point(-3.0, 4.0)
        val kite = Kite(p0, p1, p2, p3)

        val points = kite.points
        assertEquals(4, points.size)
    }

    @Test
    fun testIntVariant() {
        val p0 = Point(0.5, 0.7)
        val p1 = Point(3.3, 4.2)
        val p2 = Point(0.8, 8.9)
        val p3 = Point(-3.1, 4.6)
        val kite = Kite(p0, p1, p2, p3, validate = false)

        val kiteInt = kite.int
        assertEquals(0, kiteInt.p0.x)
        assertEquals(0, kiteInt.p0.y)
    }

    @Test
    fun testIntVariantConstruction() {
        val p0 = PointInt(0, 0)
        val p1 = PointInt(3, 4)
        val p2 = PointInt(0, 8)
        val p3 = PointInt(-3, 4)
        val kiteInt = KiteInt(p0, p1, p2, p3)

        assertEquals(0, kiteInt.p0.x)
        assertEquals(0, kiteInt.p0.y)
        assertEquals(3, kiteInt.p1.x)
        assertEquals(4, kiteInt.p1.y)
    }

    @Test
    fun testIntVariantFromDiagonals() {
        val center = PointInt(0, 4)
        val kiteInt = KiteInt.fromDiagonals(center, 8, 6, 0)

        assertEquals(8, kiteInt.diagonal1)
        assertEquals(6, kiteInt.diagonal2)
    }

    @Test
    fun testIntToFloat() {
        val p0 = PointInt(0, 0)
        val p1 = PointInt(3, 4)
        val p2 = PointInt(0, 8)
        val p3 = PointInt(-3, 4)
        val kiteInt = KiteInt(p0, p1, p2, p3)

        val kite = kiteInt.float
        assertEquals(0.0, kite.p0.x)
        assertEquals(0.0, kite.p0.y)
    }

    @Test
    fun testCounterClockwiseOrientation() {
        val kite = Kite(
            Point(0.0, 0.0),
            Point(3.0, 4.0),
            Point(0.0, 8.0),
            Point(-3.0, 4.0)
        )
        val points = kite.points

        var signedArea = 0.0
        var j = points.size - 1
        for (i in points.indices) {
            signedArea += (points.getX(j) + points.getX(i)) * (points.getY(j) - points.getY(i))
            j = i
        }
        signedArea /= 2.0

        assertTrue(
            signedArea > 0,
            "Kite must have counter-clockwise orientation (positive signed area), got $signedArea"
        )
    }

    @Test
    fun testClosureAndContinuity() {
        val kite = Kite(
            Point(0.0, 0.0),
            Point(3.0, 4.0),
            Point(0.0, 8.0),
            Point(-3.0, 4.0)
        )
        val points = kite.points

        assertTrue(points.size == 4, "Kite should have 4 points")
        assertTrue(kite.closed, "Kite should be marked as closed")

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
