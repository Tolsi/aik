package ru.tolsi.aik.geom

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class PolylineTests {
    @Test
    fun testConstructor() {
        val points = PointArrayList(
            Point(0.0, 0.0),
            Point(10.0, 0.0),
            Point(10.0, 10.0)
        )
        val polyline = Polyline(points)

        assertEquals(3, polyline.points.size)
    }

    @Test
    fun testClosedProperty() {
        val points = PointArrayList(
            Point(0.0, 0.0),
            Point(10.0, 0.0),
            Point(10.0, 10.0)
        )
        val polyline = Polyline(points)

        assertFalse(polyline.closed)
    }

    @Test
    fun testAreaProperty() {
        val points = PointArrayList(
            Point(0.0, 0.0),
            Point(10.0, 0.0),
            Point(10.0, 10.0)
        )
        val polyline = Polyline(points)

        assertEquals(0.0, polyline.area, 1e-9)
    }

    @Test
    fun testContainsPointAlwaysFalse() {
        val points = PointArrayList(
            Point(0.0, 0.0),
            Point(10.0, 0.0),
            Point(10.0, 10.0)
        )
        val polyline = Polyline(points)

        assertFalse(polyline.containsPoint(0.0, 0.0))
        assertFalse(polyline.containsPoint(5.0, 0.0))
        assertFalse(polyline.containsPoint(99.0, 99.0))
    }

    @Test
    fun testEmptyPolyline() {
        val points = PointArrayList()
        val polyline = Polyline(points)

        assertEquals(0, polyline.points.size)
        assertFalse(polyline.closed)
        assertEquals(0.0, polyline.area, 1e-9)
    }

    @Test
    fun testSinglePointPolyline() {
        val points = PointArrayList(Point(5.0, 5.0))
        val polyline = Polyline(points)

        assertEquals(1, polyline.points.size)
        assertFalse(polyline.closed)
    }

    @Test
    fun testLongPolyline() {
        val points = PointArrayList()
        for (i in 0 until 100) {
            points.add(i.toDouble(), i.toDouble())
        }
        val polyline = Polyline(points)

        assertEquals(100, polyline.points.size)
        assertFalse(polyline.closed)
        assertEquals(0.0, polyline.area, 1e-9)
    }

    @Test
    fun testPolylineImplementsFigure2D() {
        val points = PointArrayList(
            Point(0.0, 0.0),
            Point(10.0, 0.0)
        )
        val polyline = Polyline(points)

        // Verify implements Figure2D
        assertTrue(polyline is Figure2D)
    }
}

private fun assertTrue(b: Boolean) {
    kotlin.test.assertTrue(b)
}
