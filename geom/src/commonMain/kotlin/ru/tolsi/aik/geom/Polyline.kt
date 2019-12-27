package ru.tolsi.aik.geom


data class Polyline(val points: IPointArrayList) : GeometricFigure2D, WithArea {
    override val paths = listOf(points)
    override val closed: Boolean = false
    override val area: Double get() = 0.0
    override fun containsPoint(x: Double, y: Double) = false
}