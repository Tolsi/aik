package ru.tolsi.aik.geom

import kotlin.math.abs

enum class Direction(val step: Point) {
    UP(Point(0, 1)), RIGHT(Point(1, 0)), DOWN(Point(0, -1)), LEFT(Point(-1, 0));

    companion object {
        fun continueDirection(lastDirection: Direction): List<Direction> {
            return listOf(lastDirection, lastDirection.next(), lastDirection.prev(), lastDirection.opposite())
        }

        fun changeDirection(lastDirection: Direction): List<Direction> {
            return listOf(lastDirection.opposite(), lastDirection.next(), lastDirection.prev(), lastDirection)
        }
    }

    fun next(orientation: Orientation = Orientation.CLOCK_WISE): Direction {
        val index = values().indexOf(this)
        return values()[(index + orientation.value) % 4]
    }

    fun prev(orientation: Orientation = Orientation.CLOCK_WISE): Direction {
        val index = values().indexOf(this)
        return values()[abs(index - orientation.value) % 4]
    }

    fun opposite(): Direction {
        val index = values().indexOf(this)
        return values()[(index + 2) % 4]
    }

}

fun IPoint.directionsTo(p: IPoint): List<Direction> {
    val directions = mutableListOf<Direction>()

    if (x > p.x) {
        directions.add(Direction.LEFT)
    } else if (x < p.x) {
        directions.add(Direction.RIGHT)
    }

    if (y > p.y) {
        directions.add(Direction.DOWN)
    } else if (y < p.y) {
        directions.add(Direction.UP)
    }

    return directions
}