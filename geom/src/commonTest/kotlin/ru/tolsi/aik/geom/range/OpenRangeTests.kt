package ru.tolsi.aik.geom.range

import ru.tolsi.aik.geom.Angle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class OpenRangeTests {
    @Test
    fun testConstructor() {
        val range = OpenRange(0, 10)

        assertEquals(0, range.start)
        assertEquals(10, range.endExclusive)
    }

    @Test
    fun testContainsInside() {
        val range = OpenRange(0, 10)

        assertTrue(5 in range)
        assertTrue(0 in range)
    }

    @Test
    fun testContainsExclusiveEnd() {
        val range = OpenRange(0, 10)

        assertFalse(10 in range)
    }

    @Test
    fun testContainsOutside() {
        val range = OpenRange(0, 10)

        assertFalse(-1 in range)
        assertFalse(11 in range)
    }

    @Test
    fun testWithDoubleType() {
        val range = OpenRange(0.0, 10.0)

        assertTrue(5.0 in range)
        assertTrue(0.0 in range)
        assertFalse(10.0 in range)
        assertFalse(10.1 in range)
    }

    @Test
    fun testWithStringType() {
        val range = OpenRange("a", "z")

        assertTrue("m" in range)
        assertTrue("a" in range)
        assertFalse("z" in range)
    }

    @Test
    fun testWithAngleType() {
        val range = OpenRange(Angle.fromDegrees(0.0), Angle.fromDegrees(90.0))

        assertTrue(Angle.fromDegrees(45.0) in range)
        assertTrue(Angle.fromDegrees(0.0) in range)
        assertFalse(Angle.fromDegrees(90.0) in range)
        assertFalse(Angle.fromDegrees(91.0) in range)
    }

    @Test
    fun testNegativeRange() {
        val range = OpenRange(-10, 10)

        assertTrue(-5 in range)
        assertTrue(0 in range)
        assertTrue(5 in range)
        assertTrue(-10 in range)
        assertFalse(10 in range)
    }

    @Test
    fun testSingletonRange() {
        val range = OpenRange(5, 6)

        assertTrue(5 in range)
        assertFalse(6 in range)
        assertFalse(4 in range)
    }

    @Test
    fun testEmptyRange() {
        val range = OpenRange(10, 10)

        assertFalse(10 in range)
        assertFalse(9 in range)
        assertFalse(11 in range)
    }
}
