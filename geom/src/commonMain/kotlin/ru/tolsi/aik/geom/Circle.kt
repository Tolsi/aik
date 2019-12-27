package ru.tolsi.aik.geom

import kotlin.math.PI
import kotlin.math.hypot

data class Circle(val x: Double, val y: Double, val radius: Double, val totalPoints: Int = 32) : IPolygon {
    companion object {
        inline operator fun invoke(x: Number, y: Number, radius: Number, totalPoints: Int = 32) =
            Circle(x.toDouble(), y.toDouble(), radius.toDouble(), totalPoints)
    }

    override val points by lazy {
        PointArrayList(totalPoints) {
            for (it in 0 until totalPoints) {
                add(
                    x + Angle.cos01(it.toDouble() / totalPoints.toDouble()) * radius,
                    y + Angle.sin01(it.toDouble() / totalPoints.toDouble()) * radius
                )
            }
        }
    }
    override val closed: Boolean = true
    override val area: Double get() = PI * radius * radius
    override fun containsPoint(x: Double, y: Double) = hypot(this.x - x, this.y - y) < radius
}