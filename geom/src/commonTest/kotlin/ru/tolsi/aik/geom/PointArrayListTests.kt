package ru.tolsi.aik.geom

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class PointArrayListTests {
    @Test
    fun testConstructorEmpty() {
        val list = PointArrayList()
        assertEquals(0, list.size)
        assertTrue(list.isEmpty())
    }

    @Test
    fun testConstructorWithCapacity() {
        val list = PointArrayList(10)
        assertEquals(0, list.size)
    }

    @Test
    fun testConstructorWithCallback() {
        val list = PointArrayList {
            add(0.0, 0.0)
            add(10.0, 10.0)
        }
        assertEquals(2, list.size)
    }

    @Test
    fun testConstructorFromList() {
        val points = listOf(Point(0.0, 0.0), Point(10.0, 10.0))
        val list = PointArrayList(points)

        assertEquals(2, list.size)
        assertEquals(0.0, list.getX(0), 1e-9)
        assertEquals(10.0, list.getX(1), 1e-9)
    }

    @Test
    fun testConstructorFromVarargs() {
        val list = PointArrayList(Point(0.0, 0.0), Point(10.0, 10.0), Point(20.0, 20.0))

        assertEquals(3, list.size)
    }

    @Test
    fun testAddXY() {
        val list = PointArrayList()
        list.add(5.0, 10.0)

        assertEquals(1, list.size)
        assertEquals(5.0, list.getX(0), 1e-9)
        assertEquals(10.0, list.getY(0), 1e-9)
    }

    @Test
    fun testAddXYNumber() {
        val list = PointArrayList()
        list.add(5, 10)

        assertEquals(1, list.size)
        assertEquals(5.0, list.getX(0), 1e-9)
        assertEquals(10.0, list.getY(0), 1e-9)
    }

    @Test
    fun testAddPoint() {
        val list = PointArrayList()
        list.add(Point(5.0, 10.0))

        assertEquals(1, list.size)
        assertEquals(5.0, list.getX(0), 1e-9)
        assertEquals(10.0, list.getY(0), 1e-9)
    }

    @Test
    fun testAddPointArrayList() {
        val list1 = PointArrayList(Point(0.0, 0.0), Point(10.0, 10.0))
        val list2 = PointArrayList(Point(20.0, 20.0))

        list2.add(list1)

        assertEquals(3, list2.size)
        assertEquals(20.0, list2.getX(0), 1e-9)
        assertEquals(0.0, list2.getX(1), 1e-9)
        assertEquals(10.0, list2.getX(2), 1e-9)
    }

    @Test
    fun testGetX() {
        val list = PointArrayList(Point(5.0, 10.0))
        assertEquals(5.0, list.getX(0), 1e-9)
    }

    @Test
    fun testGetY() {
        val list = PointArrayList(Point(5.0, 10.0))
        assertEquals(10.0, list.getY(0), 1e-9)
    }

    @Test
    fun testGet() {
        val list = PointArrayList(Point(5.0, 10.0))
        val point = list.get(0)

        assertEquals(5.0, point.x, 1e-9)
        assertEquals(10.0, point.y, 1e-9)
    }

    @Test
    fun testGetPoint() {
        val list = PointArrayList(Point(5.0, 10.0))
        val point = list.getPoint(0)

        assertEquals(5.0, point.x, 1e-9)
        assertEquals(10.0, point.y, 1e-9)
    }

    @Test
    fun testGetIPoint() {
        val list = PointArrayList(Point(5.0, 10.0))
        val point = list.getIPoint(0)

        assertEquals(5.0, point.x, 1e-9)
        assertEquals(10.0, point.y, 1e-9)
    }

    @Test
    fun testSetX() {
        val list = PointArrayList(Point(5.0, 10.0))
        list.setX(0, 99.0)

        assertEquals(99.0, list.getX(0), 1e-9)
        assertEquals(10.0, list.getY(0), 1e-9)
    }

    @Test
    fun testSetXNumber() {
        val list = PointArrayList(Point(5.0, 10.0))
        list.setX(0, 99)

        assertEquals(99.0, list.getX(0), 1e-9)
    }

    @Test
    fun testSetY() {
        val list = PointArrayList(Point(5.0, 10.0))
        list.setY(0, 99.0)

        assertEquals(5.0, list.getX(0), 1e-9)
        assertEquals(99.0, list.getY(0), 1e-9)
    }

    @Test
    fun testSetYNumber() {
        val list = PointArrayList(Point(5.0, 10.0))
        list.setY(0, 99)

        assertEquals(99.0, list.getY(0), 1e-9)
    }

    @Test
    fun testSetXY() {
        val list = PointArrayList(Point(5.0, 10.0))
        list.setXY(0, 99.0, 88.0)

        assertEquals(99.0, list.getX(0), 1e-9)
        assertEquals(88.0, list.getY(0), 1e-9)
    }

    @Test
    fun testSetXYNumber() {
        val list = PointArrayList(Point(5.0, 10.0))
        list.setXY(0, 99, 88)

        assertEquals(99.0, list.getX(0), 1e-9)
        assertEquals(88.0, list.getY(0), 1e-9)
    }

    @Test
    fun testContains() {
        val list = PointArrayList(Point(5.0, 10.0), Point(15.0, 20.0))

        assertTrue(list.contains(5.0, 10.0))
        assertTrue(list.contains(15.0, 20.0))
        assertFalse(list.contains(99.0, 99.0))
    }

    @Test
    fun testContainsNumber() {
        val list = PointArrayList(Point(5.0, 10.0))

        assertTrue(list.contains(5, 10))
        assertFalse(list.contains(99, 99))
    }

    @Test
    fun testSwap() {
        val list = PointArrayList(Point(0.0, 0.0), Point(10.0, 10.0))
        list.swap(0, 1)

        assertEquals(10.0, list.getX(0), 1e-9)
        assertEquals(10.0, list.getY(0), 1e-9)
        assertEquals(0.0, list.getX(1), 1e-9)
        assertEquals(0.0, list.getY(1), 1e-9)
    }

    @Test
    fun testReverse() {
        val list = PointArrayList(Point(0.0, 0.0), Point(10.0, 10.0), Point(20.0, 20.0))
        list.reverse()

        assertEquals(20.0, list.getX(0), 1e-9)
        assertEquals(10.0, list.getX(1), 1e-9)
        assertEquals(0.0, list.getX(2), 1e-9)
    }

    @Test
    fun testSort() {
        val list = PointArrayList(Point(20.0, 20.0), Point(0.0, 0.0), Point(10.0, 10.0))
        list.sort()

        // Should be sorted by x, then y
        assertEquals(0.0, list.getX(0), 1e-9)
        assertEquals(10.0, list.getX(1), 1e-9)
        assertEquals(20.0, list.getX(2), 1e-9)
    }

    @Test
    fun testToString() {
        val list = PointArrayList(Point(5.0, 10.0))
        val str = list.toString()

        assertTrue(str.contains("5"))
        assertTrue(str.contains("10"))
    }

    @Test
    fun testToPoints() {
        val list = PointArrayList(Point(5.0, 10.0), Point(15.0, 20.0))
        val points = list.toPoints()

        assertEquals(2, points.size)
        assertEquals(5.0, points[0].x, 1e-9)
        assertEquals(15.0, points[1].x, 1e-9)
    }

    @Test
    fun testToIPoints() {
        val list = PointArrayList(Point(5.0, 10.0), Point(15.0, 20.0))
        val points = list.toIPoints()

        assertEquals(2, points.size)
        assertEquals(5.0, points[0].x, 1e-9)
        assertEquals(15.0, points[1].x, 1e-9)
    }

    @Test
    fun testIsNotEmpty() {
        val list = PointArrayList(Point(5.0, 10.0))
        assertTrue(list.isNotEmpty())

        val empty = PointArrayList()
        assertFalse(empty.isNotEmpty())
    }

    @Test
    fun testFlatten() {
        val list1 = PointArrayList(Point(0.0, 0.0), Point(10.0, 10.0))
        val list2 = PointArrayList(Point(20.0, 20.0))
        val list3 = PointArrayList(Point(30.0, 30.0), Point(40.0, 40.0))

        val flattened = listOf(list1, list2, list3).flatten()

        assertEquals(5, flattened.size)
        assertEquals(0.0, flattened.getX(0), 1e-9)
        assertEquals(40.0, flattened.getX(4), 1e-9)
    }

    // PointIntArrayList tests
    @Test
    fun testPointIntArrayListConstructor() {
        val list = PointIntArrayList()
        assertEquals(0, list.size)
    }

    @Test
    fun testPointIntArrayListAdd() {
        val list = PointIntArrayList()
        list.add(5, 10)

        assertEquals(1, list.size)
        assertEquals(5, list.getX(0))
        assertEquals(10, list.getY(0))
    }

    @Test
    fun testPointIntArrayListAddPoint() {
        val list = PointIntArrayList()
        list.add(PointInt(5, 10))

        assertEquals(1, list.size)
        assertEquals(5, list.getX(0))
    }

    @Test
    fun testPointIntArrayListSwap() {
        val list = PointIntArrayList()
        list.add(0, 0)
        list.add(10, 10)
        list.swap(0, 1)

        assertEquals(10, list.getX(0))
        assertEquals(0, list.getX(1))
    }

    @Test
    fun testPointIntArrayListReverse() {
        val list = PointIntArrayList()
        list.add(0, 0)
        list.add(10, 10)
        list.add(20, 20)
        list.reverse()

        assertEquals(20, list.getX(0))
        assertEquals(10, list.getX(1))
        assertEquals(0, list.getX(2))
    }

    @Test
    fun testPointIntArrayListSort() {
        val list = PointIntArrayList()
        list.add(20, 20)
        list.add(0, 0)
        list.add(10, 10)
        list.sort()

        assertEquals(0, list.getX(0))
        assertEquals(10, list.getX(1))
        assertEquals(20, list.getX(2))
    }

    @Test
    fun testPointIntArrayListContains() {
        val list = PointIntArrayList()
        list.add(5, 10)

        assertTrue(list.contains(5, 10))
        assertFalse(list.contains(99, 99))
    }

    @Test
    fun testPointIntArrayListToString() {
        val list = PointIntArrayList()
        list.add(5, 10)
        val str = list.toString()

        assertTrue(str.contains("5"))
        assertTrue(str.contains("10"))
    }
}
