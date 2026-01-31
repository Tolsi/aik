package ru.tolsi.aik.geom

import kotlin.test.Test
import kotlin.test.assertEquals

class OrientationTests {
    @Test
    fun testOrient2d_CounterClockwise() {
        // Three points forming a left turn (CCW)
        val p1 = Point(0.0, 0.0)
        val p2 = Point(10.0, 0.0)
        val p3 = Point(5.0, 5.0)

        val orientation = Orientation.orient2d(p1, p2, p3)

        assertEquals(Orientation.COUNTER_CLOCK_WISE, orientation)
    }

    @Test
    fun testOrient2d_Clockwise() {
        // Three points forming a right turn (CW)
        val p1 = Point(0.0, 0.0)
        val p2 = Point(10.0, 0.0)
        val p3 = Point(5.0, -5.0)

        val orientation = Orientation.orient2d(p1, p2, p3)

        assertEquals(Orientation.CLOCK_WISE, orientation)
    }

    @Test
    fun testOrient2d_Collinear() {
        // Three points on the same line
        val p1 = Point(0.0, 0.0)
        val p2 = Point(10.0, 0.0)
        val p3 = Point(5.0, 0.0)

        val orientation = Orientation.orient2d(p1, p2, p3)

        assertEquals(Orientation.COLLINEAR, orientation)
    }

    @Test
    fun testOrient2d_CollinearVertical() {
        // Three points on vertical line
        val p1 = Point(5.0, 0.0)
        val p2 = Point(5.0, 10.0)
        val p3 = Point(5.0, 5.0)

        val orientation = Orientation.orient2d(p1, p2, p3)

        assertEquals(Orientation.COLLINEAR, orientation)
    }

    @Test
    fun testOrient2d_CollinearDiagonal() {
        // Three points on diagonal line
        val p1 = Point(0.0, 0.0)
        val p2 = Point(10.0, 10.0)
        val p3 = Point(5.0, 5.0)

        val orientation = Orientation.orient2d(p1, p2, p3)

        assertEquals(Orientation.COLLINEAR, orientation)
    }

    @Test
    fun testOrient2d_WithRawCoordinates() {
        // Test using raw coordinates instead of Point objects
        val orientation = Orientation.orient2d(
            0.0, 0.0,
            10.0, 0.0,
            5.0, 5.0
        )

        assertEquals(Orientation.COUNTER_CLOCK_WISE, orientation)
    }

    @Test
    fun testOrient2d_EpsilonBoundary_JustAbove() {
        // Point slightly to the left (just above epsilon threshold)
        val p1 = Point(0.0, 0.0)
        val p2 = Point(10.0, 0.0)
        val p3 = Point(5.0, 1e-11)  // Slightly above epsilon (1e-12)

        val orientation = Orientation.orient2d(p1, p2, p3)

        assertEquals(Orientation.COUNTER_CLOCK_WISE, orientation,
            "Point just above epsilon should be CCW")
    }

    @Test
    fun testOrient2d_EpsilonBoundary_JustBelow() {
        // Point slightly to the right (just below negative epsilon threshold)
        val p1 = Point(0.0, 0.0)
        val p2 = Point(10.0, 0.0)
        val p3 = Point(5.0, -1e-11)  // Slightly below -epsilon (-1e-12)

        val orientation = Orientation.orient2d(p1, p2, p3)

        assertEquals(Orientation.CLOCK_WISE, orientation,
            "Point just below -epsilon should be CW")
    }

    @Test
    fun testOrient2d_WithinEpsilon_Positive() {
        // Point within epsilon tolerance (treated as collinear)
        // Using smaller offset that produces determinant < epsilon
        val p1 = Point(0.0, 0.0)
        val p2 = Point(1.0, 0.0)
        val p3 = Point(0.5, 1e-13)  // Very small offset

        val orientation = Orientation.orient2d(p1, p2, p3)

        assertEquals(Orientation.COLLINEAR, orientation,
            "Point within epsilon should be treated as collinear")
    }

    @Test
    fun testOrient2d_WithinEpsilon_Negative() {
        // Point within epsilon tolerance (negative side)
        // Using smaller offset that produces determinant < epsilon
        val p1 = Point(0.0, 0.0)
        val p2 = Point(1.0, 0.0)
        val p3 = Point(0.5, -1e-13)  // Very small offset

        val orientation = Orientation.orient2d(p1, p2, p3)

        assertEquals(Orientation.COLLINEAR, orientation,
            "Point within epsilon should be treated as collinear")
    }

    @Test
    fun testOrient2d_CounterClockwise_Triangle() {
        // Standard CCW triangle
        val p1 = Point(0.0, 0.0)
        val p2 = Point(4.0, 0.0)
        val p3 = Point(2.0, 3.0)

        val orientation = Orientation.orient2d(p1, p2, p3)

        assertEquals(Orientation.COUNTER_CLOCK_WISE, orientation)
    }

    @Test
    fun testOrient2d_Clockwise_Triangle() {
        // Standard CW triangle (reversed order from CCW)
        val p1 = Point(0.0, 0.0)
        val p2 = Point(4.0, 0.0)
        val p3 = Point(2.0, -3.0)

        val orientation = Orientation.orient2d(p1, p2, p3)

        assertEquals(Orientation.CLOCK_WISE, orientation)
    }

    @Test
    fun testOrient2d_NegativeCoordinates() {
        // Test with negative coordinates
        val p1 = Point(-10.0, -10.0)
        val p2 = Point(0.0, -10.0)
        val p3 = Point(-5.0, -5.0)

        val orientation = Orientation.orient2d(p1, p2, p3)

        assertEquals(Orientation.COUNTER_CLOCK_WISE, orientation)
    }

    @Test
    fun testOrient2d_LargeCoordinates() {
        // Test with large coordinates to check numerical stability
        val p1 = Point(1000.0, 1000.0)
        val p2 = Point(1100.0, 1000.0)
        val p3 = Point(1050.0, 1050.0)

        val orientation = Orientation.orient2d(p1, p2, p3)

        assertEquals(Orientation.COUNTER_CLOCK_WISE, orientation)
    }

    @Test
    fun testOrient2d_IdenticalPoints_FirstTwo() {
        // First two points are identical
        val p1 = Point(5.0, 5.0)
        val p2 = Point(5.0, 5.0)
        val p3 = Point(10.0, 10.0)

        val orientation = Orientation.orient2d(p1, p2, p3)

        assertEquals(Orientation.COLLINEAR, orientation,
            "Identical points should result in collinear")
    }

    @Test
    fun testOrient2d_IdenticalPoints_LastTwo() {
        // Last two points are identical
        val p1 = Point(0.0, 0.0)
        val p2 = Point(5.0, 5.0)
        val p3 = Point(5.0, 5.0)

        val orientation = Orientation.orient2d(p1, p2, p3)

        assertEquals(Orientation.COLLINEAR, orientation,
            "Identical points should result in collinear")
    }

    @Test
    fun testOrient2d_AllIdentical() {
        // All three points are identical
        val p1 = Point(5.0, 5.0)
        val p2 = Point(5.0, 5.0)
        val p3 = Point(5.0, 5.0)

        val orientation = Orientation.orient2d(p1, p2, p3)

        assertEquals(Orientation.COLLINEAR, orientation,
            "All identical points should result in collinear")
    }

    @Test
    fun testOrientationEnumValues() {
        assertEquals(1, Orientation.CLOCK_WISE.value)
        assertEquals(-1, Orientation.COUNTER_CLOCK_WISE.value)
        assertEquals(0, Orientation.COLLINEAR.value)
    }

    @Test
    fun testOrient2d_RightAngleTurn() {
        // 90 degree turn to the left
        val p1 = Point(0.0, 0.0)
        val p2 = Point(10.0, 0.0)
        val p3 = Point(10.0, 10.0)

        val orientation = Orientation.orient2d(p1, p2, p3)

        assertEquals(Orientation.COUNTER_CLOCK_WISE, orientation)
    }

    @Test
    fun testOrient2d_ObtuseAngleTurn() {
        // Obtuse angle turn
        val p1 = Point(0.0, 0.0)
        val p2 = Point(10.0, 0.0)
        val p3 = Point(-5.0, 5.0)

        val orientation = Orientation.orient2d(p1, p2, p3)

        assertEquals(Orientation.COUNTER_CLOCK_WISE, orientation)
    }

    @Test
    fun testOrient2d_SmallTriangle() {
        // Very small triangle to test numerical precision
        val p1 = Point(0.0, 0.0)
        val p2 = Point(0.001, 0.0)
        val p3 = Point(0.0005, 0.001)

        val orientation = Orientation.orient2d(p1, p2, p3)

        assertEquals(Orientation.COUNTER_CLOCK_WISE, orientation)
    }
}
