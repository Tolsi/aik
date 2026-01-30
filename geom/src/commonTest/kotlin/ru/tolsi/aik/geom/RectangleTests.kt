package ru.tolsi.aik.geom

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class RectangleTests {
    @Test
    fun testConstructorBasic() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)

        assertEquals(10.0, rect.x, 1e-9)
        assertEquals(20.0, rect.y, 1e-9)
        assertEquals(100.0, rect.width, 1e-9)
        assertEquals(50.0, rect.height, 1e-9)
    }

    @Test
    fun testConstructorOperatorInvoke() {
        val rect = Rectangle(10, 20, 100, 50)

        assertEquals(10.0, rect.x, 1e-9)
        assertEquals(20.0, rect.y, 1e-9)
        assertEquals(100.0, rect.width, 1e-9)
        assertEquals(50.0, rect.height, 1e-9)
    }

    @Test
    fun testConstructorEmpty() {
        val rect = Rectangle()

        assertEquals(0.0, rect.x, 1e-9)
        assertEquals(0.0, rect.y, 1e-9)
        assertEquals(0.0, rect.width, 1e-9)
        assertEquals(0.0, rect.height, 1e-9)
    }

    @Test
    fun testFromBounds() {
        val rect = Rectangle.fromBounds(10, 20, 110, 70)

        assertEquals(10.0, rect.x, 1e-9)
        assertEquals(20.0, rect.y, 1e-9)
        assertEquals(100.0, rect.width, 1e-9)
        assertEquals(50.0, rect.height, 1e-9)
    }

    @Test
    fun testLeftTopRightBottom() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)

        assertEquals(10.0, rect.left, 1e-9)
        assertEquals(20.0, rect.top, 1e-9)
        assertEquals(110.0, rect.right, 1e-9)
        assertEquals(70.0, rect.bottom, 1e-9)
    }

    @Test
    fun testSetLeft() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)
        rect.left = 5.0

        assertEquals(5.0, rect.x, 1e-9)
    }

    @Test
    fun testSetTop() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)
        rect.top = 15.0

        assertEquals(15.0, rect.y, 1e-9)
    }

    @Test
    fun testSetRight() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)
        rect.right = 120.0

        assertEquals(110.0, rect.width, 1e-9)
    }

    @Test
    fun testSetBottom() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)
        rect.bottom = 80.0

        assertEquals(60.0, rect.height, 1e-9)
    }

    @Test
    fun testArea() {
        val rect = Rectangle(0.0, 0.0, 10.0, 5.0)
        assertEquals(50.0, rect.area, 1e-9)
    }

    @Test
    fun testIsEmpty() {
        val empty = Rectangle(0.0, 0.0, 0.0, 0.0)
        assertTrue(empty.isEmpty)

        val notEmpty = Rectangle(0.0, 0.0, 10.0, 10.0)
        assertFalse(notEmpty.isEmpty)
    }

    @Test
    fun testIsNotEmpty() {
        val empty = Rectangle(0.0, 0.0, 0.0, 0.0)
        assertFalse(empty.isNotEmpty)

        val notEmpty = Rectangle(0.0, 0.0, 10.0, 10.0)
        assertTrue(notEmpty.isNotEmpty)
    }

    @Test
    fun testPosition() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)
        val pos = rect.position

        assertEquals(10.0, pos.x, 1e-9)
        assertEquals(20.0, pos.y, 1e-9)
    }

    @Test
    fun testSize() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)
        val size = rect.size

        assertEquals(100.0, size.width, 1e-9)
        assertEquals(50.0, size.height, 1e-9)
    }

    @Test
    fun testSetTo() {
        val rect = Rectangle()
        rect.setTo(10.0, 20.0, 100.0, 50.0)

        assertEquals(10.0, rect.x, 1e-9)
        assertEquals(20.0, rect.y, 1e-9)
        assertEquals(100.0, rect.width, 1e-9)
        assertEquals(50.0, rect.height, 1e-9)
    }

    @Test
    fun testSetToNumber() {
        val rect = Rectangle()
        rect.setTo(10, 20, 100, 50)

        assertEquals(10.0, rect.x, 1e-9)
        assertEquals(20.0, rect.y, 1e-9)
        assertEquals(100.0, rect.width, 1e-9)
        assertEquals(50.0, rect.height, 1e-9)
    }

    @Test
    fun testCopyFrom() {
        val rect1 = Rectangle(10.0, 20.0, 100.0, 50.0)
        val rect2 = Rectangle()
        rect2.copyFrom(rect1)

        assertEquals(rect1.x, rect2.x, 1e-9)
        assertEquals(rect1.y, rect2.y, 1e-9)
        assertEquals(rect1.width, rect2.width, 1e-9)
        assertEquals(rect1.height, rect2.height, 1e-9)
    }

    @Test
    fun testSetBounds() {
        val rect = Rectangle()
        rect.setBounds(10.0, 20.0, 110.0, 70.0)

        assertEquals(10.0, rect.x, 1e-9)
        assertEquals(20.0, rect.y, 1e-9)
        assertEquals(100.0, rect.width, 1e-9)
        assertEquals(50.0, rect.height, 1e-9)
    }

    @Test
    fun testSetBoundsNumber() {
        val rect = Rectangle()
        rect.setBounds(10, 20, 110, 70)

        assertEquals(10.0, rect.x, 1e-9)
        assertEquals(20.0, rect.y, 1e-9)
        assertEquals(100.0, rect.width, 1e-9)
        assertEquals(50.0, rect.height, 1e-9)
    }

    @Test
    fun testTimesOperator() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)
        val scaled = rect * 2.0

        assertEquals(20.0, scaled.x, 1e-9)
        assertEquals(40.0, scaled.y, 1e-9)
        assertEquals(200.0, scaled.width, 1e-9)
        assertEquals(100.0, scaled.height, 1e-9)
    }

    @Test
    fun testTimesOperatorNumber() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)
        val scaled = rect * 2

        assertEquals(20.0, scaled.x, 1e-9)
        assertEquals(40.0, scaled.y, 1e-9)
        assertEquals(200.0, scaled.width, 1e-9)
        assertEquals(100.0, scaled.height, 1e-9)
    }

    @Test
    fun testDivOperator() {
        val rect = Rectangle(20.0, 40.0, 200.0, 100.0)
        val scaled = rect / 2.0

        assertEquals(10.0, scaled.x, 1e-9)
        assertEquals(20.0, scaled.y, 1e-9)
        assertEquals(100.0, scaled.width, 1e-9)
        assertEquals(50.0, scaled.height, 1e-9)
    }

    @Test
    fun testDivOperatorNumber() {
        val rect = Rectangle(20.0, 40.0, 200.0, 100.0)
        val scaled = rect / 2

        assertEquals(10.0, scaled.x, 1e-9)
        assertEquals(20.0, scaled.y, 1e-9)
        assertEquals(100.0, scaled.width, 1e-9)
        assertEquals(50.0, scaled.height, 1e-9)
    }

    @Test
    fun testContainsRectangle() {
        val outer = Rectangle(0.0, 0.0, 100.0, 100.0)
        val inner = Rectangle(10.0, 10.0, 20.0, 20.0)

        assertTrue(inner in outer)
    }

    @Test
    fun testContainsRectangleNot() {
        val rect1 = Rectangle(0.0, 0.0, 50.0, 50.0)
        val rect2 = Rectangle(40.0, 40.0, 50.0, 50.0)

        assertFalse(rect2 in rect1)
    }

    @Test
    fun testContainsPoint() {
        val rect = Rectangle(0.0, 0.0, 100.0, 100.0)

        assertTrue(rect.contains(Point(50.0, 50.0)))
        assertTrue(rect.contains(Point(0.0, 0.0)))
    }

    @Test
    fun testContainsPointOutside() {
        val rect = Rectangle(0.0, 0.0, 100.0, 100.0)

        assertFalse(rect.contains(Point(-1.0, 50.0)))
        assertFalse(rect.contains(Point(101.0, 50.0)))
    }

    @Test
    fun testContainsXY() {
        val rect = Rectangle(0.0, 0.0, 100.0, 100.0)

        assertTrue(rect.contains(50.0, 50.0))
        assertTrue(rect.contains(0.0, 0.0))
    }

    @Test
    fun testContainsXYNumber() {
        val rect = Rectangle(0.0, 0.0, 100.0, 100.0)

        assertTrue(rect.contains(50, 50))
    }

    @Test
    fun testIntersects() {
        val rect1 = Rectangle(0.0, 0.0, 100.0, 100.0)
        val rect2 = Rectangle(50.0, 50.0, 100.0, 100.0)

        assertTrue(rect1 intersects rect2)
    }

    @Test
    fun testIntersectsNot() {
        val rect1 = Rectangle(0.0, 0.0, 50.0, 50.0)
        val rect2 = Rectangle(100.0, 100.0, 50.0, 50.0)

        assertFalse(rect1 intersects rect2)
    }

    @Test
    fun testIntersectsX() {
        val rect1 = Rectangle(0.0, 0.0, 100.0, 100.0)
        val rect2 = Rectangle(50.0, 200.0, 100.0, 50.0)

        assertTrue(rect1 intersectsX rect2)
    }

    @Test
    fun testIntersectsY() {
        val rect1 = Rectangle(0.0, 0.0, 100.0, 100.0)
        val rect2 = Rectangle(200.0, 50.0, 50.0, 100.0)

        assertTrue(rect1 intersectsY rect2)
    }

    @Test
    fun testIntersection() {
        val rect1 = Rectangle(0.0, 0.0, 100.0, 100.0)
        val rect2 = Rectangle(50.0, 50.0, 100.0, 100.0)

        val result = rect1 intersection rect2

        assertNotNull(result)
        assertEquals(50.0, result.x, 1e-9)
        assertEquals(50.0, result.y, 1e-9)
        assertEquals(50.0, result.width, 1e-9)
        assertEquals(50.0, result.height, 1e-9)
    }

    @Test
    fun testIntersectionNone() {
        val rect1 = Rectangle(0.0, 0.0, 50.0, 50.0)
        val rect2 = Rectangle(100.0, 100.0, 50.0, 50.0)

        val result = rect1 intersection rect2

        assertNull(result)
    }

    @Test
    fun testIntersectionWithTarget() {
        val rect1 = Rectangle(0.0, 0.0, 100.0, 100.0)
        val rect2 = Rectangle(50.0, 50.0, 100.0, 100.0)
        val target = Rectangle()

        val result = rect1.intersection(rect2, target)

        assertNotNull(result)
        assertEquals(target, result)
        assertEquals(50.0, result.x, 1e-9)
        assertEquals(50.0, result.y, 1e-9)
    }

    @Test
    fun testSetToIntersection() {
        val rect1 = Rectangle(0.0, 0.0, 100.0, 100.0)
        val rect2 = Rectangle(50.0, 50.0, 100.0, 100.0)
        val target = Rectangle()

        target.setToIntersection(rect1, rect2)

        assertEquals(50.0, target.x, 1e-9)
        assertEquals(50.0, target.y, 1e-9)
        assertEquals(50.0, target.width, 1e-9)
        assertEquals(50.0, target.height, 1e-9)
    }

    @Test
    fun testDisplaced() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)
        val displaced = rect.displaced(5.0, 10.0)

        assertEquals(15.0, displaced.x, 1e-9)
        assertEquals(30.0, displaced.y, 1e-9)
        assertEquals(100.0, displaced.width, 1e-9)
        assertEquals(50.0, displaced.height, 1e-9)

        // Original unchanged
        assertEquals(10.0, rect.x, 1e-9)
    }

    @Test
    fun testDisplacedNumber() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)
        val displaced = rect.displaced(5, 10)

        assertEquals(15.0, displaced.x, 1e-9)
        assertEquals(30.0, displaced.y, 1e-9)
    }

    @Test
    fun testDisplace() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)
        rect.displace(5.0, 10.0)

        assertEquals(15.0, rect.x, 1e-9)
        assertEquals(30.0, rect.y, 1e-9)
    }

    @Test
    fun testDisplaceNumber() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)
        rect.displace(5, 10)

        assertEquals(15.0, rect.x, 1e-9)
        assertEquals(30.0, rect.y, 1e-9)
    }

    @Test
    fun testInflate() {
        val rect = Rectangle(10.0, 10.0, 80.0, 80.0)
        rect.inflate(10.0, 10.0)

        assertEquals(0.0, rect.x, 1e-9)
        assertEquals(0.0, rect.y, 1e-9)
        assertEquals(100.0, rect.width, 1e-9)
        assertEquals(100.0, rect.height, 1e-9)
    }

    @Test
    fun testInflateNumber() {
        val rect = Rectangle(10.0, 10.0, 80.0, 80.0)
        rect.inflate(10, 10)

        assertEquals(0.0, rect.x, 1e-9)
        assertEquals(0.0, rect.y, 1e-9)
        assertEquals(100.0, rect.width, 1e-9)
        assertEquals(100.0, rect.height, 1e-9)
    }

    @Test
    fun testClone() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)
        val cloned = rect.clone()

        assertEquals(rect.x, cloned.x, 1e-9)
        assertEquals(rect.y, cloned.y, 1e-9)
        assertEquals(rect.width, cloned.width, 1e-9)
        assertEquals(rect.height, cloned.height, 1e-9)

        // Verify independent
        cloned.x = 999.0
        assertEquals(10.0, rect.x, 1e-9)
    }

    @Test
    fun testToString() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)
        val str = rect.toString()

        assertTrue(str.contains("Rectangle"))
        assertTrue(str.contains("x="))
        assertTrue(str.contains("y="))
        assertTrue(str.contains("width="))
        assertTrue(str.contains("height="))
    }

    @Test
    fun testToStringBounds() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)
        val str = rect.toStringBounds()

        assertTrue(str.contains("Rectangle"))
        assertTrue(str.contains("10"))
        assertTrue(str.contains("20"))
        assertTrue(str.contains("110"))
        assertTrue(str.contains("70"))
    }

    @Test
    fun testPointsProperty() {
        val rect = Rectangle(0.0, 0.0, 10.0, 10.0)
        val points = rect.points

        assertEquals(4, points.size)

        // Check all four corners
        assertEquals(0.0, points.getX(0), 1e-9)
        assertEquals(0.0, points.getY(0), 1e-9)

        assertEquals(0.0, points.getX(1), 1e-9)
        assertEquals(10.0, points.getY(1), 1e-9)

        assertEquals(10.0, points.getX(2), 1e-9)
        assertEquals(10.0, points.getY(2), 1e-9)

        assertEquals(10.0, points.getX(3), 1e-9)
        assertEquals(0.0, points.getY(3), 1e-9)
    }

    @Test
    fun testClosedProperty() {
        val rect = Rectangle(0.0, 0.0, 10.0, 10.0)
        assertTrue(rect.closed)
    }

    @Test
    fun testContainsPointMethod() {
        val rect = Rectangle(0.0, 0.0, 100.0, 100.0)

        assertTrue(rect.containsPoint(50.0, 50.0))
        assertFalse(rect.containsPoint(150.0, 150.0))
    }

    @Test
    fun testToInt() {
        val rect = Rectangle(10.5, 20.5, 100.5, 50.5)
        val rectInt = rect.toInt()

        assertEquals(10, rectInt.x)
        assertEquals(20, rectInt.y)
        assertEquals(100, rectInt.width)
        assertEquals(50, rectInt.height)
    }

    // RectangleInt tests
    @Test
    fun testRectangleIntConstructor() {
        val rectInt = RectangleInt(10, 20, 100, 50)

        assertEquals(10, rectInt.x)
        assertEquals(20, rectInt.y)
        assertEquals(100, rectInt.width)
        assertEquals(50, rectInt.height)
    }

    @Test
    fun testRectangleIntEmpty() {
        val rectInt = RectangleInt()

        assertEquals(0, rectInt.x)
        assertEquals(0, rectInt.y)
        assertEquals(0, rectInt.width)
        assertEquals(0, rectInt.height)
    }

    @Test
    fun testRectangleIntFromBounds() {
        val rectInt = RectangleInt.fromBounds(10, 20, 110, 70)

        assertEquals(10, rectInt.x)
        assertEquals(20, rectInt.y)
        assertEquals(100, rectInt.width)
        assertEquals(50, rectInt.height)
    }

    @Test
    fun testRectangleIntLeftTopRightBottom() {
        val rectInt = RectangleInt(10, 20, 100, 50)

        assertEquals(10, rectInt.left)
        assertEquals(20, rectInt.top)
        assertEquals(110, rectInt.right)
        assertEquals(70, rectInt.bottom)
    }

    @Test
    fun testRectangleIntSetTo() {
        val rectInt = RectangleInt()
        rectInt.setTo(10, 20, 100, 50)

        assertEquals(10, rectInt.x)
        assertEquals(20, rectInt.y)
        assertEquals(100, rectInt.width)
        assertEquals(50, rectInt.height)
    }

    @Test
    fun testRectangleIntSetPosition() {
        val rectInt = RectangleInt(10, 20, 100, 50)
        rectInt.setPosition(30, 40)

        assertEquals(30, rectInt.x)
        assertEquals(40, rectInt.y)
    }

    @Test
    fun testRectangleIntSetSize() {
        val rectInt = RectangleInt(10, 20, 100, 50)
        rectInt.setSize(200, 100)

        assertEquals(200, rectInt.width)
        assertEquals(100, rectInt.height)
    }

    @Test
    fun testRectangleIntSetBoundsTo() {
        val rectInt = RectangleInt()
        rectInt.setBoundsTo(10, 20, 110, 70)

        assertEquals(10, rectInt.x)
        assertEquals(20, rectInt.y)
        assertEquals(100, rectInt.width)
        assertEquals(50, rectInt.height)
    }

    @Test
    fun testRectangleIntContainsSizeInt() {
        val rectInt = RectangleInt(0, 0, 100, 100)
        val sizeInt = SizeInt(50, 50)

        assertTrue(sizeInt in rectInt)
    }

    @Test
    fun testRectangleIntToString() {
        val rectInt = RectangleInt(10, 20, 100, 50)
        val str = rectInt.toString()

        assertTrue(str.contains("Rectangle"))
        assertTrue(str.contains("x="))
        assertTrue(str.contains("y="))
        assertTrue(str.contains("width="))
        assertTrue(str.contains("height="))
    }

    @Test
    fun testRectangleAsInt() {
        val rect = Rectangle(10.5, 20.5, 100.5, 50.5)
        val rectInt = rect.asInt()

        assertEquals(10, rectInt.x)
        assertEquals(20, rectInt.y)
    }

    @Test
    fun testRectangleIntAsDouble() {
        val rectInt = RectangleInt(10, 20, 100, 50)
        val rect = rectInt.asDouble()

        assertEquals(10.0, rect.x, 1e-9)
        assertEquals(20.0, rect.y, 1e-9)
    }

    @Test
    fun testRectangleIntFloatProperty() {
        val rectInt = RectangleInt(10, 20, 100, 50)
        val rect = rectInt.float

        assertEquals(10.0, rect.x, 1e-9)
        assertEquals(20.0, rect.y, 1e-9)
    }

    @Test
    fun testRectangleIntProperty() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)
        val rectInt = rect.int

        assertEquals(10, rectInt.x)
        assertEquals(20, rectInt.y)
    }

    @Test
    fun testBoundLinesExtension() {
        val rects = listOf(
            Rectangle(0.0, 0.0, 50.0, 50.0),
            Rectangle(100.0, 100.0, 50.0, 50.0)
        )

        val bounds = rects.boundLines()

        assertEquals(0.0, bounds.left, 1e-9)
        assertEquals(0.0, bounds.top, 1e-9)
        assertEquals(150.0, bounds.right, 1e-9)
        assertEquals(150.0, bounds.bottom, 1e-9)
    }

    @Test
    fun testBottomSide() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)
        val side = rect.bottomSide()

        assertEquals(2, side.size)
    }

    @Test
    fun testTopSide() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)
        val side = rect.topSide()

        assertEquals(2, side.size)
    }

    @Test
    fun testLeftSide() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)
        val side = rect.leftSide()

        assertEquals(2, side.size)
    }

    @Test
    fun testRightSide() {
        val rect = Rectangle(10.0, 20.0, 100.0, 50.0)
        val side = rect.rightSide()

        assertEquals(2, side.size)
    }

    @Test
    fun testBoundLinesMethod() {
        val rect = Rectangle(0.0, 0.0, 100.0, 100.0)
        val lines = rect.boundLines()

        assertEquals(4, lines.size)
    }

    @Test
    fun testAsRectangleExtension() {
        val iRect: IRectangle = Rectangle(10.0, 20.0, 100.0, 50.0)
        val rect = iRect.asRectangle

        assertEquals(10.0, rect.x, 1e-9)
        assertEquals(20.0, rect.y, 1e-9)
    }
}
