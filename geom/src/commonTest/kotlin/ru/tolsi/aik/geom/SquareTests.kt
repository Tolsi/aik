package ru.tolsi.aik.geom

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SquareTests {
    @Test
    fun testConstruction() {
        val square = Square(0.0, 0.0, 10.0)
        assertEquals(0.0, square.x)
        assertEquals(0.0, square.y)
        assertEquals(10.0, square.side)
        assertEquals(10.0, square.width)
        assertEquals(10.0, square.height)
    }

    @Test
    fun testConstructionWithNumbers() {
        val square = Square(5, 10, 20)
        assertEquals(5.0, square.x)
        assertEquals(10.0, square.y)
        assertEquals(20.0, square.side)
    }

    @Test
    fun testFromCenter() {
        val center = Point(10.0, 10.0)
        val square = Square.fromCenter(center, 20.0)
        assertEquals(0.0, square.x)
        assertEquals(0.0, square.y)
        assertEquals(20.0, square.side)
    }

    @Test
    fun testArea() {
        val square = Square(0.0, 0.0, 5.0)
        assertEquals(25.0, square.area)
    }

    @Test
    fun testWidthHeightConstraint() {
        val square = Square(0.0, 0.0, 10.0)
        assertEquals(square.width, square.height)
        assertEquals(square.side, square.width)

        // Changing width should change side
        square.width = 20.0
        assertEquals(20.0, square.side)
        assertEquals(20.0, square.height)

        // Changing height should change side
        square.height = 15.0
        assertEquals(15.0, square.side)
        assertEquals(15.0, square.width)
    }

    @Test
    fun testContainsPoint() {
        val square = Square(0.0, 0.0, 10.0)
        assertTrue(square.contains(5.0, 5.0))
        assertTrue(square.contains(0.0, 0.0))
        assertTrue(square.contains(10.0, 10.0))
    }

    @Test
    fun testSetTo() {
        val square = Square(0.0, 0.0, 5.0)
        square.setTo(10.0, 20.0, 15.0)
        assertEquals(10.0, square.x)
        assertEquals(20.0, square.y)
        assertEquals(15.0, square.side)
    }

    @Test
    fun testToRectangle() {
        val square = Square(5.0, 10.0, 20.0)
        val rect = square.toRectangle()
        assertEquals(5.0, rect.x)
        assertEquals(10.0, rect.y)
        assertEquals(20.0, rect.width)
        assertEquals(20.0, rect.height)
    }

    @Test
    fun testClone() {
        val square = Square(5.0, 10.0, 15.0)
        val clone = square.cloneSquare()
        assertEquals(square.x, clone.x)
        assertEquals(square.y, clone.y)
        assertEquals(square.side, clone.side)
    }

    @Test
    fun testDisplaced() {
        val square = Square(0.0, 0.0, 10.0)
        val displaced = square.displaced(5.0, 5.0)
        assertEquals(5.0, displaced.x)
        assertEquals(5.0, displaced.y)
        assertEquals(10.0, displaced.width)
    }

    @Test
    fun testIntVariant() {
        val squareInt = SquareInt(Square(5.5, 10.7, 20.3))
        assertEquals(5, squareInt.x)
        assertEquals(10, squareInt.y)
        assertEquals(20, squareInt.side)
    }

    @Test
    fun testIntVariantConstruction() {
        val squareInt = SquareInt(5, 10, 20)
        assertEquals(5, squareInt.x)
        assertEquals(10, squareInt.y)
        assertEquals(20, squareInt.side)
        assertEquals(20, squareInt.width)
        assertEquals(20, squareInt.height)
    }

    @Test
    fun testIntVariantSetTo() {
        val squareInt = SquareInt(0, 0, 5)
        squareInt.setTo(10, 20, 15)
        assertEquals(10, squareInt.x)
        assertEquals(20, squareInt.y)
        assertEquals(15, squareInt.side)
    }

    @Test
    fun testIntToFloat() {
        val squareInt = SquareInt(5, 10, 20)
        val square = squareInt.square
        assertEquals(5.0, square.x)
        assertEquals(10.0, square.y)
        assertEquals(20.0, square.side)
    }
}
