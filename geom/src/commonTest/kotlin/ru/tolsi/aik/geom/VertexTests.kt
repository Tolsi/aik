package ru.tolsi.aik.geom

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class VertexTests {

    @Test
    fun testConstructorWithPoint() {
        val point = Point(1.0, 2.0)
        val vertex = Vertex(point)
        assertEquals(1.0, vertex.point.x)
        assertEquals(2.0, vertex.point.y)
        assertTrue(vertex.getAllMetadata().isEmpty())
    }

    @Test
    fun testConstructorWithCoordinates() {
        val vertex = Vertex(3.0, 4.0)
        assertEquals(3.0, vertex.point.x)
        assertEquals(4.0, vertex.point.y)
    }

    @Test
    fun testConstructorWithMetadata() {
        val metadata = mapOf("id" to 42, "name" to "vertexA")
        val vertex = Vertex(1.0, 2.0, metadata)
        assertEquals(42, vertex.getMetadata("id"))
        assertEquals("vertexA", vertex.getMetadata("name"))
    }

    @Test
    fun testSetMetadata() {
        val vertex = Vertex(0.0, 0.0)
        vertex.setMetadata("weight", 3.14)
        assertEquals(3.14, vertex.getMetadata("weight"))
    }

    @Test
    fun testGetMetadataNotPresent() {
        val vertex = Vertex(0.0, 0.0)
        assertNull(vertex.getMetadata("missing"))
    }

    @Test
    fun testHasMetadata() {
        val vertex = Vertex(0.0, 0.0)
        vertex.setMetadata("key", "value")
        assertTrue(vertex.hasMetadata("key"))
        assertFalse(vertex.hasMetadata("otherKey"))
    }

    @Test
    fun testRemoveMetadata() {
        val vertex = Vertex(0.0, 0.0, mapOf("a" to 1, "b" to 2))
        assertEquals(1, vertex.removeMetadata("a"))
        assertFalse(vertex.hasMetadata("a"))
        assertTrue(vertex.hasMetadata("b"))
    }

    @Test
    fun testClearMetadata() {
        val vertex = Vertex(0.0, 0.0, mapOf("a" to 1, "b" to 2))
        vertex.clearMetadata()
        assertTrue(vertex.getAllMetadata().isEmpty())
    }

    @Test
    fun testGetAllMetadata() {
        val initial = mapOf("x" to 10, "y" to 20)
        val vertex = Vertex(0.0, 0.0, initial)
        val retrieved = vertex.getAllMetadata()
        assertEquals(10, retrieved["x"])
        assertEquals(20, retrieved["y"])
        assertEquals(2, retrieved.size)
    }

    @Test
    fun testContainsPoint() {
        val vertex = Vertex(5.0, 10.0)
        assertTrue(vertex.containsPoint(5.0, 10.0))
        assertTrue(vertex.containsPoint(Point(5.0, 10.0)))
        assertFalse(vertex.containsPoint(5.1, 10.0))
    }

    @Test
    fun testContainsPointEpsilon() {
        val vertex = Vertex(5.0, 10.0)
        val eps = Geometry.EPS / 2.0
        assertTrue(vertex.containsPoint(5.0 + eps, 10.0))
        assertTrue(vertex.containsPoint(5.0, 10.0 + eps))
    }

    @Test
    fun testPoints() {
        val vertex = Vertex(3.0, 7.0)
        assertEquals(1, vertex.points.size)
        assertEquals(3.0, vertex.points.getX(0))
        assertEquals(7.0, vertex.points.getY(0))
    }

    @Test
    fun testClosedProperty() {
        val vertex = Vertex(0.0, 0.0)
        assertFalse(vertex.closed)
    }

    @Test
    fun testEquality() {
        val v1 = Vertex(1.0, 2.0, mapOf("id" to 1))
        val v2 = Vertex(1.0, 2.0, mapOf("id" to 1))
        val v3 = Vertex(1.0, 2.0, mapOf("id" to 2))
        val v4 = Vertex(2.0, 2.0, mapOf("id" to 1))

        assertEquals(v1, v2)
        assertNotEquals(v1, v3) // Different metadata
        assertNotEquals(v1, v4) // Different point
    }

    @Test
    fun testHashCode() {
        val v1 = Vertex(1.0, 2.0, mapOf("id" to 1))
        val v2 = Vertex(1.0, 2.0, mapOf("id" to 1))
        assertEquals(v1.hashCode(), v2.hashCode())
    }

    @Test
    fun testIntVariant() {
        val vertexInt = VertexInt(5, 10)
        assertEquals(5, vertexInt.x)
        assertEquals(10, vertexInt.y)
        assertEquals(PointInt(5, 10), vertexInt.point)
    }

    @Test
    fun testIntVariantMetadata() {
        val vertexInt = VertexInt(5, 10, mapOf("key" to "value"))
        assertEquals("value", vertexInt.getMetadata("key"))
        vertexInt.setMetadata("newKey", 42)
        assertEquals(42, vertexInt.getMetadata("newKey"))
    }

    @Test
    fun testIntConversion() {
        val vertex = Vertex(5.7, 10.3)
        val vertexInt = vertex.int
        assertEquals(5, vertexInt.x)
        assertEquals(10, vertexInt.y)
    }

    @Test
    fun testFloatConversion() {
        val vertexInt = VertexInt(5, 10)
        val vertexFloat = vertexInt.float
        assertEquals(5.0, vertexFloat.point.x)
        assertEquals(10.0, vertexFloat.point.y)
    }

    @Test
    fun testInterfaceInvoke() {
        val v1 = IVertex(3.0, 4.0)
        val v2 = IVertex(Point(5.0, 6.0), mapOf("key" to "value"))

        assertEquals(3.0, v1.point.x)
        assertEquals(4.0, v1.point.y)
        assertEquals(5.0, v2.point.x)
        assertEquals("value", v2.getMetadata("key"))
    }
}
