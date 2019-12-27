package ru.tolsi.aik.ai

import com.soywiz.kds.Queue
import com.soywiz.kds.Stack
import ru.tolsi.aik.geom.IPoint
import ru.tolsi.aik.geom.Point
import ru.tolsi.aik.geom.neighbours

fun Collection<IPoint>.dfs(from: IPoint, to: IPoint): List<IPoint>? {
    val allowedPaths = Stack<List<IPoint>>()
//    val allowedPaths = Queue<List<Point>>()
    allowedPaths.push(listOf(from))
    if (from == to) {
        return allowedPaths.first()
    }
    do {
        val mayBePath = allowedPaths.pop()
        val notUsed = this.toSet().minus(mayBePath)
        val lastPoint = mayBePath.last()
        if (lastPoint.neighbours.contains(to)) {
            return mayBePath
        } else {
            val nextPoints = lastPoint.neighbours.filter { notUsed.contains(it) }
            nextPoints.forEach {
                allowedPaths.push(mayBePath.plus(it))
            }
        }
    } while (allowedPaths.isNotEmpty())
    return null
}

fun Collection<IPoint>.bfs(from: Point, to: Point): List<IPoint>? {
//    val allowedPaths = Stack<List<Point>>()
    val allowedPaths = Queue<List<IPoint>>()
    allowedPaths.enqueue(listOf(from))
    if (from == to) {
        return allowedPaths.first()
    }
    do {
        val mayBePath = allowedPaths.dequeue()
        val notUsed = this.toSet().minus(mayBePath)
        val lastPoint = mayBePath.last()
        if (lastPoint.neighbours.contains(to)) {
            return mayBePath
        } else {
            val nextPoints = lastPoint.neighbours.filter { notUsed.contains(it) }
            nextPoints.forEach {
                allowedPaths.enqueue(mayBePath.plus(it))
            }
        }
    } while (allowedPaths.isNotEmpty())
    return null
}

// todo
//fun Collection<Point>.aStar(from: Point, to: Point): List<Point>? {
//    val set = this.toSet()
//    val board = Array2.withGen(Global.level.width.toInt(), Global.level.height.toInt(), { x, y -> !set.contains(Point(x, y)) })
//    return AStar.find(board, from.x.toInt(), from.y.toInt(), to.x.toInt(), to.y.toInt()).toPoints().map { it.asDouble() }
//}
