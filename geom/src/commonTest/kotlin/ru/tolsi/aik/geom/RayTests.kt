package ru.tolsi.aik.geom

import kotlin.test.*

class RayTests {
    @Test
    fun intersectsPointTest1() {
        assertFalse(Ray(Point(1, 1), Point(2, 2)).intersects(Point(0, 0)))
    }

    @Test
    fun intersectsPointTest2() {
        assertTrue(Ray(Point(1, 1), Point(2, 2)).intersects(Point(3, 3)))
    }

    @Test
    fun intersectsPointTest3() {
        assertTrue(Ray(Point(1, 1), Point(2, 2)).intersects(Point(2 + Geometry.EPS * 0.9, 2 + Geometry.EPS * 0.9)))
    }

    @Test
    fun intersectsPointTest4() {
        assertTrue(Ray(Point(1, 1), Point(2, 2)).intersects(Point(2, 2 + Geometry.EPS * 0.9)))
    }

    @Test
    fun intersectsPointTest5() {
        assertTrue(Ray(Point(1, 1), Point(2, 2)).intersects(Point(2 + Geometry.EPS * 0.9, 2)))
    }

    @Test
    fun intersectsPointTest6() {
        assertTrue(Ray(Point(1, 1), Point(2, 2)).intersects(Point(1 - Geometry.EPS * 0.4, 1 - Geometry.EPS * 0.4)))
    }

    @Test
    fun intersectsPointTest7() {
        assertFalse(Ray(Point(1, 1), Point(2, 2)).intersects(Point(1 - Geometry.EPS * 0.9, 1 - Geometry.EPS * 0.9)))
    }

    @Test
    fun intersectsPointTest8() {
        assertTrue(Ray(Point(1, 1), Point(2, 2)).intersects(Point(1 - Geometry.EPS * 0.9, 1)))
    }

    @Test
    fun intersectsPointTest9() {
        assertTrue(Ray(Point(1, 1), Point(2, 2)).intersects(Point(1, 1 - Geometry.EPS * 0.9)))
    }

    @Test
    fun intersectsLineTest1() {
        assertNotNull(Ray(Point(1, 1), Point(2, 2)).intersects(Line(Point(2, 3), Point(3, 2))))
        assertNotNull(Ray(Point(1, 1), Point(2, 2)).intersects(Ray(Point(2, 3), Point(3, 2))))
        assertNotNull(Ray(Point(1, 1), Point(2, 2)).intersects(LineSegment(Point(2, 3), Point(3, 2))))

        assertNull(Ray(Point(2, 2), Point(1, 1)).intersects(Line(Point(2, 3), Point(3, 2))))
        assertNull(Ray(Point(2, 2), Point(1, 1)).intersects(Ray(Point(2, 3), Point(3, 2))))
        assertNull(Ray(Point(2, 2), Point(1, 1)).intersects(LineSegment(Point(2, 3), Point(3, 2))))
    }

    @Test
    fun intersectsLineTest2() {
        assertNull(Ray(Point(1, 1), Point(2, 2)).intersects(Line(Point(0, 1), Point(1, 0))))
        assertNull(Ray(Point(1, 1), Point(2, 2)).intersects(Ray(Point(0, 1), Point(1, 0))))
        assertNull(Ray(Point(1, 1), Point(2, 2)).intersects(LineSegment(Point(0, 1), Point(1, 0))))

        assertNotNull(Ray(Point(2, 2), Point(1, 1)).intersects(Line(Point(0, 1), Point(1, 0))))
        assertNotNull(Ray(Point(2, 2), Point(1, 1)).intersects(Ray(Point(0, 1), Point(1, 0))))
        assertNotNull(Ray(Point(2, 2), Point(1, 1)).intersects(LineSegment(Point(0, 1), Point(1, 0))))
    }

    @Test
    fun intersectsLineTest3() {
        assertNull(Ray(Point(1, 1), Point(2, 2)).intersects(Line(Point(3, 3), Point(4, 4))))
        assertNull(Ray(Point(1, 1), Point(2, 2)).intersects(Ray(Point(3, 3), Point(4, 4))))
        assertNull(Ray(Point(1, 1), Point(2, 2)).intersects(LineSegment(Point(3, 3), Point(4, 4))))
    }
    // todo close to eps tests by points
    // todo close to eps tests by line
}