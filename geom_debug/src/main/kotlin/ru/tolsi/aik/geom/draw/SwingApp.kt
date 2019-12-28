package ru.tolsi.aik.geom.draw

import ru.tolsi.aik.geom.Figure2D
import ru.tolsi.aik.geom.Line
import ru.tolsi.aik.geom.Rectangle
import ru.tolsi.aik.geom.debug.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.*

object SwingApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val panel = GeometricPanelWithZoom(
            listOf(
                Line(0, 0, 10, 10),
                Rectangle(2, 2, 4, 4)
            )
        )
        val model = SpinnerNumberModel(panel.scale, 0.1, 30.0, .1)
        val spinner = JSpinner(model)
        spinner.preferredSize = Dimension(45, spinner.preferredSize.height)
        spinner.addChangeListener {
            val scale = (spinner.value as Double).toFloat()
            panel.updateScale(scale.toDouble())
        }
        val zoomPanel = JPanel()
        zoomPanel.add(JLabel("scale"))
        zoomPanel.add(spinner)

        val f = JFrame()
        f.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        f.contentPane.add(zoomPanel, "North")
        f.contentPane.add(JScrollPane(panel))
        f.setSize(500, 600)
        f.setLocation(200, 200)
        f.isVisible = true
    }
}

internal class GeometricPanelWithZoom(val figures: List<Figure2D>) : ZoomablePanel() {
    val imageWidth = 120
    val imageHeight = 120

    init {
        scale = 4.0
        background = Color.LightGray.toAWT()
    }

    private val draw = AWTDebugDrawer(Rectangle(10, 10, 100, 100), this)
    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        draw.graphics = g as Graphics2D
        draw.clear()
        figures.forEach { draw.draw(it, 0.5f, Color.values.get(it.hashCode() % Color.values.size)) }
    }

    /**
     * For the scroll pane.
     */
    override fun getPreferredSize(): Dimension {
        val w = (scale * imageWidth).toInt()
        val h = (scale * imageHeight).toInt()
        return Dimension(w, h)
    }
}

abstract class ZoomablePanel : JPanel() {
    var scale: Double = 1.0

    fun updateScale(s: Double) {
        scale = s
        revalidate() // update the scroll pane
        repaint()
    }
}