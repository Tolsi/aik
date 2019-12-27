package ru.tolsi.aik.ai

import com.soywiz.kds.Queue
import com.soywiz.kds.Stack
import ru.tolsi.aik.geom.Point
import ru.tolsi.aik.geom.Polygon
import ru.tolsi.aik.geom.neighbours
import ru.tolsi.aik.geom.toPolygon

// recommended
fun Collection<Point>.twoWaysBfsTravellingSalesmanProblem(): List<Point>? {

    if (this.size % 2 != 0) return null
    // dfs
    // val allowedPaths = Stack<Pair<List<Point>, List<Point>>>()
    // bfs
    val allowedPaths = Queue<Pair<List<Point>, List<Point>>>()
    val sortedPoints = this.groupBy { it.x }.flatMap { it.value.sortedBy { it.y } }
    val startAndPreEndPoint = sortedPoints.first()
    allowedPaths.enqueue(listOf(startAndPreEndPoint) to listOf())
    do {
        val (leftPath, rightPath) = allowedPaths.dequeue()
        val notUsed = this.toSet().minus(leftPath).minus(rightPath)
        val lastLeftPoint = leftPath.last()
        val lastRightPointOpt = rightPath.lastOrNull()
        if (lastRightPointOpt.let { it == lastLeftPoint }) {
            if (notUsed.isEmpty()) {
                return leftPath.plus(rightPath.dropLast(1).asReversed())
            }
        } else if (lastRightPointOpt == null) {
            val leftNeighboursPoints = lastLeftPoint.neighbours.filter { notUsed.contains(it) }
            allowedPaths.enqueue(leftPath.plus(leftNeighboursPoints[0]) to listOf(leftNeighboursPoints[1]))
        } else {
            val leftNeighboursPoints = lastLeftPoint.neighbours.filter { notUsed.contains(it) }
            val rightNeighboursPoints = lastRightPointOpt.neighbours.filter { notUsed.contains(it) }
            leftNeighboursPoints.forEach { leftNeighbour ->
                rightNeighboursPoints.forEach { rightNeighbour ->
                    allowedPaths.enqueue(leftPath.plus(leftNeighbour) to rightPath.plus(rightNeighbour))
                }
            }
        }
    } while (allowedPaths.isNotEmpty())
    return null
}

// too slow
fun Collection<Point>.dfsTravellingSalesmanProblem(): List<Point>? {
    // dfs
    val allowedPaths = Stack<List<Point>>()
    // bfs
//    val allowedPaths = Queue<List<Point>>()
    val sortedPoints = this.groupBy { it.x }.flatMap { it.value.sortedBy { it.y } }
    val startAndPreEndPoint = sortedPoints.first()
    allowedPaths.push(listOf(startAndPreEndPoint))
    do {
        val mayBePath = allowedPaths.pop()
        val notUsed = this.toSet().minus(mayBePath)
        val lastPoint = mayBePath.last()
        if (lastPoint.neighbours.contains(startAndPreEndPoint) && notUsed.isEmpty()) {
            return mayBePath
        } else {
            val nextPoints = lastPoint.neighbours.filter { notUsed.contains(it) }
            nextPoints.forEach {
                val mayBePathBack = notUsed.plus(startAndPreEndPoint).dfs(it, startAndPreEndPoint)?.dropLast(1)
                if (mayBePathBack != null) {
                    if (mayBePathBack.size == notUsed.size) {
                        return mayBePath.plus(mayBePathBack)
                    } else {
                        allowedPaths.push(mayBePath.plus(it))
                    }
                }
            }
        }
    } while (allowedPaths.isNotEmpty())
    return null
}

fun Polygon.travellingSalesmanProblem(): Polygon {
    return this.points.twoWaysBfsTravellingSalesmanProblem()!!.toPolygon()
}