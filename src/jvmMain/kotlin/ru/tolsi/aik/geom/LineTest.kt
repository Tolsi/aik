package ru.tolsi.aik.geom

import ru.tolsi.aik.geom.shape.Shape2d
import ru.tolsi.aik.geom.shape.clip
import ru.tolsi.aik.geom.triangle.Triangle
import kotlin.jvm.JvmStatic

object LineTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val l1 = Line(
            Point(10, 10),
            Point(11, 10)
        )
        println(l1.rotate(45.degrees))
//        println(l1.infiniteIntersects(Point(4, 0)))
//        println(l1.intersectsInfiniteDirected(l2))
        val tri = Triangle(
            Point(100, 150),
            Point(200, 250),
            Point(300, 200), false, false)
        val rec = Shape2d.Line(100, 100, 200, 200)
        println(rec.clip(tri))
    }
}