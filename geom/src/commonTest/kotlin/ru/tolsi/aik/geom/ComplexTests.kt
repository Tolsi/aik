package ru.tolsi.aik.geom

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class ComplexTests {
    @Test
    fun testConstructorWithSinglePolygon() {
        val triangle = Triangle(
            Point(0.0, 0.0),
            Point(10.0, 0.0),
            Point(5.0, 10.0)
        )
        val complex = Complex(listOf(triangle))

        assertEquals(1, complex.items.size)
        assertEquals(3, complex.points.size)
        assertFalse(complex.closed, "Complex should not be closed")
    }

    @Test
    fun testConstructorWithMultiplePolygons() {
        val triangle1 = Triangle(
            Point(0.0, 0.0),
            Point(10.0, 0.0),
            Point(5.0, 10.0)
        )
        val triangle2 = Triangle(
            Point(20.0, 0.0),
            Point(30.0, 0.0),
            Point(25.0, 10.0)
        )
        val complex = Complex(listOf(triangle1, triangle2))

        assertEquals(2, complex.items.size)
        assertEquals(6, complex.points.size, "Should have all points from both triangles")
    }

    @Test
    fun testPointsFlattening() {
        val rect = Rectangle(0.0, 0.0, 10.0, 10.0)
        val triangle = Triangle(
            Point(20.0, 0.0),
            Point(30.0, 0.0),
            Point(25.0, 10.0)
        )
        val complex = Complex(listOf(rect, triangle))

        assertEquals(7, complex.points.size, "4 points from rectangle + 3 from triangle")

        // Check that points are properly flattened
        assertTrue(complex.points.contains(0.0, 0.0))
        assertTrue(complex.points.contains(20.0, 0.0))
    }

    @Test
    fun testAreaSinglePolygon() {
        val rect = Rectangle(0.0, 0.0, 10.0, 10.0)
        val complex = Complex(listOf(rect))

        assertEquals(100.0, complex.area, "Area should match rectangle area")
    }

    @Test
    fun testAreaMultiplePolygons() {
        val rect1 = Rectangle(0.0, 0.0, 10.0, 10.0)  // Area = 100
        val rect2 = Rectangle(20.0, 20.0, 5.0, 5.0)  // Area = 25
        val complex = Complex(listOf(rect1, rect2))

        assertEquals(125.0, complex.area, "Area should be sum of all polygon areas")
    }

    @Test
    fun testAreaWithTriangle() {
        // Triangle with area = 0.5 * base * height = 0.5 * 10 * 10 = 50
        val triangle = Triangle(
            Point(0.0, 0.0),
            Point(10.0, 0.0),
            Point(5.0, 10.0)
        )
        val rect = Rectangle(20.0, 0.0, 10.0, 5.0)  // Area = 50
        val complex = Complex(listOf(triangle, rect))

        assertEquals(100.0, complex.area, 0.01, "Combined area should be ~100")
    }

    @Test
    fun testContainsPoint_PointInOnePolygon() {
        val rect1 = Rectangle(0.0, 0.0, 10.0, 10.0)
        val rect2 = Rectangle(20.0, 20.0, 10.0, 10.0)
        val complex = Complex(listOf(rect1, rect2))

        // containsPoint checks if point is in the point list (vertices), not inside polygon
        // So we check a vertex instead
        assertTrue(complex.containsPoint(0.0, 0.0))
        assertTrue(complex.containsPoint(20.0, 20.0))
    }

    @Test
    fun testContainsPoint_PointAtVertex() {
        val triangle = Triangle(
            Point(0.0, 0.0),
            Point(10.0, 0.0),
            Point(5.0, 10.0)
        )
        val complex = Complex(listOf(triangle))

        // Check vertex point
        assertTrue(complex.containsPoint(0.0, 0.0), "Should contain vertex point")
        assertTrue(complex.containsPoint(10.0, 0.0), "Should contain vertex point")
        assertTrue(complex.containsPoint(5.0, 10.0), "Should contain vertex point")
    }

    @Test
    fun testContainsPoint_PointNotInAnyPolygon() {
        val rect1 = Rectangle(0.0, 0.0, 10.0, 10.0)
        val rect2 = Rectangle(20.0, 20.0, 10.0, 10.0)
        val complex = Complex(listOf(rect1, rect2))

        // Point not in any rectangle
        assertFalse(complex.containsPoint(15.0, 15.0), "Point between rectangles should not be contained")
        assertFalse(complex.containsPoint(-5.0, -5.0), "Point outside all rectangles should not be contained")
    }

    @Test
    fun testContainsPoint_UsingPointObject() {
        val rect = Rectangle(0.0, 0.0, 10.0, 10.0)
        val complex = Complex(listOf(rect))

        // containsPoint checks vertices, not interior points
        assertTrue(complex.containsPoint(Point(0.0, 0.0)), "Vertex should be contained")
        assertTrue(complex.containsPoint(Point(10.0, 10.0)), "Vertex should be contained")
        assertFalse(complex.containsPoint(Point(15.0, 15.0)), "Non-vertex should not be contained")
    }

    @Test
    fun testEmptyComplexPolygon() {
        val complex = Complex(emptyList())

        assertEquals(0, complex.items.size)
        assertEquals(0, complex.points.size)
        assertEquals(0.0, complex.area)
        assertFalse(complex.containsPoint(0.0, 0.0))
    }

    @Test
    fun testComplexWithOverlappingPolygons() {
        // Two overlapping rectangles
        val rect1 = Rectangle(0.0, 0.0, 10.0, 10.0)
        val rect2 = Rectangle(5.0, 5.0, 10.0, 10.0)
        val complex = Complex(listOf(rect1, rect2))

        // Area is sum (not union), so overlapping areas are counted twice
        assertEquals(200.0, complex.area, "Area should be sum, not union")

        // containsPoint checks vertices, not interior
        assertTrue(complex.containsPoint(5.0, 5.0), "Overlapping vertex should be contained")
    }

    @Test
    fun testComplexWithDifferentShapes() {
        val circle = Circle(10.0, 10.0, 5.0, totalPoints = 8)
        val rect = Rectangle(30.0, 30.0, 10.0, 10.0)
        val triangle = Triangle(
            Point(50.0, 0.0),
            Point(60.0, 0.0),
            Point(55.0, 10.0)
        )
        val complex = Complex(listOf(circle, rect, triangle))

        assertEquals(3, complex.items.size)
        // 8 points from circle + 4 from rect + 3 from triangle = 15
        assertEquals(15, complex.points.size)

        // Total area is sum of all three
        val totalArea = circle.area + rect.area + triangle.area
        assertEquals(totalArea, complex.area, 0.01)
    }

    @Test
    fun testPointsLazyEvaluation() {
        // Create a complex with large polygons
        val rect1 = Rectangle(0.0, 0.0, 100.0, 100.0)
        val rect2 = Rectangle(200.0, 200.0, 100.0, 100.0)
        val complex = Complex(listOf(rect1, rect2))

        // Access points multiple times - should use cached value
        val points1 = complex.points
        val points2 = complex.points

        assertTrue(points1 === points2, "Lazy property should return same instance")
    }

    @Test
    fun testComplexWithSquare() {
        val square = Square(0.0, 0.0, 10.0)
        val complex = Complex(listOf(square))

        assertEquals(100.0, complex.area)
        // containsPoint checks vertices, not interior
        assertTrue(complex.containsPoint(0.0, 0.0), "Vertex should be contained")
        assertTrue(complex.containsPoint(10.0, 0.0), "Vertex should be contained")
    }

    @Test
    fun testAreaCalculationWithZeroAreaPolygon() {
        // Degenerate polygon (all points collinear) has zero area
        val points = PointArrayList(
            Point(0.0, 0.0),
            Point(5.0, 0.0),
            Point(10.0, 0.0)
        )
        val degenerate = Polygon(points)
        val rect = Rectangle(10.0, 10.0, 10.0, 10.0)  // Area = 100
        val complex = Complex(listOf(degenerate, rect))

        assertEquals(100.0, complex.area, 0.01, "Degenerate polygon should contribute 0 area")
    }

    @Test
    fun testComplexClosedProperty() {
        val rect = Rectangle(0.0, 0.0, 10.0, 10.0)
        val complex = Complex(listOf(rect))

        assertFalse(complex.closed, "Complex is defined as not closed")
    }

    @Test
    fun testComplexWithManyPolygons() {
        val polygons = (0..9).map { i ->
            Rectangle(i * 20.0, 0.0, 10.0, 10.0)
        }
        val complex = Complex(polygons)

        assertEquals(10, complex.items.size)
        assertEquals(1000.0, complex.area, "10 rectangles * 100 area each")
        assertEquals(40, complex.points.size, "10 rectangles * 4 points each")
    }
}
