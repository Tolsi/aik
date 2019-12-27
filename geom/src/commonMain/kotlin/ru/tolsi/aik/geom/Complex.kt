package ru.tolsi.aik.geom


open class Complex(val items: List<IPolygon>) : IPolygon {
    override val points by lazy { items.map { it.points }.flatten() }
    override val closed: Boolean = false
    override fun containsPoint(x: Double, y: Double): Boolean = this.getAllPoints().contains(x, y)
    override val area: Double
        get() = items.map { it.area }.sum()
}