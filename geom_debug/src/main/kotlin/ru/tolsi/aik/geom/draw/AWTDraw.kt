package ru.tolsi.aik.geom.draw

import ru.tolsi.aik.geom.*
import ru.tolsi.aik.geom.debug.Color
import ru.tolsi.aik.geom.debug.DebugDrawer
import java.awt.BasicStroke
import java.awt.Graphics2D

@ExperimentalUnsignedTypes
fun Color.toAWT(): java.awt.Color {
    return java.awt.Color(this.r.toInt(), this.g.toInt(), this.b.toInt(), this.alpha.toInt())
}

class AWTDebugDrawer(val size: Rectangle, val panel: GeometricPanelWithZoom) : DebugDrawer {
    lateinit var graphics: Graphics2D
    val multiply = 5
    val topPadding = 3
    val leftPadding = 5

    fun fieldXCoordinate(x: Double): Int {
        return zoom(x * multiply + size.left + leftPadding)
    }

    private val maxY = size.height.toInt() / multiply

    fun fieldYCoordinate(y: Double): Int {
        return zoom((maxY -  y) * multiply + size.top + topPadding)
    }

    private fun draw(f: Figure2D, depth: Float, c: Color, first: Boolean) {
        if (first) {
            drawField(graphics)
        }
        graphics.color = c.toAWT()
        graphics.stroke = BasicStroke(depth)
        when (f) {
            is ILine -> {
                f.edges().forEach {
                    // Skip edges with duplicate points (can happen with very small shapes after rounding)
                    val x1 = fieldXCoordinate(it.from.x)
                    val y1 = fieldYCoordinate(it.from.y)
                    val x2 = fieldXCoordinate(it.to.x)
                    val y2 = fieldYCoordinate(it.to.y)
                    if (x1 != x2 || y1 != y2) {
                        graphics.drawLine(x1, y1, x2, y2)
                    }
                }
                f.points.forEach {
                    val r = it.toRectangleWithCenterInPoint(0.1)
                    graphics.drawRect(
                        fieldXCoordinate(r.x),
                        fieldYCoordinate(r.y + r.height),
                        zoom(r.width * multiply),
                        zoom(r.height * multiply)
                    )
                }
            }
            is IPolygon ->
                try {
                    f.edges().forEach {
                        draw(it, depth, c)
                    }
                } catch (e: IllegalArgumentException) {
                    // Skip shapes that generate invalid edges (duplicate points)
                    println("Warning: Skipping polygon ${f.javaClass.simpleName} with duplicate points: ${e.message}")
                    println("Polygon has ${f.points.size} points")
                }
        }
    }

    override fun draw(f: Figure2D, depth: Float, c: Color) {
        draw(f, depth, c, first = true)
    }

    override fun clear() {
        graphics.color = Color.DarkGray.toAWT()
        // todo draw until size-10?
        graphics.fillRect(0, 0, zoom(size.right + 10), zoom(size.bottom + 10))
    }

    fun zoom(value: Number): Int {
        return (value.toDouble() * panel.scale).toInt()
    }

    private fun drawField(g: Graphics2D) {
        g.color = Color.White.copy(alpha = 30u).toAWT()
        g.stroke = BasicStroke(0.2f)
        g.font = g.font.deriveFont(zoom(3f))

        val leftX = size.x.toInt() + leftPadding
        val bottomY = size.bottom.toInt() + topPadding

        // y
        (0..size.height.toInt() step multiply).forEach { i ->
            g.drawLine(zoom(leftX), zoom(bottomY - i), zoom(leftX + 100), zoom(bottomY - i))
            g.drawString("${(i / 5)}", zoom(leftX - 5f), zoom(bottomY - i + 0.7))
        }
        // x
        (0..size.width.toInt() step multiply).forEach { i ->
            g.drawLine(zoom(leftX + i), zoom(bottomY), zoom(leftX + i), zoom(bottomY - 100))
            g.drawString("${(i / 5)}", zoom(leftX + i - 0.5), zoom(bottomY + 3f))
        }
    }

}