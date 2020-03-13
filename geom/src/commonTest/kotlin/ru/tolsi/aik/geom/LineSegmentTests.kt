package ru.tolsi.aik.geom

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LineSegmentTests {
    @Test
    fun intersectsPointTest1() {
        assertFalse(LineSegment(Point(1, 1), Point(2, 2)).intersects(Point(0, 0)))
    }

    @Test
    fun intersectsPointTest2() {
        assertFalse(LineSegment(Point(1, 1), Point(2, 2)).intersects(Point(3, 3)))
    }

    @Test
    fun intersectsPointTest3() {
        assertTrue(
            LineSegment(Point(1, 1), Point(2, 2))
                .intersects(Point(2 + Geometry.EPS * 0.4, 2 + Geometry.EPS * 0.4)))
    }

    @Test
    fun intersectsPointTest4() {
        assertTrue(LineSegment(Point(1, 1), Point(2, 2)).intersects(Point(2, 2 + Geometry.EPS * 0.9)))
    }

    @Test
    fun intersectsPointTest5() {
        assertTrue(LineSegment(Point(1, 1), Point(2, 2)).intersects(Point(2 + Geometry.EPS * 0.9, 2)))
    }

    @Test
    fun intersectsPointTest6() {
        assertTrue(
            LineSegment(Point(1, 1), Point(2, 2))
                .intersects(Point(1 - Geometry.EPS * 0.4, 1 - Geometry.EPS * 0.4)))
    }

    @Test
    fun intersectsPointTest7() {
        assertFalse(
            LineSegment(Point(1, 1), Point(2, 2))
                .intersects(Point(1 - Geometry.EPS * 0.9, 1 - Geometry.EPS * 0.9)))
    }

    @Test
    fun intersectsPointTest8() {
        assertTrue(LineSegment(Point(1, 1), Point(2, 2)).intersects(Point(1 - Geometry.EPS * 0.9, 1)))
    }

    @Test
    fun intersectsPointTest9() {
        assertTrue(LineSegment(Point(1, 1), Point(2, 2)).intersects(Point(1, 1 - Geometry.EPS * 0.9)))
    }

    @Test
    fun intersectsLineTest1() {
        assertNull(LineSegment(Point(1, 1), Point(2, 2)).intersects(Line(Point(2, 3), Point(3, 2))))
        assertNull(LineSegment(Point(1, 1), Point(2, 2)).intersects(Ray(Point(2, 3), Point(3, 2))))
        assertNull(LineSegment(Point(1, 1), Point(2, 2)).intersects(LineSegment(Point(2, 3), Point(3, 2))))
    }

    @Test
    fun intersectsLineTest2() {
        assertNull(LineSegment(Point(1, 1), Point(2, 2)).intersects(Line(Point(0, 1), Point(1, 0))))
        assertNull(LineSegment(Point(1, 1), Point(2, 2)).intersects(Ray(Point(0, 1), Point(1, 0))))
        assertNull(LineSegment(Point(1, 1), Point(2, 2)).intersects(LineSegment(Point(0, 1), Point(1, 0))))
    }

    @Test
    fun intersectsLineTest3() {
        assertNull(LineSegment(Point(1, 1), Point(2, 2)).intersects(Line(Point(3, 3), Point(4, 4))))
        assertNull(LineSegment(Point(1, 1), Point(2, 2)).intersects(Ray(Point(3, 3), Point(4, 4))))
        assertNull(LineSegment(Point(1, 1), Point(2, 2)).intersects(LineSegment(Point(3, 3), Point(4, 4))))
    }
    // todo close to eps tests by points
    // todo close to eps tests by line
}