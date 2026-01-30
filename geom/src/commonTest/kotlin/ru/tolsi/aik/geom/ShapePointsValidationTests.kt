package ru.tolsi.aik.geom

import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ShapePointsValidationTests {

    private fun IPointArrayList.hasValidPoints(): Boolean {
        if (size == 0) return false
        for (i in 0 until size) {
            val x = getX(i)
            val y = getY(i)
            if (x.isNaN() || y.isNaN() || x.isInfinite() || y.isInfinite()) {
                return false
            }
        }
        return true
    }

    private fun IPointArrayList.hasNoDuplicateConsecutivePoints(): Boolean {
        if (size < 2) return true
        for (i in 0 until size - 1) {
            val x1 = getX(i)
            val y1 = getY(i)
            val x2 = getX(i + 1)
            val y2 = getY(i + 1)
            if (x1 == x2 && y1 == y2) {
                println("Duplicate points at indices $i and ${i+1}: ($x1, $y1)")
                return false
            }
        }
        return true
    }

    private fun IPolygon.isProperlyClosedWithoutDuplicates(): Boolean {
        // Check first and last points are NOT the same (polygon should auto-close)
        val firstX = points.getX(0)
        val firstY = points.getY(0)
        val lastX = points.getX(points.size - 1)
        val lastY = points.getY(points.size - 1)

        // If they're the same, that's a duplicate
        return !(firstX == lastX && firstY == lastY)
    }

    @Test
    fun testRectanglePointsValid() {
        val rect = Rectangle.fromBounds(10.0, 20.0, 30.0, 40.0)
        assertTrue(rect.points.hasValidPoints(), "Rectangle should have valid points")
        assertTrue(rect.points.hasNoDuplicateConsecutivePoints(), "Rectangle should not have duplicate points")
        assertTrue(rect.points.size == 4, "Rectangle should have 4 points")
    }

    @Test
    fun testCirclePointsValid() {
        val circle = Circle(10.0, 10.0, 5.0, totalPoints = 16)
        assertTrue(circle.points.hasValidPoints(), "Circle should have valid points")
        assertTrue(circle.points.hasNoDuplicateConsecutivePoints(), "Circle should not have duplicate points")
        assertTrue(circle.isProperlyClosedWithoutDuplicates(), "Circle should not have duplicate first/last point")
    }

    @Test
    fun testTrianglePointsValid() {
        val triangle = Triangle(Point(0.0, 0.0), Point(10.0, 0.0), Point(5.0, 10.0))
        assertTrue(triangle.points.hasValidPoints(), "Triangle should have valid points")
        assertTrue(triangle.points.hasNoDuplicateConsecutivePoints(), "Triangle should not have duplicate points")
        assertTrue(triangle.points.size == 3, "Triangle should have 3 points")
    }

    @Test
    fun testEllipsePointsValid() {
        val ellipse = Ellipse(10.0, 10.0, 5.0, 3.0, totalPoints = 32)
        assertTrue(ellipse.points.hasValidPoints(), "Ellipse should have valid points")
        assertTrue(ellipse.points.hasNoDuplicateConsecutivePoints(), "Ellipse should not have duplicate points")
        assertTrue(ellipse.isProperlyClosedWithoutDuplicates(), "Ellipse should not have duplicate first/last point")
    }

    @Test
    fun testStadiumPointsValid() {
        val stadium = Stadium(10.0, 10.0, width = 20.0, height = 10.0, totalPoints = 16)
        assertTrue(stadium.points.hasValidPoints(), "Stadium should have valid points")
        assertTrue(stadium.points.hasNoDuplicateConsecutivePoints(), "Stadium should not have duplicate points")
        assertTrue(stadium.isProperlyClosedWithoutDuplicates(), "Stadium should not have duplicate first/last point")
    }

    @Test
    fun testRingPointsValid() {
        val ring = Ring(10.0, 10.0, 3.0, 7.0, totalPoints = 32)
        assertTrue(ring.points.hasValidPoints(), "Ring should have valid points")
        assertTrue(ring.points.hasNoDuplicateConsecutivePoints(), "Ring should not have duplicate points")
        assertTrue(ring.isProperlyClosedWithoutDuplicates(), "Ring should not have duplicate first/last point")
    }

    @Test
    fun testCircularSectorPointsValid() {
        val sector = CircularSector(10.0, 10.0, 5.0,
            Angle.fromDegrees(0), Angle.fromDegrees(90), totalPoints = 16)
        assertTrue(sector.points.hasValidPoints(), "CircularSector should have valid points")
        assertTrue(sector.points.hasNoDuplicateConsecutivePoints(), "CircularSector should not have duplicate points")
        assertTrue(sector.isProperlyClosedWithoutDuplicates(), "CircularSector should not have duplicate first/last point")
    }

    @Test
    fun testCircularSegmentPointsValid() {
        val segment = CircularSegment(10.0, 10.0, 5.0,
            Angle.fromRadians(0.0), Angle.fromRadians(PI * 0.5), totalPoints = 16)
        assertTrue(segment.points.hasValidPoints(), "CircularSegment should have valid points")
        assertTrue(segment.points.hasNoDuplicateConsecutivePoints(), "CircularSegment should not have duplicate points")
        assertTrue(segment.isProperlyClosedWithoutDuplicates(), "CircularSegment should not have duplicate first/last point")
    }

    @Test
    fun testKitePointsValid() {
        val kite = Kite(
            Point(10.0, 5.0),
            Point(15.0, 10.0),
            Point(10.0, 15.0),
            Point(5.0, 10.0)
        )
        assertTrue(kite.points.hasValidPoints(), "Kite should have valid points")
        assertTrue(kite.points.hasNoDuplicateConsecutivePoints(), "Kite should not have duplicate points")
        assertTrue(kite.isProperlyClosedWithoutDuplicates(), "Kite should not have duplicate first/last point")
    }

    @Test
    fun testParallelogramPointsValid() {
        val parallelogram = Parallelogram(Point(0.0, 0.0), Point(10.0, 0.0), Point(2.0, 5.0))
        assertTrue(parallelogram.points.hasValidPoints(), "Parallelogram should have valid points")
        assertTrue(parallelogram.points.hasNoDuplicateConsecutivePoints(), "Parallelogram should not have duplicate points")
        assertTrue(parallelogram.isProperlyClosedWithoutDuplicates(), "Parallelogram should not have duplicate first/last point")
    }

    @Test
    fun testRhombusPointsValid() {
        val rhombus = Rhombus.fromDiagonals(Point(10.0, 10.0), 10.0, 8.0)
        assertTrue(rhombus.points.hasValidPoints(), "Rhombus should have valid points")
        assertTrue(rhombus.points.hasNoDuplicateConsecutivePoints(), "Rhombus should not have duplicate points")
        assertTrue(rhombus.isProperlyClosedWithoutDuplicates(), "Rhombus should not have duplicate first/last point")
    }

    @Test
    fun testTrapezoidPointsValid() {
        val trapezoid = Trapezoid(
            Point(5.0, 5.0),
            Point(15.0, 5.0),
            Point(12.0, 15.0),
            Point(8.0, 15.0)
        )
        assertTrue(trapezoid.points.hasValidPoints(), "Trapezoid should have valid points")
        assertTrue(trapezoid.points.hasNoDuplicateConsecutivePoints(), "Trapezoid should not have duplicate points")
        assertTrue(trapezoid.isProperlyClosedWithoutDuplicates(), "Trapezoid should not have duplicate first/last point")
    }
}
