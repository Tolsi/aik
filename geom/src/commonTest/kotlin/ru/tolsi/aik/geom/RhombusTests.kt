package ru.tolsi.aik.geom

import ru.tolsi.aik.geom.math.allAlmostEqual
import ru.tolsi.aik.geom.math.almostEquals
import ru.tolsi.aik.geom.math.almostZero
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class RhombusTests {
    @Test
    fun testFromDiagonals() {
        val center = Point(0.0, 0.0)
        val rhombus = Rhombus.fromDiagonals(center, 8.0, 6.0)

        assertEquals(0.0, center.x)
        assertEquals(0.0, center.y)
        assertTrue(almostEquals(8.0, rhombus.diagonal1))
        assertTrue(almostEquals(6.0, rhombus.diagonal2))
    }

    @Test
    fun testFromDiagonalsVertices() {
        val center = Point(0.0, 0.0)
        val rhombus = Rhombus.fromDiagonals(center, 8.0, 6.0)

        // After CCW orientation fix, edge order changed: p1 and p3 are swapped
        assertEquals(-4.0, rhombus.p0.x)
        assertEquals(0.0, rhombus.p0.y)
        assertEquals(0.0, rhombus.p1.x)
        assertEquals(-3.0, rhombus.p1.y)  // Swapped with p3
        assertEquals(4.0, rhombus.p2.x)
        assertEquals(0.0, rhombus.p2.y)
        assertEquals(0.0, rhombus.p3.x)
        assertEquals(3.0, rhombus.p3.y)  // Swapped with p1
    }

    @Test
    fun testEqualSides() {
        val center = Point(0.0, 0.0)
        val rhombus = Rhombus.fromDiagonals(center, 8.0, 6.0)

        val sides = listOf(
            rhombus.p0.distanceTo(rhombus.p1),
            rhombus.p1.distanceTo(rhombus.p2),
            rhombus.p2.distanceTo(rhombus.p3),
            rhombus.p3.distanceTo(rhombus.p0)
        )

        assertTrue(allAlmostEqual(sides))
        assertTrue(almostEquals(5.0, sides[0])) // 3-4-5 triangle
    }

    @Test
    fun testSideProperty() {
        val center = Point(0.0, 0.0)
        val rhombus = Rhombus.fromDiagonals(center, 8.0, 6.0)

        assertTrue(almostEquals(5.0, rhombus.side))
    }

    @Test
    fun testPerpendicularDiagonals() {
        val center = Point(0.0, 0.0)
        val rhombus = Rhombus.fromDiagonals(center, 8.0, 6.0)

        val diag1 = rhombus.p2 - rhombus.p0
        val diag2 = rhombus.p3 - rhombus.p1
        val dot = diag1.x * diag2.x + diag1.y * diag2.y

        assertTrue(almostZero(abs(dot)))
    }

    @Test
    fun testAreaFromDiagonals() {
        val center = Point(0.0, 0.0)
        val rhombus = Rhombus.fromDiagonals(center, 8.0, 6.0)

        // Area = (d1 * d2) / 2 = (8 * 6) / 2 = 24
        assertTrue(almostEquals(24.0, rhombus.area))
    }

    @Test
    fun testFromPoints() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(3.0, 4.0)
        val p2 = Point(6.0, 0.0)
        val p3 = Point(3.0, -4.0)

        val rhombus = Rhombus.fromPoints(p0, p1, p2, p3)

        // After CCW orientation fix, edge order changed: p1 and p3 are swapped
        assertEquals(0.0, rhombus.p0.x)
        assertEquals(0.0, rhombus.p0.y)
        assertEquals(3.0, rhombus.p1.x)
        assertEquals(-4.0, rhombus.p1.y)  // Now p3 from input
    }

    @Test
    fun testFromPointsValidation() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(3.0, 4.0)
        val p2 = Point(6.0, 0.0)
        val p3 = Point(3.0, -4.1) // Not equal sides

        assertFailsWith<IllegalArgumentException> {
            Rhombus.fromPoints(p0, p1, p2, p3, validate = true)
        }
    }

    @Test
    fun testFromPointsNonPerpendicularDiagonals() {
        val p0 = Point(0.0, 0.0)
        val p1 = Point(4.0, 2.0)
        val p2 = Point(8.0, 0.0)
        val p3 = Point(4.0, -2.0)

        // This is a rhombus with all sides equal
        val rhombus = Rhombus.fromPoints(p0, p1, p2, p3, validate = false)

        val sides = listOf(
            rhombus.p0.distanceTo(rhombus.p1),
            rhombus.p1.distanceTo(rhombus.p2),
            rhombus.p2.distanceTo(rhombus.p3),
            rhombus.p3.distanceTo(rhombus.p0)
        )
        assertTrue(allAlmostEqual(sides))
    }

    @Test
    fun testContainsPoint() {
        val center = Point(0.0, 0.0)
        val rhombus = Rhombus.fromDiagonals(center, 8.0, 6.0)

        assertTrue(rhombus.containsPoint(0.0, 0.0))
        assertTrue(rhombus.containsPoint(1.0, 1.0))
    }

    @Test
    fun testFromAngle() {
        val center = Point(0.0, 0.0)
        val rhombus = Rhombus(center, 5.0, Angle.fromRadians(kotlin.math.PI / 2))

        val sides = listOf(
            rhombus.p0.distanceTo(rhombus.p1),
            rhombus.p1.distanceTo(rhombus.p2),
            rhombus.p2.distanceTo(rhombus.p3),
            rhombus.p3.distanceTo(rhombus.p0)
        )

        assertTrue(allAlmostEqual(sides))
    }

    @Test
    fun testFromSideAndHeight() {
        val base = Point(0.0, 0.0)
        val rhombus = Rhombus.fromSideAndHeight(base, 5.0, 4.0)

        val sides = listOf(
            rhombus.p0.distanceTo(rhombus.p1),
            rhombus.p1.distanceTo(rhombus.p2),
            rhombus.p2.distanceTo(rhombus.p3),
            rhombus.p3.distanceTo(rhombus.p0)
        )

        assertTrue(allAlmostEqual(sides))
        assertTrue(almostEquals(5.0, sides[0]))
    }

    @Test
    fun testDiagonalProperties() {
        val center = Point(0.0, 0.0)
        val rhombus = Rhombus.fromDiagonals(center, 10.0, 8.0)

        assertTrue(almostEquals(10.0, rhombus.diagonal1))
        assertTrue(almostEquals(8.0, rhombus.diagonal2))
    }

    @Test
    fun testIntVariant() {
        val center = Point(0.0, 0.0)
        val rhombus = Rhombus.fromDiagonals(center, 8.5, 6.7)

        val rhombusInt = rhombus.int
        assertEquals(8, rhombusInt.diagonal1)
        assertEquals(6, rhombusInt.diagonal2)
    }

    @Test
    fun testIntVariantFromDiagonals() {
        val center = PointInt(0, 0)
        val rhombusInt = RhombusInt.fromDiagonals(center, 8, 6)

        assertEquals(8, rhombusInt.diagonal1)
        assertEquals(6, rhombusInt.diagonal2)
        assertEquals(5, rhombusInt.side)
    }

    @Test
    fun testIntVariantFromPoints() {
        val p0 = PointInt(0, 0)
        val p1 = PointInt(3, 4)
        val p2 = PointInt(6, 0)
        val p3 = PointInt(3, -4)

        val rhombusInt = RhombusInt.fromPoints(p0, p1, p2, p3)

        // After CCW orientation fix, edge order changed: p1 and p3 are swapped
        assertEquals(0, rhombusInt.p0.x)
        assertEquals(0, rhombusInt.p0.y)
        assertEquals(3, rhombusInt.p1.x)
        assertEquals(-4, rhombusInt.p1.y)  // Now p3 from input
    }

    @Test
    fun testIntToFloat() {
        val center = PointInt(0, 0)
        val rhombusInt = RhombusInt.fromDiagonals(center, 8, 6)

        val rhombus = rhombusInt.float
        assertTrue(almostEquals(8.0, rhombus.diagonal1))
        assertTrue(almostEquals(6.0, rhombus.diagonal2))
    }

    @Test
    fun testCounterClockwiseOrientation() {
        val rhombus = Rhombus.fromDiagonals(Point(0.0, 0.0), 8.0, 6.0)
        val points = rhombus.points

        var signedArea = 0.0
        var j = points.size - 1
        for (i in points.indices) {
            signedArea += (points.getX(j) + points.getX(i)) * (points.getY(j) - points.getY(i))
            j = i
        }
        signedArea /= 2.0

        assertTrue(
            signedArea > 0,
            "Rhombus must have counter-clockwise orientation (positive signed area), got $signedArea"
        )
    }

    @Test
    fun testClosureAndContinuity() {
        val rhombus = Rhombus.fromDiagonals(Point(0.0, 0.0), 8.0, 6.0)
        val points = rhombus.points

        assertTrue(points.size == 4, "Rhombus should have 4 points")
        assertTrue(rhombus.closed, "Rhombus should be marked as closed")

        for (i in 0 until points.size) {
            val p1 = Point(points.getX(i), points.getY(i))
            val p2 = Point(points.getX((i + 1) % points.size), points.getY((i + 1) % points.size))
            val distance = p1.distanceTo(p2)

            assertTrue(
                distance > Geometry.EPS,
                "Consecutive points should not be duplicates at indices $i and ${(i + 1) % points.size}"
            )
        }
    }
}
