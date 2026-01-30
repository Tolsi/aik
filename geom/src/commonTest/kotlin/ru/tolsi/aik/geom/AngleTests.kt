package ru.tolsi.aik.geom

import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class AngleTests {
    @Test
    fun testFromRadians() {
        val angle = Angle.fromRadians(PI)
        assertEquals(PI, angle.radians, 1e-9)
    }

    @Test
    fun testFromDegrees() {
        val angle = Angle.fromDegrees(180.0)
        assertEquals(PI, angle.radians, 1e-9)
    }

    @Test
    fun testZero() {
        assertEquals(0.0, Angle.ZERO.radians, 1e-9)
    }

    @Test
    fun testDegrees() {
        val angle = Angle.fromDegrees(180.0)
        assertEquals(180.0, angle.degrees, 1e-9)
    }

    @Test
    fun testDegreesExtension() {
        val angle = 90.degrees
        assertEquals(90.0, angle.degrees, 1e-9)
    }

    @Test
    fun testRadiansExtension() {
        val angle = PI.radians
        assertEquals(PI, angle.radians, 1e-9)
    }

    @Test
    fun testCos() {
        val angle = Angle.fromDegrees(0.0)
        assertEquals(1.0, cos(angle), 1e-9)

        val angle90 = Angle.fromDegrees(90.0)
        assertEquals(0.0, cos(angle90), 1e-9)
    }

    @Test
    fun testSin() {
        val angle = Angle.fromDegrees(0.0)
        assertEquals(0.0, sin(angle), 1e-9)

        val angle90 = Angle.fromDegrees(90.0)
        assertEquals(1.0, sin(angle90), 1e-9)
    }

    @Test
    fun testTan() {
        val angle = Angle.fromDegrees(0.0)
        assertEquals(0.0, tan(angle), 1e-9)

        val angle45 = Angle.fromDegrees(45.0)
        assertEquals(1.0, tan(angle45), 1e-9)
    }

    @Test
    fun testCosineProperty() {
        val angle = Angle.fromDegrees(0.0)
        assertEquals(1.0, angle.cosine, 1e-9)
    }

    @Test
    fun testSineProperty() {
        val angle = Angle.fromDegrees(90.0)
        assertEquals(1.0, angle.sine, 1e-9)
    }

    @Test
    fun testTangentProperty() {
        val angle = Angle.fromDegrees(45.0)
        assertEquals(1.0, angle.tangent, 1e-9)
    }

    @Test
    fun testAbsoluteValue() {
        val negAngle = Angle.fromDegrees(-45.0)
        val absAngle = negAngle.absoluteValue

        assertTrue(absAngle.radians > 0)
    }

    @Test
    fun testShortDistanceTo() {
        val from = Angle.fromDegrees(10.0)
        val to = Angle.fromDegrees(20.0)

        val distance = from.shortDistanceTo(to)
        assertEquals(10.0, distance.degrees, 1e-9)
    }

    @Test
    fun testShortDistanceToWraparound() {
        val from = Angle.fromDegrees(350.0)
        val to = Angle.fromDegrees(10.0)

        val distance = from.shortDistanceTo(to)
        // Should be 20 degrees (short way across 0)
        assertEquals(20.0, distance.degrees, 1e-9)
    }

    @Test
    fun testLongDistanceTo() {
        val from = Angle.fromDegrees(10.0)
        val to = Angle.fromDegrees(20.0)

        val distance = from.longDistanceTo(to)
        // Long way is 360 - 10 = 350 degrees
        assertTrue(distance.degrees < 0 || distance.degrees > 180.0)
    }

    @Test
    fun testTimesOperator() {
        val angle = Angle.fromDegrees(45.0)
        val doubled = angle * 2

        assertEquals(90.0, doubled.degrees, 1e-9)
    }

    @Test
    fun testTimesOperatorDouble() {
        val angle = Angle.fromDegrees(45.0)
        val doubled = angle * 2.0

        assertEquals(90.0, doubled.degrees, 1e-9)
    }

    @Test
    fun testDivOperator() {
        val angle = Angle.fromDegrees(90.0)
        val halved = angle / 2

        assertEquals(45.0, halved.degrees, 1e-9)
    }

    @Test
    fun testDivOperatorDouble() {
        val angle = Angle.fromDegrees(90.0)
        val halved = angle / 2.0

        assertEquals(45.0, halved.degrees, 1e-9)
    }

    @Test
    fun testDivOperatorAngleRatio() {
        val angle1 = Angle.fromDegrees(90.0)
        val angle2 = Angle.fromDegrees(45.0)

        val ratio = angle1 / angle2
        assertEquals(2.0, ratio, 1e-9)
    }

    @Test
    fun testPlusOperator() {
        val angle1 = Angle.fromDegrees(30.0)
        val angle2 = Angle.fromDegrees(60.0)

        val sum = angle1 + angle2
        assertEquals(90.0, sum.degrees, 1e-9)
    }

    @Test
    fun testMinusOperator() {
        val angle1 = Angle.fromDegrees(90.0)
        val angle2 = Angle.fromDegrees(30.0)

        val diff = angle1 - angle2
        assertEquals(60.0, diff.degrees, 1e-9)
    }

    @Test
    fun testUnaryMinus() {
        val angle = Angle.fromDegrees(45.0)
        val negated = -angle

        assertEquals(-45.0, negated.degrees, 1e-9)
    }

    @Test
    fun testUnaryPlus() {
        val angle = Angle.fromDegrees(45.0)
        val positive = +angle

        assertEquals(45.0, positive.degrees, 1e-9)
    }

    @Test
    fun testCompareTo() {
        val angle1 = Angle.fromDegrees(30.0)
        val angle2 = Angle.fromDegrees(60.0)

        assertTrue(angle1 < angle2)
        assertTrue(angle2 > angle1)
        assertFalse(angle1 > angle2)
    }

    @Test
    fun testCompareToEquals() {
        val angle1 = Angle.fromDegrees(45.0)
        val angle2 = Angle.fromDegrees(45.0)

        assertEquals(0, angle1.compareTo(angle2))
    }

    @Test
    fun testContainsInClosedRange() {
        val range = Angle.fromDegrees(0.0)..Angle.fromDegrees(90.0)
        val angle = Angle.fromDegrees(45.0)

        assertTrue(angle in range)
    }

    @Test
    fun testContainsInClosedRangeBoundary() {
        val range = Angle.fromDegrees(0.0)..Angle.fromDegrees(90.0)

        assertTrue(Angle.fromDegrees(0.0) in range)
        assertTrue(Angle.fromDegrees(90.0) in range)
    }

    @Test
    fun testContainsInOpenRange() {
        val range = Angle.fromDegrees(0.0) until Angle.fromDegrees(90.0)
        val angle = Angle.fromDegrees(45.0)

        assertTrue(angle in range)
    }

    @Test
    fun testContainsInOpenRangeBoundary() {
        val range = Angle.fromDegrees(0.0) until Angle.fromDegrees(90.0)

        assertTrue(Angle.fromDegrees(0.0) in range)
        assertFalse(Angle.fromDegrees(90.0) in range)
    }

    @Test
    fun testInBetweenInclusive() {
        val angle = Angle.fromDegrees(45.0)
        val min = Angle.fromDegrees(0.0)
        val max = Angle.fromDegrees(90.0)

        assertTrue(angle.inBetweenInclusive(min, max))
    }

    @Test
    fun testInBetweenInclusiveBoundary() {
        val min = Angle.fromDegrees(0.0)
        val max = Angle.fromDegrees(90.0)

        assertTrue(Angle.fromDegrees(0.0).inBetweenInclusive(min, max))
        assertTrue(Angle.fromDegrees(90.0).inBetweenInclusive(min, max))
    }

    @Test
    fun testInBetweenExclusive() {
        val angle = Angle.fromDegrees(45.0)
        val min = Angle.fromDegrees(0.0)
        val max = Angle.fromDegrees(90.0)

        assertTrue(angle.inBetweenExclusive(min, max))
    }

    @Test
    fun testInBetweenExclusiveBoundary() {
        val min = Angle.fromDegrees(0.0)
        val max = Angle.fromDegrees(90.0)

        assertTrue(Angle.fromDegrees(0.0).inBetweenExclusive(min, max))
        assertFalse(Angle.fromDegrees(90.0).inBetweenExclusive(min, max))
    }

    @Test
    fun testInBetweenClosedRange() {
        val range = Angle.fromDegrees(0.0)..Angle.fromDegrees(90.0)
        val angle = Angle.fromDegrees(45.0)

        assertTrue(angle inBetween range)
    }

    @Test
    fun testInBetweenOpenRange() {
        val range = Angle.fromDegrees(0.0) until Angle.fromDegrees(90.0)
        val angle = Angle.fromDegrees(45.0)

        assertTrue(angle inBetween range)
    }

    @Test
    fun testNormalized() {
        val angle = Angle.fromDegrees(450.0)  // 360 + 90
        val normalized = angle.normalized

        assertEquals(90.0, normalized.degrees, 1e-9)
    }

    @Test
    fun testNormalizedNegative() {
        val angle = Angle.fromDegrees(-90.0)
        val normalized = angle.normalized

        assertEquals(270.0, normalized.degrees, 1e-9)
    }

    @Test
    fun testAngleBetweenPoints() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(1.0, 0.0)

        val angle = Angle.between(p0, p1)
        assertEquals(0.0, angle.degrees, 1e-9)
    }

    @Test
    fun testAngleBetweenPointsVertical() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(0.0, 1.0)

        val angle = Angle.between(p0, p1)
        assertEquals(90.0, angle.degrees, 1e-9)
    }

    @Test
    fun testAngleBetweenCoordinates() {
        val angle = Angle.between(0.0, 0.0, 1.0, 0.0)
        assertEquals(0.0, angle.degrees, 1e-9)
    }

    @Test
    fun testAngleBetweenCoordinatesNumber() {
        val angle = Angle.between(0, 0, 1, 0)
        assertEquals(0.0, angle.degrees, 1e-9)
    }

    @Test
    fun testCos01() {
        val value = Angle.cos01(0.0)
        assertEquals(1.0, value, 1e-9)

        val value90 = Angle.cos01(0.25)  // 0.25 * 360 = 90 degrees
        assertEquals(0.0, value90, 1e-9)
    }

    @Test
    fun testSin01() {
        val value = Angle.sin01(0.0)
        assertEquals(0.0, value, 1e-9)

        val value90 = Angle.sin01(0.25)  // 0.25 * 360 = 90 degrees
        assertEquals(1.0, value90, 1e-9)
    }

    @Test
    fun testTan01() {
        val value = Angle.tan01(0.0)
        assertEquals(0.0, value, 1e-9)

        val value45 = Angle.tan01(0.125)  // 0.125 * 360 = 45 degrees
        assertEquals(1.0, value45, 1e-9)
    }

    @Test
    fun testDegreesToRadians() {
        val radians = Angle.degreesToRadians(180.0)
        assertEquals(PI, radians, 1e-9)
    }

    @Test
    fun testRadiansToDegrees() {
        val degrees = Angle.radiansToDegrees(PI)
        assertEquals(180.0, degrees, 1e-9)
    }

    @Test
    fun testToString() {
        val angle = Angle.fromDegrees(45.0)
        val str = angle.toString()

        assertTrue(str.contains("45"))
        assertTrue(str.contains("degrees"))
    }

    @Test
    fun testInBetweenWraparound() {
        // Test when range wraps around 0 degrees
        val angle = Angle.fromDegrees(10.0)
        val min = Angle.fromDegrees(350.0)
        val max = Angle.fromDegrees(30.0)

        assertTrue(angle.inBetween(min, max, inclusive = true))
    }

    @Test
    fun testInBetweenWraparoundOutside() {
        val angle = Angle.fromDegrees(180.0)
        val min = Angle.fromDegrees(350.0)
        val max = Angle.fromDegrees(30.0)

        assertFalse(angle.inBetween(min, max, inclusive = true))
    }

    @Test
    fun testLargeAngle() {
        val angle = Angle.fromDegrees(720.0)  // Two full rotations
        assertEquals(720.0, angle.degrees, 1e-9)

        val normalized = angle.normalized
        assertEquals(0.0, normalized.degrees, 1e-9)
    }

    @Test
    fun testNegativeAngle() {
        val angle = Angle.fromDegrees(-45.0)
        assertEquals(-45.0, angle.degrees, 1e-9)
    }
}
