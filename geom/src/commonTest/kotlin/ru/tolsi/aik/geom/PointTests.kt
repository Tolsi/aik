package ru.tolsi.aik.geom

import kotlin.test.Test
import kotlin.test.assertEquals

class PointTests {

    @Test
    fun invoke0Test() {
        assertEquals(Point(), Point(0, 0))
    }

    @Test
    fun invokePointTest() {
        assertEquals(Point(1, 1), Point(Point(1, 1)))
    }

    @Test
    fun invoke1Test() {
        assertEquals(Point(2, 2), Point(2))
    }

    @Test
    fun invoke2Test() {
        val p = Point(8, 3)
        assertEquals(p.x, 8.0)
        assertEquals(p.y, 3.0)
    }

    @Test
    fun addTest() {
        assertEquals(Point(1, -1).add(Point(1)), Point(2, 0))
    }

    @Test
    fun addTest2() {
        assertEquals(Point(0, -2), Point(1, -1).add(Point(-1)))
    }

    @Test
    fun middleTest() {
        assertEquals(Point(50, 50), Point.middle(Point(0, 0), Point(100, 100)))
    }

    @Test
    fun angleBetweenVectorsTest() {
        // from https://ru.stackoverflow.com/a/869249/180062
        assertEquals(Angle.fromDegrees(23.19859051364817), Point.angleBetweenVectors(Point(1, 2.5), Point(3, 3)))
    }

    @Test
    fun angleBetweenPoints2Test() {
        assertEquals(Angle.fromDegrees(45), Point.angleBetweenPoints(0.0, 0.0, 100.0, 100.0))
    }

    @Test
    fun angleBetweenPoints3Test() {
        assertEquals(Angle.fromDegrees(225), Point.angleBetweenPoints(100.0, 100.0, 0.0, 0.0, 100.0, 100.0))
    }

    @Test
    fun compareTest() {
        assertEquals(-1, Point.compare(0.0, 0.0, 100.0, 100.0))
    }

    @Test
    fun compareTest2() {
        assertEquals(0, Point.compare(10.0, 10.0, 10.0, 10.0))
    }

    @Test
    fun compareTest3() {
        assertEquals(1, Point.compare(100.0, 100.0, 10.0, 10.0))
    }

    @Test
    fun compareTestPoints() {
        assertEquals(-1, Point.compare(Point(0.0, 0.0), Point(100.0, 100.0)))
    }

    @Test
    fun compareTestPoints2() {
        assertEquals(0, Point.compare(Point(10.0, 10.0), Point(10.0, 10.0)))
    }

    @Test
    fun compareTestPoints3() {
        assertEquals(1, Point.compare(Point(100.0, 100.0), Point(10.0, 10.0)))
    }

    @Test
    fun compareEpsTest() {
        assertEquals(-1, Point.compareEps(0.0, 0.0, 100.0, 100.0))
    }

    @Test
    fun compareEpsTest2() {
        assertEquals(0, Point.compareEps(10.0, 10.0, 10.0, 10.0))
    }

    @Test
    fun compareEpsTest3() {
        assertEquals(1, Point.compareEps(100.0, 100.0, 10.0, 10.0))
    }

    @Test
    fun compareEpsPointsTest() {
        assertEquals(-1, Point.compareEps(Point(0.0, 0.0), Point(100.0, 100.0)))
    }

    @Test
    fun compareEpsPointsTest2() {
        assertEquals(0, Point.compareEps(Point(10.0, 10.0), Point(10.0, 10.0)))
    }

    @Test
    fun compareEpsPointsTest3() {
        assertEquals(1, Point.compareEps(Point(100.0, 100.0), Point(10.0, 10.0)))
    }

    @Test
    fun compareEpsTestLess1() {
        assertEquals(-1, Point.compareEps(100.0 - Geometry.EPS, 100.0, 100.0, 100.0))
    }

    @Test
    fun compareEpsTestLess2() {
        assertEquals(-1, Point.compareEps(100.0, 100.0 - Geometry.EPS, 100.0, 100.0))
    }

    @Test
    fun compareEpsTestEquals1() {
        assertEquals(0, Point.compareEps(100.0 - Geometry.EPS + 1e-10, 100.0, 100.0, 100.0))
    }

    @Test
    fun compareEpsTestEquals2() {
        assertEquals(0, Point.compareEps(100.0, 100.0 - Geometry.EPS + 1e-10, 100.0, 100.0))
    }

    @Test
    fun compareEpsTestEquals3() {
        assertEquals(0, Point.compareEps(100.0, 100.0, 100.0 - Geometry.EPS + 1e-10, 100.0))
    }

    @Test
    fun compareEpsTestEquals4() {
        assertEquals(0, Point.compareEps(100.0, 100.0, 100.0, 100.0 - Geometry.EPS + 1e-10))
    }

    @Test
    fun compareEpsTestMore1() {
        assertEquals(1, Point.compareEps(100.0, 100.0, 100.0 - Geometry.EPS, 100.0))
    }

    @Test
    fun compareEpsTestMore2() {
        assertEquals(1, Point.compareEps(100.0, 100.0, 100.0, 100.0 - Geometry.EPS))
    }

    @Test
    fun distance1Test() {
        assertEquals(10.0, Point.distance(0.0, 10.0))
    }

    @Test
    fun distance2Test() {
        assertEquals(10.0, Point.distance(0, 0, 10.0, 0.0))
    }

    @Test
    fun distance2Test2() {
        assertEquals(14.142135623730951, Point.distance(0, 0, 10.0, 10.0))
    }

    @Test
    fun stepsTest() {
        assertEquals(200, Point.steps(Point(0, 0), Point(100.0, 100.0)))
    }

    @Test
    fun stepsTest2() {
        assertEquals(1, Point.steps(Point(0, 0), Point(0, 1)))
    }

    @Test
    fun stepsTest3() {
        assertEquals(2, Point.steps(Point(0, 0), Point(1, 1)))
    }

    @Test
    fun stepsTest4() {
        assertEquals(0, Point.steps(Point(1, 1), Point(1, 1)))
    }


}