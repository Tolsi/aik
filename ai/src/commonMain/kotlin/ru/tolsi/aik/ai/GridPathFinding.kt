package ru.tolsi.aik.ai

import korlibs.datastructure.Array2
import ru.tolsi.aik.geom.IPoint
import ru.tolsi.aik.geom.Point
import ru.tolsi.aik.geom.PointInt
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Movement pattern for pathfinding.
 */
enum class MovementPattern {
    /** 4-directional movement (up, down, left, right) */
    FOUR_WAY,

    /** 8-directional movement (includes diagonals) */
    EIGHT_WAY,

    /** Custom movement pattern */
    CUSTOM
}

/**
 * Grid cell for pathfinding.
 */
data class GridCell(
    val x: Int,
    val y: Int,
    val walkable: Boolean = true,
    val cost: Double = 1.0
) {
    fun toPoint(): PointInt = PointInt(x, y)
    fun toPointDouble(): Point = Point(x.toDouble(), y.toDouble())
}

/**
 * Path node used in pathfinding algorithms.
 */
data class PathNode(
    val position: PointInt,
    val parent: PathNode? = null,
    val gCost: Double = 0.0,  // Cost from start
    val hCost: Double = 0.0,  // Heuristic cost to goal
    val totalCost: Double = gCost + hCost
) : Comparable<PathNode> {
    override fun compareTo(other: PathNode): Int = totalCost.compareTo(other.totalCost)

    fun reconstructPath(): List<PointInt> {
        val path = mutableListOf<PointInt>()
        var current: PathNode? = this
        while (current != null) {
            path.add(0, current.position)
            current = current.parent
        }
        return path
    }
}

/**
 * Grid-based map for pathfinding.
 */
class PathfindingGrid(
    val width: Int,
    val height: Int,
    private val cells: Array2<GridCell>
) {
    constructor(width: Int, height: Int, initWalkable: Boolean = true) : this(
        width,
        height,
        Array2(width, height, Array(width * height) { i ->
            val x = i % width
            val y = i / width
            GridCell(x, y, initWalkable)
        })
    )

    /**
     * Check if coordinates are within grid bounds.
     */
    fun isInBounds(x: Int, y: Int): Boolean = x in 0 until width && y in 0 until height

    /**
     * Check if coordinates are within grid bounds.
     */
    fun isInBounds(point: PointInt): Boolean = isInBounds(point.x, point.y)

    /**
     * Get cell at coordinates.
     */
    operator fun get(x: Int, y: Int): GridCell? =
        if (isInBounds(x, y)) cells[x, y] else null

    /**
     * Get cell at point.
     */
    operator fun get(point: PointInt): GridCell? = get(point.x, point.y)

    /**
     * Set cell walkability.
     */
    fun setWalkable(x: Int, y: Int, walkable: Boolean) {
        if (isInBounds(x, y)) {
            cells[x, y] = cells[x, y].copy(walkable = walkable)
        }
    }

    /**
     * Set cell cost.
     */
    fun setCost(x: Int, y: Int, cost: Double) {
        if (isInBounds(x, y)) {
            cells[x, y] = cells[x, y].copy(cost = cost)
        }
    }

    /**
     * Get neighbors of a cell based on movement pattern.
     */
    fun getNeighbors(
        x: Int,
        y: Int,
        pattern: MovementPattern = MovementPattern.FOUR_WAY,
        customOffsets: List<Pair<Int, Int>>? = null
    ): List<GridCell> {
        val offsets = when {
            pattern == MovementPattern.CUSTOM && customOffsets != null -> customOffsets
            pattern == MovementPattern.EIGHT_WAY -> listOf(
                -1 to 0, 1 to 0, 0 to -1, 0 to 1,  // Cardinal
                -1 to -1, -1 to 1, 1 to -1, 1 to 1  // Diagonal
            )
            else -> listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)  // Four-way
        }

        return offsets.mapNotNull { (dx, dy) ->
            val nx = x + dx
            val ny = y + dy
            get(nx, ny)?.takeIf { it.walkable }
        }
    }

    /**
     * Get neighbors of a point.
     */
    fun getNeighbors(
        point: PointInt,
        pattern: MovementPattern = MovementPattern.FOUR_WAY,
        customOffsets: List<Pair<Int, Int>>? = null
    ): List<GridCell> = getNeighbors(point.x, point.y, pattern, customOffsets)

    companion object {
        /**
         * Create grid from 2D boolean array (true = walkable).
         */
        fun fromBooleanArray(walkable: Array2<Boolean>): PathfindingGrid {
            val data = Array(walkable.width * walkable.height) { i ->
                val x = i % walkable.width
                val y = i / walkable.width
                GridCell(x, y, walkable[x, y])
            }
            val cells = Array2(walkable.width, walkable.height, data)
            return PathfindingGrid(walkable.width, walkable.height, cells)
        }

        /**
         * Create grid from 2D cost array (0 or negative = unwalkable).
         */
        fun fromCostArray(costs: Array2<Double>): PathfindingGrid {
            val data = Array(costs.width * costs.height) { i ->
                val x = i % costs.width
                val y = i / costs.width
                val cost = costs[x, y]
                GridCell(x, y, cost > 0, if (cost > 0) cost else 1.0)
            }
            val cells = Array2(costs.width, costs.height, data)
            return PathfindingGrid(costs.width, costs.height, cells)
        }
    }
}

/**
 * Heuristic functions for A* algorithm.
 */
object Heuristics {
    /**
     * Manhattan distance (for 4-way movement).
     */
    fun manhattan(from: PointInt, to: PointInt): Double =
        abs(from.x - to.x).toDouble() + abs(from.y - to.y).toDouble()

    /**
     * Euclidean distance (straight line).
     */
    fun euclidean(from: PointInt, to: PointInt): Double {
        val dx = (from.x - to.x).toDouble()
        val dy = (from.y - to.y).toDouble()
        return sqrt(dx * dx + dy * dy)
    }

    /**
     * Chebyshev distance (for 8-way movement with uniform cost).
     */
    fun chebyshev(from: PointInt, to: PointInt): Double =
        maxOf(abs(from.x - to.x), abs(from.y - to.y)).toDouble()

    /**
     * Octile distance (for 8-way movement with diagonal cost).
     */
    fun octile(from: PointInt, to: PointInt): Double {
        val dx = abs(from.x - to.x)
        val dy = abs(from.y - to.y)
        val min = minOf(dx, dy)
        val max = maxOf(dx, dy)
        return min * sqrt(2.0) + (max - min)
    }
}

/**
 * A* pathfinding algorithm.
 *
 * Finds the optimal path from start to goal using A* algorithm with configurable heuristic.
 *
 * @param grid The pathfinding grid
 * @param start Starting position
 * @param goal Goal position
 * @param movement Movement pattern (4-way, 8-way, or custom)
 * @param heuristic Heuristic function for estimating distance to goal
 * @param diagonalCost Cost multiplier for diagonal movement (default: sqrt(2))
 * @return List of points representing the path, or null if no path exists
 */
fun astar(
    grid: PathfindingGrid,
    start: PointInt,
    goal: PointInt,
    movement: MovementPattern = MovementPattern.FOUR_WAY,
    heuristic: (PointInt, PointInt) -> Double = Heuristics::manhattan,
    diagonalCost: Double = sqrt(2.0)
): List<PointInt>? {
    if (!grid.isInBounds(start) || !grid.isInBounds(goal)) return null
    if (grid[start]?.walkable != true || grid[goal]?.walkable != true) return null
    if (start == goal) return listOf(start)

    val openSet = sortedSetOf<PathNode>()
    val closedSet = mutableSetOf<PointInt>()
    val gScores = mutableMapOf<PointInt, Double>()

    val startNode = PathNode(
        position = start,
        parent = null,
        gCost = 0.0,
        hCost = heuristic(start, goal)
    )

    openSet.add(startNode)
    gScores[start] = 0.0

    while (openSet.isNotEmpty()) {
        val current = openSet.first()
        openSet.remove(current)

        if (current.position == goal) {
            return current.reconstructPath()
        }

        // Skip if we've already processed this position
        if (current.position in closedSet) continue

        closedSet.add(current.position)

        val neighbors = grid.getNeighbors(current.position, movement)
        for (neighborCell in neighbors) {
            val neighborPos = neighborCell.toPoint()

            if (neighborPos in closedSet) continue

            // Calculate movement cost (with diagonal cost if applicable)
            val isDiagonal = abs(neighborPos.x - current.position.x) +
                           abs(neighborPos.y - current.position.y) > 1
            val moveCost = if (isDiagonal) diagonalCost else 1.0
            val tentativeGCost = current.gCost + moveCost * neighborCell.cost

            if (neighborPos !in gScores || tentativeGCost < gScores[neighborPos]!!) {
                gScores[neighborPos] = tentativeGCost

                val neighborNode = PathNode(
                    position = neighborPos,
                    parent = current,
                    gCost = tentativeGCost,
                    hCost = heuristic(neighborPos, goal)
                )

                openSet.add(neighborNode)
            }
        }
    }

    return null
}

/**
 * Dijkstra pathfinding algorithm.
 *
 * Finds the shortest path considering movement costs. Useful when costs vary significantly.
 *
 * @param grid The pathfinding grid
 * @param start Starting position
 * @param goal Goal position
 * @param movement Movement pattern
 * @param diagonalCost Cost multiplier for diagonal movement
 * @return List of points representing the path, or null if no path exists
 */
fun dijkstra(
    grid: PathfindingGrid,
    start: PointInt,
    goal: PointInt,
    movement: MovementPattern = MovementPattern.FOUR_WAY,
    diagonalCost: Double = sqrt(2.0)
): List<PointInt>? {
    // Dijkstra is A* with heuristic = 0
    return astar(grid, start, goal, movement, { _, _ -> 0.0 }, diagonalCost)
}

/**
 * Find path with maximum range limit.
 *
 * Finds a path that doesn't exceed the maximum movement range.
 *
 * @param grid The pathfinding grid
 * @param start Starting position
 * @param goal Goal position
 * @param maxRange Maximum movement range (in cost units)
 * @param movement Movement pattern
 * @param heuristic Heuristic function
 * @param diagonalCost Cost multiplier for diagonal movement
 * @return List of points representing the path, or null if no path exists within range
 */
fun astarWithMaxRange(
    grid: PathfindingGrid,
    start: PointInt,
    goal: PointInt,
    maxRange: Double,
    movement: MovementPattern = MovementPattern.FOUR_WAY,
    heuristic: (PointInt, PointInt) -> Double = Heuristics::manhattan,
    diagonalCost: Double = sqrt(2.0)
): List<PointInt>? {
    val path = astar(grid, start, goal, movement, heuristic, diagonalCost) ?: return null

    // Calculate total path cost
    var totalCost = 0.0
    for (i in 0 until path.size - 1) {
        val current = path[i]
        val next = path[i + 1]
        val cell = grid[next] ?: return null

        val isDiagonal = abs(next.x - current.x) + abs(next.y - current.y) > 1
        val moveCost = if (isDiagonal) diagonalCost else 1.0
        totalCost += moveCost * cell.cost
    }

    return if (totalCost <= maxRange) path else null
}

/**
 * Find all reachable cells within a given range.
 *
 * Returns all cells that can be reached from start within the specified range.
 *
 * @param grid The pathfinding grid
 * @param start Starting position
 * @param maxRange Maximum movement range
 * @param movement Movement pattern
 * @param diagonalCost Cost multiplier for diagonal movement
 * @return Map of reachable positions to their distance from start
 */
fun findReachableCells(
    grid: PathfindingGrid,
    start: PointInt,
    maxRange: Double,
    movement: MovementPattern = MovementPattern.FOUR_WAY,
    diagonalCost: Double = sqrt(2.0)
): Map<PointInt, Double> {
    if (!grid.isInBounds(start) || grid[start]?.walkable != true) {
        return emptyMap()
    }

    val reachable = mutableMapOf<PointInt, Double>()
    val openSet = sortedSetOf<PathNode>()

    openSet.add(PathNode(start, null, 0.0, 0.0))
    reachable[start] = 0.0

    while (openSet.isNotEmpty()) {
        val current = openSet.first()
        openSet.remove(current)

        val neighbors = grid.getNeighbors(current.position, movement)
        for (neighborCell in neighbors) {
            val neighborPos = neighborCell.toPoint()

            val isDiagonal = abs(neighborPos.x - current.position.x) +
                           abs(neighborPos.y - current.position.y) > 1
            val moveCost = if (isDiagonal) diagonalCost else 1.0
            val newCost = current.gCost + moveCost * neighborCell.cost

            if (newCost <= maxRange && (neighborPos !in reachable || newCost < reachable[neighborPos]!!)) {
                reachable[neighborPos] = newCost
                openSet.add(PathNode(neighborPos, current, newCost, 0.0))
            }
        }
    }

    return reachable
}
