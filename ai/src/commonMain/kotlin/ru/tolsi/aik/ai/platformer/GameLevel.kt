package ru.tolsi.aik.ai.platformer

import com.soywiz.kds.Array2
import ru.tolsi.aik.ai.travellingSalesmanProblem
import ru.tolsi.aik.geom.*

abstract class Tile

abstract class Game {}

class GameField<T : Tile>(val tiles: Array2<Tile>) :
    Rectangle(0.0, 0.0, tiles.height.toDouble(), tiles.width.toDouble()) {

    fun tilesToPolygons(tileType: T): List<Polygon> {
        val result = mutableSetOf<Polygon>()

        this.tiles.getPositionsWithValue(tileType).forEach { (x, y) ->
            if (x != 0 &&
                x != width.toInt() - 1 &&
//          bottom - y != 0 &&
                y != height.toInt() - 1
            ) {
                val rect = Rectangle(x, y, 1, 1)
                if (result.isEmpty()) {
                    result.add(rect.toPolygon())
                } else {
                    val mergedPolygonAndResults =
                        result.asSequence().map { it to it.merge(rect) }.filter { it.second != null }.toList()
                    if (mergedPolygonAndResults.isEmpty()) {
                        result.add(rect.toPolygon())
                    } else {
                        val allMerged = mergedPolygonAndResults.map { it.second!! }.reduce { f, s -> f.merge(s)!! }
                        mergedPolygonAndResults.forEach { result.remove(it.first) }
                        result.add(allMerged)
                    }
                }
            }
        }

        return result.map { it.travellingSalesmanProblem() }.map { it.simplify() }.toList()
    }
}