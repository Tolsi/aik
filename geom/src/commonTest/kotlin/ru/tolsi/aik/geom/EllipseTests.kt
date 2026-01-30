package ru.tolsi.aik.geom

import kotlin.math.PI
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EllipseTests {

    @Test
    fun testConstructorWithCoordinates() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 5.0)
        assertEquals(0.0, ellipse.x)
        assertEquals(0.0, ellipse.y)
        assertEquals(10.0, ellipse.semiMajorAxis)
        assertEquals(5.0, ellipse.semiMinorAxis)
        assertEquals(Angle.ZERO, ellipse.rotation)
    }

    @Test
    fun testConstructorWithPoint() {
        val center = Point(5.0, 5.0)
        val ellipse = Ellipse(center, 10.0, 5.0)
        assertEquals(5.0, ellipse.center.x)
        assertEquals(5.0, ellipse.center.y)
    }

    @Test
    fun testAxisAlignedEllipse() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 5.0, Angle.fromDegrees(0))
        assertEquals(0.0, ellipse.rotation.radians)
    }

    @Test
    fun testRotatedEllipse45Degrees() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 5.0, Angle.fromDegrees(45))
        assertEquals(PI / 4, ellipse.rotation.radians, 0.001)
    }

    @Test
    fun testRotatedEllipse90Degrees() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 5.0, Angle.fromDegrees(90))
        assertEquals(PI / 2, ellipse.rotation.radians, 0.001)
    }

    @Test
    fun testDegenerateToCircle() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 10.0)
        assertTrue(ellipse.isCircle)
        assertEquals(10.0, ellipse.semiMajorAxis)
        assertEquals(10.0, ellipse.semiMinorAxis)
    }

    @Test
    fun testNotCircle() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 5.0)
        assertFalse(ellipse.isCircle)
    }

    @Test
    fun testValidationMinorGreaterThanMajor() {
        assertFails {
            Ellipse(0.0, 0.0, 5.0, 10.0)
        }
    }

    @Test
    fun testValidationNegativeMinorAxis() {
        assertFails {
            Ellipse(0.0, 0.0, 10.0, -5.0)
        }
    }

    @Test
    fun testValidationZeroMinorAxis() {
        assertFails {
            Ellipse(0.0, 0.0, 10.0, 0.0)
        }
    }

    @Test
    fun testAreaAxisAligned() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 5.0)
        // Area = π * a * b = π * 10 * 5 = 50π
        val expected = PI * 50
        assertEquals(expected, ellipse.area, 0.01)
    }

    @Test
    fun testAreaRotated() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 5.0, Angle.fromDegrees(45))
        // Area doesn't change with rotation
        val expected = PI * 50
        assertEquals(expected, ellipse.area, 0.01)
    }

    @Test
    fun testAreaCircle() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 10.0)
        // Area = π * r²
        val expected = PI * 100
        assertEquals(expected, ellipse.area, 0.01)
    }

    @Test
    fun testFocalDistance() {
        val ellipse = Ellipse(0.0, 0.0, 5.0, 3.0)
        // c = sqrt(a² - b²) = sqrt(25 - 9) = sqrt(16) = 4
        val expected = 4.0
        assertEquals(expected, ellipse.focalDistance, 0.01)
    }

    @Test
    fun testFocalDistanceCircle() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 10.0)
        // For circle, c = 0
        assertEquals(0.0, ellipse.focalDistance, 0.01)
    }

    @Test
    fun testContainsPointCenterAxisAligned() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 5.0)
        assertTrue(ellipse.containsPoint(0.0, 0.0))
    }

    @Test
    fun testContainsPointOnMajorAxisAxisAligned() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 5.0)
        assertTrue(ellipse.containsPoint(10.0, 0.0))
        assertTrue(ellipse.containsPoint(-10.0, 0.0))
    }

    @Test
    fun testContainsPointOnMinorAxisAxisAligned() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 5.0)
        assertTrue(ellipse.containsPoint(0.0, 5.0))
        assertTrue(ellipse.containsPoint(0.0, -5.0))
    }

    @Test
    fun testContainsPointInsideAxisAligned() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 5.0)
        assertTrue(ellipse.containsPoint(5.0, 2.0))
    }

    @Test
    fun testContainsPointOutsideAxisAligned() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 5.0)
        assertFalse(ellipse.containsPoint(11.0, 0.0))
        assertFalse(ellipse.containsPoint(0.0, 6.0))
    }

    @Test
    fun testContainsPointCenterRotated() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 5.0, Angle.fromDegrees(45))
        assertTrue(ellipse.containsPoint(0.0, 0.0))
    }

    @Test
    fun testContainsPointRotated90Degrees() {
        // When rotated 90°, major axis becomes vertical
        val ellipse = Ellipse(0.0, 0.0, 10.0, 5.0, Angle.fromDegrees(90))
        // Point on what is now the vertical major axis
        assertTrue(ellipse.containsPoint(0.0, 10.0))
        assertTrue(ellipse.containsPoint(0.0, -10.0))
        // Point on what is now the horizontal minor axis
        assertTrue(ellipse.containsPoint(5.0, 0.0))
        assertTrue(ellipse.containsPoint(-5.0, 0.0))
    }

    @Test
    fun testContainsPointRotated45Degrees() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 5.0, Angle.fromDegrees(45))
        // Point on rotated major axis
        val dist = 10.0 / sqrt(2.0)
        assertTrue(ellipse.containsPoint(dist, dist))
    }

    @Test
    fun testContainsPointOutsideRotated() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 5.0, Angle.fromDegrees(45))
        // Far outside
        assertFalse(ellipse.containsPoint(15.0, 0.0))
    }

    @Test
    fun testContainsPointTranslatedCenter() {
        val ellipse = Ellipse(10.0, 10.0, 5.0, 3.0)
        assertTrue(ellipse.containsPoint(10.0, 10.0))  // Center
        assertTrue(ellipse.containsPoint(15.0, 10.0))  // On major axis
        assertTrue(ellipse.containsPoint(10.0, 13.0))  // On minor axis
        assertFalse(ellipse.containsPoint(0.0, 0.0))   // Far away
    }

    @Test
    fun testClosedProperty() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 5.0)
        assertTrue(ellipse.closed)
    }

    @Test
    fun testPointsCount() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 5.0, totalPoints = 32)
        assertEquals(32, ellipse.points.size)
    }

    @Test
    fun testIntVariant() {
        val ellipseInt = EllipseInt(5, 10, 8, 4)
        assertEquals(5, ellipseInt.x)
        assertEquals(10, ellipseInt.y)
        assertEquals(8, ellipseInt.semiMajorAxis)
        assertEquals(4, ellipseInt.semiMinorAxis)
        assertEquals(PointInt(5, 10), ellipseInt.center)
    }

    @Test
    fun testIntConversion() {
        val ellipse = Ellipse(5.7, 10.3, 8.9, 4.2)
        val ellipseInt = ellipse.int
        assertEquals(5, ellipseInt.x)
        assertEquals(10, ellipseInt.y)
        assertEquals(8, ellipseInt.semiMajorAxis)
        assertEquals(4, ellipseInt.semiMinorAxis)
    }

    @Test
    fun testFloatConversion() {
        val ellipseInt = EllipseInt(5, 10, 8, 4)
        val ellipseFloat = ellipseInt.float
        assertEquals(5.0, ellipseFloat.center.x)
        assertEquals(10.0, ellipseFloat.center.y)
        assertEquals(8.0, ellipseFloat.semiMajorAxis)
        assertEquals(4.0, ellipseFloat.semiMinorAxis)
    }

    @Test
    fun testCounterClockwiseOrientation() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 5.0)
        val points = ellipse.points

        var signedArea = 0.0
        var j = points.size - 1
        for (i in points.indices) {
            signedArea += (points.getX(j) + points.getX(i)) * (points.getY(j) - points.getY(i))
            j = i
        }
        signedArea /= 2.0

        assertTrue(
            signedArea > 0,
            "Ellipse must have counter-clockwise orientation (positive signed area), got $signedArea"
        )
    }

    @Test
    fun testClosureAndContinuity() {
        val ellipse = Ellipse(0.0, 0.0, 10.0, 5.0, totalPoints = 32)
        val points = ellipse.points

        assertTrue(points.size > 0, "Ellipse should have points")
        assertTrue(ellipse.closed, "Ellipse should be marked as closed")

        // Check continuity - no large gaps between consecutive points
        for (i in 0 until points.size) {
            val p1 = Point(points.getX(i), points.getY(i))
            val p2 = Point(points.getX((i + 1) % points.size), points.getY((i + 1) % points.size))
            val distance = p1.distanceTo(p2)

            // For ellipse, max gap should be reasonable fraction of perimeter
            assertTrue(
                distance < 5.0,
                "Gap too large between points $i and ${(i + 1) % points.size}: $distance"
            )
        }
    }
}
