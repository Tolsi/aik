package ru.tolsi.aik.geom

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SizeTests {
    @Test
    fun testConstructor() {
        val size = Size(100.0, 50.0)

        assertEquals(100.0, size.width, 1e-9)
        assertEquals(50.0, size.height, 1e-9)
    }

    @Test
    fun testConstructorOperatorInvoke() {
        val size = Size(100, 50)

        assertEquals(100.0, size.width, 1e-9)
        assertEquals(50.0, size.height, 1e-9)
    }

    @Test
    fun testConstructorEmpty() {
        val size = Size()

        assertEquals(0.0, size.width, 1e-9)
        assertEquals(0.0, size.height, 1e-9)
    }

    @Test
    fun testSetTo() {
        val size = Size()
        size.setTo(100.0, 50.0)

        assertEquals(100.0, size.width, 1e-9)
        assertEquals(50.0, size.height, 1e-9)
    }

    @Test
    fun testSetToNumber() {
        val size = Size()
        size.setTo(100, 50)

        assertEquals(100.0, size.width, 1e-9)
        assertEquals(50.0, size.height, 1e-9)
    }

    @Test
    fun testSetToISize() {
        val size1 = Size(100.0, 50.0)
        val size2 = Size()
        size2.setTo(size1)

        assertEquals(100.0, size2.width, 1e-9)
        assertEquals(50.0, size2.height, 1e-9)
    }

    @Test
    fun testClone() {
        val size = Size(100.0, 50.0)
        val cloned = size.clone()

        assertEquals(size.width, cloned.width, 1e-9)
        assertEquals(size.height, cloned.height, 1e-9)

        // Verify independent
        cloned.width = 999.0
        assertEquals(100.0, size.width, 1e-9)
    }

    @Test
    fun testSetToScaled() {
        val size = Size(100.0, 50.0)
        size.setToScaled(2.0, 3.0)

        assertEquals(200.0, size.width, 1e-9)
        assertEquals(150.0, size.height, 1e-9)
    }

    @Test
    fun testSetToScaledNumber() {
        val size = Size(100.0, 50.0)
        size.setToScaled(2, 3)

        assertEquals(200.0, size.width, 1e-9)
        assertEquals(150.0, size.height, 1e-9)
    }

    @Test
    fun testSetToScaledUniform() {
        val size = Size(100.0, 50.0)
        size.setToScaled(2)

        assertEquals(200.0, size.width, 1e-9)
        assertEquals(100.0, size.height, 1e-9)
    }

    @Test
    fun testArea() {
        val size = Size(10.0, 5.0)
        assertEquals(50.0, size.area, 1e-9)
    }

    @Test
    fun testPerimeter() {
        val size = Size(10.0, 5.0)
        assertEquals(30.0, size.perimeter, 1e-9)
    }

    @Test
    fun testMin() {
        val size = Size(10.0, 5.0)
        assertEquals(5.0, size.min, 1e-9)
    }

    @Test
    fun testMax() {
        val size = Size(10.0, 5.0)
        assertEquals(10.0, size.max, 1e-9)
    }

    @Test
    fun testToString() {
        val size = Size(100.0, 50.0)
        val str = size.toString()

        assertTrue(str.contains("Size"))
        assertTrue(str.contains("width="))
        assertTrue(str.contains("height="))
    }

    @Test
    fun testSizeProperty() {
        val size = Size(100.0, 50.0)
        assertEquals(size, size.size)
    }

    @Test
    fun testAsInt() {
        val size = Size(100.5, 50.5)
        val sizeInt = size.asInt()

        assertEquals(100, sizeInt.width)
        assertEquals(50, sizeInt.height)
    }

    // SizeInt tests
    @Test
    fun testSizeIntConstructor() {
        val sizeInt = SizeInt(100, 50)

        assertEquals(100, sizeInt.width)
        assertEquals(50, sizeInt.height)
    }

    @Test
    fun testSizeIntEmpty() {
        val sizeInt = SizeInt()

        assertEquals(0, sizeInt.width)
        assertEquals(0, sizeInt.height)
    }

    @Test
    fun testSizeIntSetTo() {
        val sizeInt = SizeInt()
        sizeInt.setTo(100, 50)

        assertEquals(100, sizeInt.width)
        assertEquals(50, sizeInt.height)
    }

    @Test
    fun testSizeIntSetToSizeInt() {
        val sizeInt1 = SizeInt(100, 50)
        val sizeInt2 = SizeInt()
        sizeInt2.setTo(sizeInt1)

        assertEquals(100, sizeInt2.width)
        assertEquals(50, sizeInt2.height)
    }

    @Test
    fun testSizeIntSetToScaled() {
        val sizeInt = SizeInt(100, 50)
        sizeInt.setToScaled(2.0, 3.0)

        assertEquals(200, sizeInt.width)
        assertEquals(150, sizeInt.height)
    }

    @Test
    fun testSizeIntSetToScaledNumber() {
        val sizeInt = SizeInt(100, 50)
        sizeInt.setToScaled(2, 3)

        assertEquals(200, sizeInt.width)
        assertEquals(150, sizeInt.height)
    }

    @Test
    fun testSizeIntContains() {
        val larger = SizeInt(100, 100)
        val smaller = SizeInt(50, 50)

        assertTrue(smaller in larger)
    }

    @Test
    fun testSizeIntContainsNot() {
        val smaller = SizeInt(50, 50)
        val larger = SizeInt(100, 100)

        assertFalse(larger in smaller)
    }

    @Test
    fun testSizeIntTimesDouble() {
        val sizeInt = SizeInt(10, 20)
        val scaled = sizeInt * 2.0

        assertEquals(20, scaled.width)
        assertEquals(40, scaled.height)
    }

    @Test
    fun testSizeIntTimesNumber() {
        val sizeInt = SizeInt(10, 20)
        val scaled = sizeInt * 2

        assertEquals(20, scaled.width)
        assertEquals(40, scaled.height)
    }

    @Test
    fun testSizeIntAsDouble() {
        val sizeInt = SizeInt(100, 50)
        val size = sizeInt.asDouble()

        assertEquals(100.0, size.width, 1e-9)
        assertEquals(50.0, size.height, 1e-9)
    }

    @Test
    fun testSizeIntToString() {
        val sizeInt = SizeInt(100, 50)
        val str = sizeInt.toString()

        assertTrue(str.contains("SizeInt"))
        assertTrue(str.contains("width="))
        assertTrue(str.contains("height="))
    }

    @Test
    fun testISizeOperatorInvoke() {
        val size: ISize = ISize(100, 50)

        assertEquals(100.0, size.width, 1e-9)
        assertEquals(50.0, size.height, 1e-9)
    }
}

private fun assertFalse(b: Boolean) {
    kotlin.test.assertFalse(b)
}
