package ru.tolsi.aik.geom

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class Figure2DTests {
    @Test
    fun testToFigure2D_RectangleDetection() {
        // Rectangle points in specific order (p0, p1, p2, p3) where p1 and p2 are opposite corners
        val points = PointArrayList(
            Point(0.0, 0.0),
            Point(10.0, 0.0),
            Point(10.0, 10.0),
            Point(0.0, 10.0)
        )

        val figure = points.toFigure2D(closed = true)

        assertTrue(figure is Rectangle, "Should detect Rectangle pattern")
        val rect = figure as Rectangle
        assertEquals(0.0, rect.x)
        assertEquals(0.0, rect.y)
        assertEquals(10.0, rect.width)
        assertEquals(10.0, rect.height)
    }

    @Test
    fun testToFigure2D_RectangleDetectionNegativeCoordinates() {
        // Rectangle with negative coordinates
        val points = PointArrayList(
            Point(-10.0, -10.0),
            Point(5.0, -10.0),
            Point(5.0, 5.0),
            Point(-10.0, 5.0)
        )

        val figure = points.toFigure2D(closed = true)

        assertTrue(figure is Rectangle, "Should detect Rectangle with negative coords")
        val rect = figure as Rectangle
        assertEquals(-10.0, rect.x)
        assertEquals(-10.0, rect.y)
        assertEquals(15.0, rect.width)
        assertEquals(15.0, rect.height)
    }

    @Test
    fun testToFigure2D_NonRectangle4Points() {
        // 4 points that don't form a rectangle pattern
        val points = PointArrayList(
            Point(0.0, 0.0),
            Point(10.0, 5.0),
            Point(8.0, 10.0),
            Point(2.0, 7.0)
        )

        val figure = points.toFigure2D(closed = true)

        assertTrue(figure is Polygon, "Non-rectangular 4 points should create Polygon")
        assertFalse(figure is Rectangle, "Should not be detected as Rectangle")
    }

    @Test
    fun testToFigure2D_ClosedPolygon() {
        // Triangle
        val points = PointArrayList(
            Point(0.0, 0.0),
            Point(10.0, 0.0),
            Point(5.0, 10.0)
        )

        val figure = points.toFigure2D(closed = true)

        assertTrue(figure is Polygon, "Should create closed Polygon")
        assertTrue(figure.closed, "Figure should be closed")
        assertEquals(3, figure.points.size)
    }

    @Test
    fun testToFigure2D_OpenPolyline() {
        // Open path
        val points = PointArrayList(
            Point(0.0, 0.0),
            Point(10.0, 0.0),
            Point(5.0, 10.0)
        )

        val figure = points.toFigure2D(closed = false)

        assertTrue(figure is Polyline, "Should create open Polyline")
        assertFalse(figure.closed, "Figure should be open")
        assertEquals(3, figure.points.size)
    }

    @Test
    fun testToFigure2D_EmptyPoints() {
        val points = PointArrayList()

        val figure = points.toFigure2D(closed = true)

        assertTrue(figure is Polygon)
        assertEquals(0, figure.points.size)
    }

    @Test
    fun testEdges() {
        val points = PointArrayList(
            Point(0.0, 0.0),
            Point(10.0, 0.0),
            Point(10.0, 10.0)
        )
        val polygon = Polygon(points)

        val edges = polygon.edges()

        // For a closed figure with 3 points, we expect 3 edges (including closing edge)
        assertEquals(3, edges.size)

        // Check first edge
        val edge0 = edges[0]
        assertTrue(edge0.from.x == 0.0 && edge0.from.y == 0.0)
        assertTrue(edge0.to.x == 10.0 && edge0.to.y == 0.0)

        // Check last edge (closing edge)
        val lastEdge = edges[2]
        assertTrue(lastEdge.from.x == 10.0 && lastEdge.from.y == 10.0)
        assertTrue(lastEdge.to.x == 0.0 && lastEdge.to.y == 0.0)
    }

    @Test
    fun testClosedPoints() {
        val points = PointArrayList(
            Point(0.0, 0.0),
            Point(10.0, 0.0),
            Point(5.0, 10.0)
        )
        val polygon = Polygon(points)

        val closedPoints = polygon.closedPoints()

        assertEquals(4, closedPoints.size, "Should have original 3 points plus first point repeated")
        assertEquals(closedPoints.first(), closedPoints.last(), "First and last points should be equal")
    }

    @Test
    fun testGetAllPoints() {
        val points = PointArrayList(
            Point(1.0, 2.0),
            Point(3.0, 4.0),
            Point(5.0, 6.0)
        )
        val polygon = Polygon(points)

        val allPoints = polygon.getAllPoints()

        assertEquals(3, allPoints.size)
        assertEquals(1.0, allPoints.getX(0))
        assertEquals(2.0, allPoints.getY(0))
        assertEquals(3.0, allPoints.getX(1))
        assertEquals(4.0, allPoints.getY(1))
    }

    @Test
    fun testGetAllPointsWithCustomOutput() {
        val points = PointArrayList(
            Point(1.0, 2.0),
            Point(3.0, 4.0)
        )
        val polygon = Polygon(points)
        val existingPoints = PointArrayList(Point(0.0, 0.0))

        val result = polygon.getAllPoints(existingPoints)

        assertEquals(3, result.size, "Should append to existing points")
        assertEquals(0.0, result.getX(0), "First point should be from existing list")
        assertEquals(1.0, result.getX(1), "Following points from polygon")
    }

    @Test
    fun testToPolygon_FromPolygon() {
        val points = PointArrayList(
            Point(0.0, 0.0),
            Point(10.0, 0.0),
            Point(5.0, 10.0)
        )
        val polygon = Polygon(points)

        val result = polygon.toPolygon()

        assertTrue(result === polygon, "Should return same instance when already a Polygon")
    }

    @Test
    fun testToPolygon_FromOtherFigure() {
        val points = PointArrayList(
            Point(0.0, 0.0),
            Point(10.0, 0.0),
            Point(5.0, 10.0)
        )
        val polyline = Polyline(points)

        val polygon = polyline.toPolygon()

        assertTrue(polygon is Polygon)
        assertEquals(3, polygon.points.size)
    }

    @Test
    fun testMerge_CanBeMerged() {
        // Two triangles sharing an edge (2 common points)
        val points1 = PointArrayList(
            Point(0.0, 0.0),
            Point(10.0, 0.0),
            Point(5.0, 10.0)
        )
        val polygon1 = Polygon(points1)

        val points2 = PointArrayList(
            Point(0.0, 0.0),
            Point(5.0, 10.0),
            Point(-5.0, 10.0)
        )
        val polygon2 = Polygon(points2)

        val merged = polygon1.merge(polygon2)

        assertNotNull(merged, "Shapes with 2 common points should be mergeable")
        // Total: 6 points, unique: 4 points (0,0 and 5,10 are shared)
        assertEquals(4, merged.points.size)
    }

    @Test
    fun testMerge_CannotBeMerged() {
        // Two triangles with only 1 common point
        val points1 = PointArrayList(
            Point(0.0, 0.0),
            Point(10.0, 0.0),
            Point(5.0, 10.0)
        )
        val polygon1 = Polygon(points1)

        val points2 = PointArrayList(
            Point(0.0, 0.0),
            Point(-10.0, 0.0),
            Point(-5.0, 10.0)
        )
        val polygon2 = Polygon(points2)

        val merged = polygon1.merge(polygon2)

        assertNull(merged, "Shapes with only 1 common point cannot be merged")
    }

    @Test
    fun testMerge_NoCommonPoints() {
        // Two separate triangles
        val points1 = PointArrayList(
            Point(0.0, 0.0),
            Point(10.0, 0.0),
            Point(5.0, 10.0)
        )
        val polygon1 = Polygon(points1)

        val points2 = PointArrayList(
            Point(20.0, 0.0),
            Point(30.0, 0.0),
            Point(25.0, 10.0)
        )
        val polygon2 = Polygon(points2)

        val merged = polygon1.merge(polygon2)

        assertNull(merged, "Completely separate shapes cannot be merged")
    }

    @Test
    fun testContainsPoint_DefaultImplementation() {
        // Basic Figure2D with default containsPoint implementation
        val points = PointArrayList(
            Point(0.0, 0.0),
            Point(10.0, 0.0),
            Point(5.0, 10.0)
        )
        val polyline = Polyline(points)  // Polyline doesn't override containsPoint

        assertFalse(polyline.containsPoint(5.0, 5.0), "Default implementation should return false")
        assertFalse(polyline.containsPoint(Point(5.0, 5.0)), "Default implementation should return false")
    }

    @Test
    fun testTotalVertices() {
        val points1 = PointArrayList(
            Point(0.0, 0.0),
            Point(10.0, 0.0),
            Point(5.0, 10.0)
        )
        val points2 = PointArrayList(
            Point(20.0, 0.0),
            Point(30.0, 0.0),
            Point(25.0, 10.0),
            Point(20.0, 5.0)
        )

        val list = listOf(points1, points2)

        assertEquals(7, list.totalVertices, "Should sum all vertices from all point arrays")
    }

    @Test
    fun testTotalVerticesEmpty() {
        val list = listOf<IPointArrayList>()
        assertEquals(0, list.totalVertices, "Empty list should have 0 vertices")
    }
}
