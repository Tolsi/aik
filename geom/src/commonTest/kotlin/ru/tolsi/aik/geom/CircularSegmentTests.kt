package ru.tolsi.aik.geom

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CircularSegmentTests {

    @Test
    fun testConstructorWithCoordinates() {
        val segment = CircularSegment(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        assertEquals(0.0, segment.x)
        assertEquals(0.0, segment.y)
        assertEquals(10.0, segment.radius)
        assertEquals(0.0, segment.startAngle.radians, 0.001)
        assertEquals(PI / 2, segment.endAngle.radians, 0.001)
    }

    @Test
    fun testConstructorWithPoint() {
        val center = Point(5.0, 5.0)
        val segment = CircularSegment(
            center, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        assertEquals(5.0, segment.center.x)
        assertEquals(5.0, segment.center.y)
    }

    @Test
    fun testSmallSegment() {
        val segment = CircularSegment(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(30)
        )
        assertEquals(30 * PI / 180, segment.sweepAngle.radians, 0.001)
    }

    @Test
    fun testLargeSegment() {
        val segment = CircularSegment(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(270)
        )
        assertEquals(3 * PI / 2, segment.sweepAngle.radians, 0.001)
    }

    @Test
    fun testSemiCircle() {
        val segment = CircularSegment(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(180)
        )
        assertEquals(PI, segment.sweepAngle.radians, 0.001)
    }

    @Test
    fun testAreaSmallSegment() {
        val segment = CircularSegment(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(60)
        )
        // Area = r²(θ - sin(θ))/2
        val theta = PI / 3
        val expected = (100 * (theta - sin(theta))) / 2
        assertEquals(expected, segment.area, 0.01)
    }

    @Test
    fun testAreaSemiCircle() {
        val segment = CircularSegment(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(180)
        )
        // For semicircle: Area = πr²/2
        val expected = PI * 50
        assertEquals(expected, segment.area, 0.01)
    }

    @Test
    fun testChordLengthQuarter() {
        val segment = CircularSegment(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        // For 90°: chord = 2r*sin(45°) = 2*10*sin(π/4) = 20*√2/2 = 10√2
        val angle45 = PI / 4
        val expected = 2 * 10 * sin(angle45)
        assertEquals(expected, segment.chordLength, 0.01)
    }

    @Test
    fun testChordLengthSemiCircle() {
        val segment = CircularSegment(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(180)
        )
        // For semicircle: chord = diameter = 2r
        assertEquals(20.0, segment.chordLength, 0.01)
    }

    @Test
    fun testSegmentHeight() {
        val segment = CircularSegment(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        // For 90°: h = r(1 - cos(45°)) = 10(1 - √2/2)
        val angle45 = PI / 4
        val expected = 10 * (1 - cos(angle45))
        assertEquals(expected, segment.segmentHeight, 0.01)
    }

    @Test
    fun testSegmentHeightSemiCircle() {
        val segment = CircularSegment(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(180)
        )
        // For semicircle: h = r
        assertEquals(10.0, segment.segmentHeight, 0.01)
    }

    @Test
    fun testContainsPointInsideSegment() {
        val segment = CircularSegment(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        // Point on arc side of chord
        assertTrue(segment.containsPoint(7.0, 3.0))
    }

    @Test
    fun testContainsPointOutsideRadius() {
        val segment = CircularSegment(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        assertFalse(segment.containsPoint(15.0, 0.0))
    }

    @Test
    fun testContainsPointOutsideAngleRange() {
        val segment = CircularSegment(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        // Point at 135° angle (outside range)
        assertFalse(segment.containsPoint(-5.0, 5.0))
    }

    @Test
    fun testContainsPointOnWrongSideOfChord() {
        val segment = CircularSegment(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        // Point inside angle range and radius but on center side of chord
        // For small angles, points near center are on wrong side
        assertFalse(segment.containsPoint(1.0, 1.0))
    }

    @Test
    fun testContainsPointOnArc() {
        val segment = CircularSegment(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        // Point on the arc at 45°
        val angle45 = PI / 4
        val px = 10 * cos(angle45)
        val py = 10 * sin(angle45)
        assertTrue(segment.containsPoint(px, py))
    }

    @Test
    fun testCompareSectorAndSegmentAreas() {
        val sector = CircularSector(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        val segment = CircularSegment(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        // Segment area should be less than sector area
        assertTrue(segment.area < sector.area)
    }

    @Test
    fun testPointsDoesNotIncludeCenter() {
        val segment = CircularSegment(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90),
            totalPoints = 8
        )
        // First point should be on the arc, not the center
        val firstX = segment.points.getX(0)
        val firstY = segment.points.getY(0)
        val distanceFromCenter = hypot(firstX, firstY)
        assertEquals(10.0, distanceFromCenter, 0.01)
    }

    @Test
    fun testClosedProperty() {
        val segment = CircularSegment(
            0.0, 0.0, 10.0,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        assertTrue(segment.closed)
    }

    @Test
    fun testIntVariant() {
        val segmentInt = CircularSegmentInt(
            5, 10, 20,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        assertEquals(5, segmentInt.x)
        assertEquals(10, segmentInt.y)
        assertEquals(20, segmentInt.radius)
        assertEquals(PointInt(5, 10), segmentInt.center)
    }

    @Test
    fun testIntConversion() {
        val segment = CircularSegment(
            5.7, 10.3, 20.8,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        val segmentInt = segment.int
        assertEquals(5, segmentInt.x)
        assertEquals(10, segmentInt.y)
        assertEquals(20, segmentInt.radius)
    }

    @Test
    fun testFloatConversion() {
        val segmentInt = CircularSegmentInt(
            5, 10, 20,
            Angle.fromDegrees(0),
            Angle.fromDegrees(90)
        )
        val segmentFloat = segmentInt.float
        assertEquals(5.0, segmentFloat.center.x)
        assertEquals(10.0, segmentFloat.center.y)
        assertEquals(20.0, segmentFloat.radius)
    }
}
