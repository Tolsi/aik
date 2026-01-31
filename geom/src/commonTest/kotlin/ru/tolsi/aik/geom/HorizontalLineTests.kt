package ru.tolsi.aik.geom

import kotlin.test.Test
import kotlin.test.assertTrue

class HorizontalLineTests {
    @Test
    fun testIntersectionsWithLine_CanCall() {
        // Just verify the function can be called without exceptions
        val result = HorizontalLine.intersectionsWithLine(
            ax = 5.0, ay = 5.0,
            bx0 = 0.0, by0 = 0.0,
            bx1 = 10.0, by1 = 10.0
        )

        // Function returns 0 or 1
        assertTrue(result in 0..1)
    }
}
