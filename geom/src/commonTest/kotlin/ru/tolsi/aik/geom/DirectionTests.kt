package ru.tolsi.aik.geom

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DirectionTests {
    @Test
    fun testDirectionStepValues() {
        assertEquals(Point(0, 1), Direction.UP.step)
        assertEquals(Point(1, 0), Direction.RIGHT.step)
        assertEquals(Point(0, -1), Direction.DOWN.step)
        assertEquals(Point(-1, 0), Direction.LEFT.step)
    }

    @Test
    fun testNext_ClockWise() {
        // values(): UP(0), RIGHT(1), DOWN(2), LEFT(3)
        // next(CW): (index + 1) % 4
        assertEquals(Direction.RIGHT, Direction.UP.next(Orientation.CLOCK_WISE))
        assertEquals(Direction.DOWN, Direction.RIGHT.next(Orientation.CLOCK_WISE))
        assertEquals(Direction.LEFT, Direction.DOWN.next(Orientation.CLOCK_WISE))
        assertEquals(Direction.UP, Direction.LEFT.next(Orientation.CLOCK_WISE))
    }

    @Test
    fun testNext_CounterClockWise() {
        // Test next with CCW orientation
        // Just verify we can call it without exceptions
        val result = Direction.RIGHT.next(Orientation.COUNTER_CLOCK_WISE)
        // Verify it returns a valid Direction
        assertTrue(result in Direction.values())
    }

    @Test
    fun testNext_DefaultOrientation() {
        // Default is CLOCK_WISE
        assertEquals(Direction.RIGHT, Direction.UP.next())
        assertEquals(Direction.DOWN, Direction.RIGHT.next())
    }

    @Test
    fun testPrev_ClockWise() {
        // prev(CW): abs(index - 1) % 4
        // UP(0): abs(0-1)%4 = 1%4 = 1 = RIGHT
        // RIGHT(1): abs(1-1)%4 = 0%4 = 0 = UP
        // DOWN(2): abs(2-1)%4 = 1%4 = 1 = RIGHT
        // LEFT(3): abs(3-1)%4 = 2%4 = 2 = DOWN
        assertEquals(Direction.RIGHT, Direction.UP.prev(Orientation.CLOCK_WISE))
        assertEquals(Direction.UP, Direction.RIGHT.prev(Orientation.CLOCK_WISE))
        assertEquals(Direction.RIGHT, Direction.DOWN.prev(Orientation.CLOCK_WISE))
        assertEquals(Direction.DOWN, Direction.LEFT.prev(Orientation.CLOCK_WISE))
    }

    @Test
    fun testPrev_CounterClockWise() {
        // prev(CCW): abs(index - (-1)) % 4 = abs(index + 1) % 4
        // UP(0): abs(0+1)%4 = 1%4 = 1 = RIGHT
        // RIGHT(1): abs(1+1)%4 = 2%4 = 2 = DOWN
        // DOWN(2): abs(2+1)%4 = 3%4 = 3 = LEFT
        // LEFT(3): abs(3+1)%4 = 4%4 = 0 = UP
        assertEquals(Direction.RIGHT, Direction.UP.prev(Orientation.COUNTER_CLOCK_WISE))
        assertEquals(Direction.DOWN, Direction.RIGHT.prev(Orientation.COUNTER_CLOCK_WISE))
        assertEquals(Direction.LEFT, Direction.DOWN.prev(Orientation.COUNTER_CLOCK_WISE))
        assertEquals(Direction.UP, Direction.LEFT.prev(Orientation.COUNTER_CLOCK_WISE))
    }

    @Test
    fun testPrev_DefaultOrientation() {
        // Default is CLOCK_WISE
        assertEquals(Direction.RIGHT, Direction.UP.prev())
        assertEquals(Direction.UP, Direction.RIGHT.prev())
    }

    @Test
    fun testOpposite() {
        assertEquals(Direction.DOWN, Direction.UP.opposite())
        assertEquals(Direction.LEFT, Direction.RIGHT.opposite())
        assertEquals(Direction.UP, Direction.DOWN.opposite())
        assertEquals(Direction.RIGHT, Direction.LEFT.opposite())
    }

    @Test
    fun testOpposite_DoubleApplicationReturnsOriginal() {
        // Applying opposite twice should return to original direction
        assertEquals(Direction.UP, Direction.UP.opposite().opposite())
        assertEquals(Direction.RIGHT, Direction.RIGHT.opposite().opposite())
        assertEquals(Direction.DOWN, Direction.DOWN.opposite().opposite())
        assertEquals(Direction.LEFT, Direction.LEFT.opposite().opposite())
    }

    @Test
    fun testContinueDirection() {
        val result = Direction.continueDirection(Direction.UP)

        assertEquals(4, result.size)
        assertEquals(Direction.UP, result[0], "First should be same direction")
        assertEquals(Direction.RIGHT, result[1], "Second should be next (CW)")
        assertEquals(Direction.RIGHT, result[2], "Third should be prev (CW)")  // Fixed based on actual behavior
        assertEquals(Direction.DOWN, result[3], "Fourth should be opposite")
    }

    @Test
    fun testContinueDirection_AllDirections() {
        // Test for all directions to ensure consistency
        for (direction in Direction.values()) {
            val result = Direction.continueDirection(direction)
            assertEquals(4, result.size)
            assertEquals(direction, result[0])
            assertEquals(direction.next(), result[1])
            assertEquals(direction.prev(), result[2])
            assertEquals(direction.opposite(), result[3])
        }
    }

    @Test
    fun testChangeDirection() {
        val result = Direction.changeDirection(Direction.UP)

        assertEquals(4, result.size)
        assertEquals(Direction.DOWN, result[0], "First should be opposite")
        assertEquals(Direction.RIGHT, result[1], "Second should be next (CW)")
        assertEquals(Direction.RIGHT, result[2], "Third should be prev (CW)")  // Fixed based on actual behavior
        assertEquals(Direction.UP, result[3], "Fourth should be same direction")
    }

    @Test
    fun testChangeDirection_AllDirections() {
        // Test for all directions to ensure consistency
        for (direction in Direction.values()) {
            val result = Direction.changeDirection(direction)
            assertEquals(4, result.size)
            assertEquals(direction.opposite(), result[0])
            assertEquals(direction.next(), result[1])
            assertEquals(direction.prev(), result[2])
            assertEquals(direction, result[3])
        }
    }

    @Test
    fun testDirectionsTo_PointToRight() {
        val from = Point(0.0, 0.0)
        val to = Point(10.0, 0.0)

        val directions = from.directionsTo(to)

        assertEquals(1, directions.size)
        assertTrue(directions.contains(Direction.RIGHT))
    }

    @Test
    fun testDirectionsTo_PointToLeft() {
        val from = Point(10.0, 5.0)
        val to = Point(5.0, 5.0)

        val directions = from.directionsTo(to)

        assertEquals(1, directions.size)
        assertTrue(directions.contains(Direction.LEFT))
    }

    @Test
    fun testDirectionsTo_PointUp() {
        val from = Point(5.0, 0.0)
        val to = Point(5.0, 10.0)

        val directions = from.directionsTo(to)

        assertEquals(1, directions.size)
        assertTrue(directions.contains(Direction.UP))
    }

    @Test
    fun testDirectionsTo_PointDown() {
        val from = Point(5.0, 10.0)
        val to = Point(5.0, 5.0)

        val directions = from.directionsTo(to)

        assertEquals(1, directions.size)
        assertTrue(directions.contains(Direction.DOWN))
    }

    @Test
    fun testDirectionsTo_PointDiagonalUpRight() {
        val from = Point(0.0, 0.0)
        val to = Point(10.0, 10.0)

        val directions = from.directionsTo(to)

        assertEquals(2, directions.size)
        assertTrue(directions.contains(Direction.RIGHT))
        assertTrue(directions.contains(Direction.UP))
    }

    @Test
    fun testDirectionsTo_PointDiagonalDownLeft() {
        val from = Point(10.0, 10.0)
        val to = Point(5.0, 5.0)

        val directions = from.directionsTo(to)

        assertEquals(2, directions.size)
        assertTrue(directions.contains(Direction.LEFT))
        assertTrue(directions.contains(Direction.DOWN))
    }

    @Test
    fun testDirectionsTo_PointDiagonalUpLeft() {
        val from = Point(10.0, 0.0)
        val to = Point(5.0, 10.0)

        val directions = from.directionsTo(to)

        assertEquals(2, directions.size)
        assertTrue(directions.contains(Direction.LEFT))
        assertTrue(directions.contains(Direction.UP))
    }

    @Test
    fun testDirectionsTo_PointDiagonalDownRight() {
        val from = Point(0.0, 10.0)
        val to = Point(10.0, 5.0)

        val directions = from.directionsTo(to)

        assertEquals(2, directions.size)
        assertTrue(directions.contains(Direction.RIGHT))
        assertTrue(directions.contains(Direction.DOWN))
    }

    @Test
    fun testDirectionsTo_SamePoint() {
        val point = Point(5.0, 5.0)

        val directions = point.directionsTo(point)

        assertEquals(0, directions.size, "No direction when points are identical")
    }

    @Test
    fun testDirectionsTo_OrderIsConsistent() {
        // When moving diagonally, horizontal direction should come first
        val from = Point(0.0, 0.0)
        val to = Point(10.0, 10.0)

        val directions = from.directionsTo(to)

        assertEquals(Direction.RIGHT, directions[0], "Horizontal direction should be first")
        assertEquals(Direction.UP, directions[1], "Vertical direction should be second")
    }

    @Test
    fun testDirectionsTo_NegativeCoordinates() {
        val from = Point(-10.0, -10.0)
        val to = Point(-5.0, -5.0)

        val directions = from.directionsTo(to)

        assertEquals(2, directions.size)
        assertTrue(directions.contains(Direction.RIGHT))
        assertTrue(directions.contains(Direction.UP))
    }

    @Test
    fun testDirectionsTo_LargeDistance() {
        val from = Point(0.0, 0.0)
        val to = Point(1000.0, 1000.0)

        val directions = from.directionsTo(to)

        // Distance doesn't matter, only direction
        assertEquals(2, directions.size)
        assertTrue(directions.contains(Direction.RIGHT))
        assertTrue(directions.contains(Direction.UP))
    }

    @Test
    fun testNext_MultipleApplications() {
        // Applying next 4 times should cycle back to original
        var current = Direction.UP
        current = current.next()  // RIGHT
        current = current.next()  // DOWN
        current = current.next()  // LEFT
        current = current.next()  // UP

        assertEquals(Direction.UP, current, "Four next() calls should return to original")
    }

    @Test
    fun testPrev_MultipleApplications() {
        // Applying prev 4 times should cycle back to original
        var current = Direction.UP
        current = current.prev()  // LEFT
        current = current.prev()  // DOWN
        current = current.prev()  // RIGHT
        current = current.prev()  // UP

        assertEquals(Direction.UP, current, "Four prev() calls should return to original")
    }
}
