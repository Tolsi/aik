package ru.tolsi.aik.geom

import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CircularSectorTests {

    @Test
    fun testConstructorWithCoordinates() {
        val sector = CircularSector(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        assertEquals(0.0, sector.x)
        assertEquals(0.0, sector.y)
        assertEquals(10.0, sector.radius)
        assertEquals(0.0, sector.startAngle.radians, 0.001)
        assertEquals(PI / 2, sector.endAngle.radians, 0.001)
    }

    @Test
    fun testConstructorWithPoint() {
        val center = Point(5.0, 5.0)
        val sector = CircularSector(
            center, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        assertEquals(5.0, sector.center.x)
        assertEquals(5.0, sector.center.y)
    }

    @Test
    fun testQuarterCircle() {
        val sector = CircularSector(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        assertEquals(PI / 2, sector.sweepAngle.radians, 0.001)
    }

    @Test
    fun testSemiCircle() {
        val sector = CircularSector(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(180)
        )
        assertEquals(PI, sector.sweepAngle.radians, 0.001)
    }

    @Test
    fun testThreeQuarterCircle() {
        val sector = CircularSector(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(270)
        )
        assertEquals(3 * PI / 2, sector.sweepAngle.radians, 0.001)
    }

    @Test
    fun testFullCircle() {
        val sector = CircularSector(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(360)
        )
        assertEquals(2 * PI, sector.sweepAngle.radians, 0.001)
    }

    @Test
    fun testAngleWrapping() {
        // Sector from 350° to 10° (wraps around 0)
        val sector = CircularSector(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(350),
            Angle.fromDegrees(10)
        )
        // Should be 20° sweep
        assertEquals(20 * PI / 180, sector.sweepAngle.radians, 0.001)
    }

    @Test
    fun testAreaQuarterCircle() {
        val sector = CircularSector(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        // Area = (θ * r²) / 2 = (π/2 * 100) / 2 = 25π
        val expected = (PI / 2 * 100) / 2
        assertEquals(expected, sector.area, 0.001)
    }

    @Test
    fun testAreaSemiCircle() {
        val sector = CircularSector(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(180)
        )
        // Area = (π * 100) / 2 = 50π
        val expected = (PI * 100) / 2
        assertEquals(expected, sector.area, 0.001)
    }

    @Test
    fun testAreaFullCircle() {
        val sector = CircularSector(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(360)
        )
        // Area = (2π * 100) / 2 = 100π
        val expected = PI * 100
        assertEquals(expected, sector.area, 0.001)
    }

    @Test
    fun testContainsPointInsideArc() {
        val sector = CircularSector(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        // Point at 45° angle, radius 5
        assertTrue(sector.containsPoint(3.5, 3.5))
    }

    @Test
    fun testContainsPointOutsideRadius() {
        val sector = CircularSector(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        assertFalse(sector.containsPoint(15.0, 0.0))
    }

    @Test
    fun testContainsPointOutsideAngleRange() {
        val sector = CircularSector(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        // Point at 135° angle (outside range)
        assertFalse(sector.containsPoint(-5.0, 5.0))
    }

    @Test
    fun testContainsPointOnRadius() {
        val sector = CircularSector(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        // Point on starting radius
        assertTrue(sector.containsPoint(5.0, 0.0))
    }

    @Test
    fun testContainsPointAtCenter() {
        val sector = CircularSector(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        assertTrue(sector.containsPoint(0.0, 0.0))
    }

    @Test
    fun testPointsIncludesCenter() {
        val sector = CircularSector(
            5.0, 5.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90),
            totalPoints = 8
        )
        // First point should be the center
        assertEquals(5.0, sector.points.getX(0), 0.001)
        assertEquals(5.0, sector.points.getY(0), 0.001)
    }

    @Test
    fun testClosedProperty() {
        val sector = CircularSector(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        assertTrue(sector.closed)
    }

    @Test
    fun testIntVariant() {
        val sectorInt = CircularSectorInt(
            5, 10, 20,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        assertEquals(5, sectorInt.x)
        assertEquals(10, sectorInt.y)
        assertEquals(20, sectorInt.radius)
        assertEquals(PointInt(5, 10), sectorInt.center)
    }

    @Test
    fun testIntConversion() {
        val sector = CircularSector(
            5.7, 10.3, 20.8,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        val sectorInt = sector.int
        assertEquals(5, sectorInt.x)
        assertEquals(10, sectorInt.y)
        assertEquals(20, sectorInt.radius)
    }

    @Test
    fun testFloatConversion() {
        val sectorInt = CircularSectorInt(
            5, 10, 20,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        val sectorFloat = sectorInt.float
        assertEquals(5.0, sectorFloat.center.x)
        assertEquals(10.0, sectorFloat.center.y)
        assertEquals(20.0, sectorFloat.radius)
    }

    @Test
    fun testWrappingAngleContainment() {
        // Sector from 350° to 10° (wraps around 0)
        val sector = CircularSector(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(350),
            Angle.fromDegrees(10)
        )
        // Point at 0° should be inside
        assertTrue(sector.containsPoint(5.0, 0.0))
        // Point at 180° should be outside
        assertFalse(sector.containsPoint(-5.0, 0.0))
    }
}
