package ru.tolsi.aik.geom.draw

import ru.tolsi.aik.geom.*
import ru.tolsi.aik.geom.debug.Color
import ru.tolsi.aik.geom.debug.DebugDrawer
import java.awt.BasicStroke
import java.awt.Graphics2D
import kotlin.math.min

@ExperimentalUnsignedTypes
fun Color.toAWT(): java.awt.Color {
    return java.awt.Color(this.r.toInt(), this.g.toInt(), this.b.toInt(), this.alpha.toInt())
}

class AWTDebugDrawer(val size: Rectangle, val panel: ZoomablePanel) : DebugDrawer {
    lateinit var graphics: Graphics2D
    private val multiply = 5
    private fun draw(f: Figure2D, depth: Float, c: Color, first: Boolean) {
        if (first) {
            drawField(graphics)
        }
        graphics.color = c.toAWT()
        graphics.stroke = BasicStroke(depth)
        when (f) {
            is ILine -> {
                f.edges().forEach {
                    graphics.drawLine(
                        zoom(it.from.x * multiply + size.left),
                        zoom(it.from.y * multiply + size.top),
                        zoom(it.to.x * multiply + size.left),
                        zoom(it.to.y * multiply + size.top)
                    )
                }
                f.points.forEach {
                    val r = it.toRectangleWithCenterInPoint(0.1)
                    graphics.fillRect(
                        zoom(r.x * multiply + size.left),
                        zoom(r.y * multiply + size.top),
                        zoom(r.width * multiply),
                        zoom(r.height * multiply)
                    )
                }
            }
            is IPolygon ->
                f.edges().forEach {
                    draw(it, depth, c)
                }
        }
    }

    override fun draw(f: Figure2D, depth: Float, c: Color) {
        draw(f, depth, c, first = true)
    }

    override fun clear() {
        graphics.color = Color.DarkGray.toAWT()
        graphics.fillRect(0, 0, zoom(size.right + 5), zoom(size.bottom + 5))
    }

    private fun zoom(value: Number): Int {
        return (value.toDouble() * panel.scale).toInt()
    }

    private fun drawField(g: Graphics2D) {
        g.color = Color.White.copy(alpha = 30u).toAWT()
        g.stroke = BasicStroke(0.2f)
        g.font = g.font.deriveFont(zoom(3f))

        val startFromX = size.x.toInt()
        val startFromY = size.y.toInt()

        (0..min(size.height, size.width).toInt() step multiply).forEach { i ->
            g.drawLine(zoom(startFromX + i), zoom(startFromY), zoom(startFromX + i), zoom(startFromY + 100))
            g.drawLine(zoom(startFromX), zoom(startFromY + i), zoom(startFromX + 100), zoom(startFromY + i))

            g.drawString("${(i / 5)}", zoom(startFromX + (i - 0.5).toFloat()), zoom(startFromY - 3f))
            g.drawString("${(i / 5)}", zoom(startFromX - 5f), zoom(startFromY + (i + 0.7).toFloat()))
        }
    }

}