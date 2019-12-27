package ru.tolsi.aik.geom


object Empty : GeometricFigure2D, WithArea {
    override val paths: List<PointArrayList> = listOf(PointArrayList(0))
    override val closed: Boolean = false
    override val area: Double = 0.0
    override fun containsPoint(x: Double, y: Double) = false
}