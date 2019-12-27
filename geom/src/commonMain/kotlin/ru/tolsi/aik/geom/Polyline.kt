package ru.tolsi.aik.geom


open class Polyline(override val points: IPointArrayList) : Figure2D, WithArea {
    override val closed: Boolean = false
    override val area: Double get() = 0.0
    override fun containsPoint(x: Double, y: Double) = false
}