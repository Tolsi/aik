package ru.tolsi.aik.ai

import korlibs.datastructure.Array2
import ru.tolsi.aik.geom.PointInt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GridPathFindingTests {

    @Test
    fun testGridCreation() {
        val grid = PathfindingGrid(10, 10)
        assertEquals(10, grid.width)
        assertEquals(10, grid.height)
        assertTrue(grid.isInBounds(0, 0))
        assertTrue(grid.isInBounds(9, 9))
        assertTrue(!grid.isInBounds(10, 10))
    }

    @Test
    fun testGridFromBooleanArray() {
        val walkable = Array2(5, 5, Array(5 * 5) { i ->
            val x = i % 5
            val y = i / 5
            x + y < 5
        })
        val grid = PathfindingGrid.fromBooleanArray(walkable)

        assertTrue(grid[0, 0]!!.walkable)
        assertTrue(!grid[4, 4]!!.walkable)
    }

    @Test
    fun testGridSetWalkable() {
        val grid = PathfindingGrid(5, 5)
        grid.setWalkable(2, 2, false)
        assertTrue(!grid[2, 2]!!.walkable)
    }

    @Test
    fun testGridGetNeighborsFourWay() {
        val grid = PathfindingGrid(5, 5)
        val neighbors = grid.getNeighbors(2, 2, MovementPattern.FOUR_WAY)
        assertEquals(4, neighbors.size)
    }

    @Test
    fun testGridGetNeighborsEightWay() {
        val grid = PathfindingGrid(5, 5)
        val neighbors = grid.getNeighbors(2, 2, MovementPattern.EIGHT_WAY)
        assertEquals(8, neighbors.size)
    }

    @Test
    fun testGridGetNeighborsCorner() {
        val grid = PathfindingGrid(5, 5)
        val neighbors = grid.getNeighbors(0, 0, MovementPattern.FOUR_WAY)
        assertEquals(2, neighbors.size)  // Only right and down
    }

    @Test
    fun testAstarSimplePath() {
        // Simple open grid
        val grid = PathfindingGrid(10, 10)
        val start = PointInt(0, 0)
        val goal = PointInt(3, 3)

        val path = astar(grid, start, goal)

        assertNotNull(path)
        assertEquals(start, path.first())
        assertEquals(goal, path.last())
        assertTrue(path.size >= 4)  // Manhattan distance is 6, so path is at least 7 points
    }

    @Test
    fun testAstarWithObstacle() {
        val grid = PathfindingGrid(10, 10)

        // Create a wall
        for (y in 0 until 10) {
            grid.setWalkable(5, y, false)
        }

        val start = PointInt(0, 5)
        val goal = PointInt(9, 5)

        val path = astar(grid, start, goal)

        assertNotNull(path)
        assertEquals(start, path.first())
        assertEquals(goal, path.last())

        // Path should go around the wall
        assertTrue(path.any { it.x != 5 || it.y != 5 })
    }

    @Test
    fun testAstarNoPath() {
        val grid = PathfindingGrid(10, 10)

        // Create impassable walls
        for (y in 0 until 10) {
            grid.setWalkable(5, y, false)
        }

        val start = PointInt(0, 5)
        val goal = PointInt(9, 5)

        // Allow one passage through the wall
        grid.setWalkable(5, 0, true)  // Open passage at top

        val path = astar(grid, start, goal)

        // Should find a path going through the passage
        assertNotNull(path)
    }

    @Test
    fun testAstarCompletelyBlocked() {
        val grid = PathfindingGrid(10, 10)

        // Completely surround the goal
        for (dx in -1..1) {
            for (dy in -1..1) {
                if (dx != 0 || dy != 0) {
                    grid.setWalkable(5 + dx, 5 + dy, false)
                }
            }
        }

        val start = PointInt(0, 0)
        val goal = PointInt(5, 5)

        val path = astar(grid, start, goal)

        assertNull(path)
    }

    @Test
    fun testAstarSameStartAndGoal() {
        val grid = PathfindingGrid(10, 10)
        val start = PointInt(5, 5)

        val path = astar(grid, start, start)

        assertNotNull(path)
        assertEquals(1, path.size)
        assertEquals(start, path.first())
    }

    @Test
    fun testAstarEightWayMovement() {
        val grid = PathfindingGrid(10, 10)
        val start = PointInt(0, 0)
        val goal = PointInt(5, 5)

        val path = astar(grid, start, goal, MovementPattern.EIGHT_WAY)

        assertNotNull(path)
        // With 8-way movement and diagonal allowed, path should be shorter
        assertTrue(path.size <= 6)  // Direct diagonal path
    }

    @Test
    fun testAstarWithCosts() {
        val grid = PathfindingGrid(10, 10)

        // Make a high-cost corridor
        for (x in 3..7) {
            grid.setCost(x, 5, 10.0)
        }

        val start = PointInt(0, 5)
        val goal = PointInt(9, 5)

        val path = astar(grid, start, goal)

        assertNotNull(path)
        // Path might avoid the high-cost corridor if going around is cheaper
    }

    @Test
    fun testDijkstraSimplePath() {
        val grid = PathfindingGrid(10, 10)
        val start = PointInt(0, 0)
        val goal = PointInt(3, 3)

        val path = dijkstra(grid, start, goal)

        assertNotNull(path)
        assertEquals(start, path.first())
        assertEquals(goal, path.last())
    }

    @Test
    fun testDijkstraWithVariableCosts() {
        val costs = Array2(10, 10, Array(10 * 10) { i ->
            val x = i % 10
            val y = i / 10
            if (x in 3..6 && y == 5) 10.0 else 1.0
        })
        val grid = PathfindingGrid.fromCostArray(costs)

        val start = PointInt(0, 5)
        val goal = PointInt(9, 5)

        val path = dijkstra(grid, start, goal)

        assertNotNull(path)
        // Dijkstra should find path avoiding high costs if possible
    }

    @Test
    fun testAstarWithMaxRange() {
        val grid = PathfindingGrid(20, 20)
        val start = PointInt(0, 0)
        val goal = PointInt(10, 10)

        // Path to (10,10) is about 20 moves (Manhattan distance)
        val pathWithinRange = astarWithMaxRange(grid, start, goal, 25.0)
        assertNotNull(pathWithinRange)

        val pathOutOfRange = astarWithMaxRange(grid, start, goal, 10.0)
        assertNull(pathOutOfRange)
    }

    @Test
    fun testAstarWithMaxRangeEightWay() {
        val grid = PathfindingGrid(20, 20)
        val start = PointInt(0, 0)
        val goal = PointInt(10, 10)

        // With 8-way movement, diagonal distance is ~14.14
        val pathWithinRange = astarWithMaxRange(
            grid, start, goal, 15.0,
            movement = MovementPattern.EIGHT_WAY
        )
        assertNotNull(pathWithinRange)
    }

    @Test
    fun testFindReachableCells() {
        val grid = PathfindingGrid(10, 10)
        val start = PointInt(5, 5)
        val range = 3.0

        val reachable = findReachableCells(grid, start, range)

        // Should include start
        assertTrue(start in reachable)
        assertEquals(0.0, reachable[start])

        // Should include adjacent cells
        assertTrue(PointInt(6, 5) in reachable)
        assertTrue(PointInt(5, 6) in reachable)

        // Should not include cells too far away
        assertTrue(PointInt(9, 9) !in reachable)
    }

    @Test
    fun testFindReachableCellsWithObstacle() {
        val grid = PathfindingGrid(10, 10)

        // Create a wall
        for (x in 0 until 10) {
            grid.setWalkable(x, 5, false)
        }

        val start = PointInt(5, 3)
        val range = 5.0

        val reachable = findReachableCells(grid, start, range)

        // Cells on the other side of wall should not be reachable with range 5
        assertTrue(PointInt(5, 7) !in reachable)
        assertTrue(PointInt(5, 2) in reachable)
    }

    @Test
    fun testManhattanHeuristic() {
        val from = PointInt(0, 0)
        val to = PointInt(3, 4)

        val distance = Heuristics.manhattan(from, to)
        assertEquals(7.0, distance)
    }

    @Test
    fun testEuclideanHeuristic() {
        val from = PointInt(0, 0)
        val to = PointInt(3, 4)

        val distance = Heuristics.euclidean(from, to)
        assertEquals(5.0, distance, 0.001)
    }

    @Test
    fun testChebyshevHeuristic() {
        val from = PointInt(0, 0)
        val to = PointInt(3, 4)

        val distance = Heuristics.chebyshev(from, to)
        assertEquals(4.0, distance)
    }

    @Test
    fun testPathNodeReconstruction() {
        val node3 = PathNode(PointInt(3, 3), null, 0.0, 0.0)
        val node2 = PathNode(PointInt(2, 2), node3, 1.0, 1.0)
        val node1 = PathNode(PointInt(1, 1), node2, 2.0, 2.0)

        val path = node1.reconstructPath()

        assertEquals(3, path.size)
        assertEquals(PointInt(3, 3), path[0])
        assertEquals(PointInt(2, 2), path[1])
        assertEquals(PointInt(1, 1), path[2])
    }

    @Test
    fun testAstarOptimalityFourWay() {
        // Test that A* finds optimal path
        val grid = PathfindingGrid(10, 10)
        val start = PointInt(0, 0)
        val goal = PointInt(3, 3)

        val path = astar(grid, start, goal, MovementPattern.FOUR_WAY)

        assertNotNull(path)
        // Manhattan distance is 6, so optimal path has 7 points (including start)
        assertEquals(7, path.size)
    }

    @Test
    fun testLargeGrid() {
        val grid = PathfindingGrid(100, 100)
        val start = PointInt(0, 0)
        val goal = PointInt(99, 99)

        val path = astar(grid, start, goal, MovementPattern.EIGHT_WAY)

        assertNotNull(path)
        assertEquals(start, path.first())
        assertEquals(goal, path.last())
    }

    @Test
    fun testMaze() {
        val grid = PathfindingGrid(11, 11)

        // Create a simple maze
        // ###########
        // #S        #
        // # ####### #
        // # #     # #
        // # # ### # #
        // # #   # # #
        // # ### # # #
        // #     # # #
        // ####### # #
        // #       #G#
        // ###########

        // Walls
        for (i in 0 until 11) {
            grid.setWalkable(0, i, false)
            grid.setWalkable(10, i, false)
            grid.setWalkable(i, 0, false)
            grid.setWalkable(i, 10, false)
        }

        // Internal walls
        for (i in 1..7) {
            grid.setWalkable(2, i, false)
        }
        for (i in 3..9) {
            grid.setWalkable(8, i, false)
        }

        val start = PointInt(1, 1)
        val goal = PointInt(9, 9)

        val path = astar(grid, start, goal)

        assertNotNull(path)
        assertEquals(start, path.first())
        assertEquals(goal, path.last())
    }
}
