package ru.tolsi.aik.geom


data class Polyline(override val points: IPointArrayList) : Figure2D, WithArea {
    override val closed: Boolean = false
    override val area: Double get() = 0.0
    override fun containsPoint(x: Double, y: Double) = false
}