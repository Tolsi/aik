package ru.tolsi.aik.geom


data class Complex(val items: List<GeometricFigure2D>) : GeometricFigure2D {
    override val points by lazy { items.map { it.points }.flatten() }
    override val closed: Boolean = false
    override fun containsPoint(x: Double, y: Double): Boolean = this.getAllPoints().contains(x, y)
}