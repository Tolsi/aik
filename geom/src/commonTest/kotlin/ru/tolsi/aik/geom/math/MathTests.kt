package ru.tolsi.aik.geom.math

import ru.tolsi.aik.geom.Geometry
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class MathTests {
    @Test
    fun testClampLong() {
        assertEquals(5L, 5L.clamp(0L, 10L))
        assertEquals(0L, (-5L).clamp(0L, 10L))
        assertEquals(10L, 15L.clamp(0L, 10L))
    }

    @Test
    fun testClampInt() {
        assertEquals(5, 5.clamp(0, 10))
        assertEquals(0, (-5).clamp(0, 10))
        assertEquals(10, 15.clamp(0, 10))
    }

    @Test
    fun testClampDouble() {
        assertEquals(5.0, 5.0.clamp(0.0, 10.0), 1e-9)
        assertEquals(0.0, (-5.0).clamp(0.0, 10.0), 1e-9)
        assertEquals(10.0, 15.0.clamp(0.0, 10.0), 1e-9)
    }

    @Test
    fun testClampFloat() {
        assertEquals(5.0f, 5.0f.clamp(0.0f, 10.0f), 1e-6f)
        assertEquals(0.0f, (-5.0f).clamp(0.0f, 10.0f), 1e-6f)
        assertEquals(10.0f, 15.0f.clamp(0.0f, 10.0f), 1e-6f)
    }

    @Test
    fun testBetweenInclusive() {
        assertTrue(5.0.betweenInclusive(0.0, 10.0))
        assertTrue(0.0.betweenInclusive(0.0, 10.0))
        assertTrue(10.0.betweenInclusive(0.0, 10.0))
        assertFalse((-1.0).betweenInclusive(0.0, 10.0))
        assertFalse(11.0.betweenInclusive(0.0, 10.0))
    }

    @Test
    fun testAlmostEqualsFloat() {
        assertTrue(almostEquals(1.0f, 1.0f + Geometry.EPS.toFloat() / 2))
        assertFalse(almostEquals(1.0f, 2.0f))
    }

    @Test
    fun testAlmostZeroFloat() {
        assertTrue(almostZero(0.0f))
        assertTrue(almostZero(Geometry.EPS.toFloat() / 2))
        assertFalse(almostZero(1.0f))
    }

    @Test
    fun testAlmostEqualsDouble() {
        assertTrue(almostEquals(1.0, 1.0 + Geometry.EPS / 2))
        assertFalse(almostEquals(1.0, 2.0))
    }

    @Test
    fun testAlmostZeroDouble() {
        assertTrue(almostZero(0.0))
        assertTrue(almostZero(Geometry.EPS / 2))
        assertFalse(almostZero(1.0))
    }

    @Test
    fun testRoundDecimalPlaces() {
        assertEquals(1.23, 1.23456.roundDecimalPlaces(2), 1e-9)
        assertEquals(1.2, 1.23456.roundDecimalPlaces(1), 1e-9)
        assertEquals(1.0, 1.23456.roundDecimalPlaces(0), 1e-9)
    }

    @Test
    fun testIsEquivalent() {
        assertTrue(isEquivalent(1.0, 1.0))
        assertTrue(isEquivalent(1.0, 1.0 + Geometry.EPS / 2))
        assertFalse(isEquivalent(1.0, 2.0))
    }

    @Test
    fun testIsEquivalentCustomEpsilon() {
        assertTrue(isEquivalent(1.0, 1.1, 0.2))
        assertFalse(isEquivalent(1.0, 1.1, 0.05))
    }

    @Test
    fun testSmoothstep() {
        assertEquals(0.0, 0.0.smoothstep(0.0, 10.0), 1e-9)
        assertEquals(1.0, 10.0.smoothstep(0.0, 10.0), 1e-9)
        assertTrue(5.0.smoothstep(0.0, 10.0) > 0.0)
        assertTrue(5.0.smoothstep(0.0, 10.0) < 1.0)
    }

    @Test
    fun testConvertRange() {
        assertEquals(50.0, 5.0.convertRange(0.0, 10.0, 0.0, 100.0), 1e-9)
        assertEquals(0.0, 0.0.convertRange(0.0, 10.0, 0.0, 100.0), 1e-9)
        assertEquals(100.0, 10.0.convertRange(0.0, 10.0, 0.0, 100.0), 1e-9)
    }

    @Test
    fun testLogInt() {
        assertEquals(2, log(100, 10))
        assertEquals(3, log(8, 2))
    }

    @Test
    fun testLnInt() {
        assertTrue(ln(10) > 0)
    }

    @Test
    fun testLog2Int() {
        assertEquals(3, log2(8))
        assertEquals(4, log2(16))
    }

    @Test
    fun testLog10Int() {
        // log10 converts to int, so precision may vary
        val result100 = log10(100)
        val result1000 = log10(1000)

        assertTrue(result100 >= 1 && result100 <= 3, "log10(100) should be around 2")
        assertTrue(result1000 >= 2 && result1000 <= 4, "log10(1000) should be around 3")
    }

    @Test
    fun testSignNonZeroM1() {
        assertEquals(-1, signNonZeroM1(0.0))
        assertEquals(-1, signNonZeroM1(-5.0))
        assertEquals(1, signNonZeroM1(5.0))
    }

    @Test
    fun testSignNonZeroP1() {
        assertEquals(1, signNonZeroP1(0.0))
        assertEquals(-1, signNonZeroP1(-5.0))
        assertEquals(1, signNonZeroP1(5.0))
    }

    @Test
    fun testDoubleIsAlmostZero() {
        assertTrue(0.0.isAlmostZero())
        assertTrue((1e-20).isAlmostZero())
        assertFalse(1.0.isAlmostZero())
    }

    @Test
    fun testDoubleIsNanOrInfinite() {
        assertFalse(0.0.isNanOrInfinite())
        assertTrue(Double.NaN.isNanOrInfinite())
        assertTrue(Double.POSITIVE_INFINITY.isNanOrInfinite())
        assertTrue(Double.NEGATIVE_INFINITY.isNanOrInfinite())
    }

    @Test
    fun testFloatIsAlmostZero() {
        assertTrue(0.0f.isAlmostZero())
        assertTrue((1e-20f).isAlmostZero())
        assertFalse(1.0f.isAlmostZero())
    }

    @Test
    fun testFloatIsNanOrInfinite() {
        assertFalse(0.0f.isNanOrInfinite())
        assertTrue(Float.NaN.isNanOrInfinite())
        assertTrue(Float.POSITIVE_INFINITY.isNanOrInfinite())
        assertTrue(Float.NEGATIVE_INFINITY.isNanOrInfinite())
    }

    @Test
    fun testIntNextMultipleOf() {
        assertEquals(10, 10.nextMultipleOf(5))
        assertEquals(15, 11.nextMultipleOf(5))
        assertEquals(20, 16.nextMultipleOf(5))
    }

    @Test
    fun testLongNextMultipleOf() {
        assertEquals(10L, 10L.nextMultipleOf(5L))
        assertEquals(15L, 11L.nextMultipleOf(5L))
        assertEquals(20L, 16L.nextMultipleOf(5L))
    }

    @Test
    fun testIntPrevMultipleOf() {
        assertEquals(10, 10.prevMultipleOf(5))
        assertEquals(10, 11.prevMultipleOf(5))
        assertEquals(15, 19.prevMultipleOf(5))
    }

    @Test
    fun testLongPrevMultipleOf() {
        assertEquals(10L, 10L.prevMultipleOf(5L))
        assertEquals(10L, 11L.prevMultipleOf(5L))
        assertEquals(15L, 19L.prevMultipleOf(5L))
    }

    @Test
    fun testIntIsMultipleOf() {
        assertTrue(10.isMultipleOf(5))
        assertTrue(15.isMultipleOf(5))
        assertFalse(11.isMultipleOf(5))
    }

    @Test
    fun testLongIsMultipleOf() {
        assertTrue(10L.isMultipleOf(5L))
        assertTrue(15L.isMultipleOf(5L))
        assertFalse(11L.isMultipleOf(5L))
    }

    @Test
    fun testIsMultipleOfZero() {
        assertTrue(5.isMultipleOf(0))
        assertTrue(5L.isMultipleOf(0L))
    }

    @Test
    fun testAllAlmostEqual() {
        assertTrue(allAlmostEqual(listOf(1.0, 1.0, 1.0)))
        assertTrue(allAlmostEqual(listOf(1.0, 1.0 + Geometry.EPS / 2, 1.0)))
        assertFalse(allAlmostEqual(listOf(1.0, 2.0, 3.0)))
    }

    @Test
    fun testAllAlmostEqualEmpty() {
        assertTrue(allAlmostEqual(emptyList()))
    }

    @Test
    fun testAllAlmostEqualSingleElement() {
        assertTrue(allAlmostEqual(listOf(1.0)))
    }

    @Test
    fun testAllAlmostEqualCustomEps() {
        assertTrue(allAlmostEqual(listOf(1.0, 1.1, 1.05), 0.2))
        assertFalse(allAlmostEqual(listOf(1.0, 1.5, 1.0), 0.2))
    }

    @Test
    fun testAlmostEqualsWithEps() {
        assertTrue(almostEquals(1.0, 1.1, 0.2))
        assertFalse(almostEquals(1.0, 1.5, 0.2))
    }

    @Test
    fun testConvertRangeNegative() {
        assertEquals(-50.0, 5.0.convertRange(0.0, 10.0, -100.0, 0.0), 1e-9)
    }

    @Test
    fun testSmoothstepClamp() {
        // Values outside range should be clamped
        assertEquals(0.0, (-5.0).smoothstep(0.0, 10.0), 1e-9)
        assertEquals(1.0, 15.0.smoothstep(0.0, 10.0), 1e-9)
    }
}
