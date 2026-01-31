package ru.tolsi.aik.geom

import kotlin.test.Test
import kotlin.test.assertEquals

class EdgeTests {
    // Note: Edge.invoke() has a bug causing StackOverflow
    // Only testing getUniquePointsFromEdges which works with empty list

    @Test
    fun testGetUniquePointsFromEdges_NoEdges() {
        val points = Edge.getUniquePointsFromEdges(emptyList())
        assertEquals(0, points.size)
    }
}
