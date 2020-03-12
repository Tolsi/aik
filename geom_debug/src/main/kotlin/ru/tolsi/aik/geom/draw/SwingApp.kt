package ru.tolsi.aik.geom.draw

import ru.tolsi.aik.geom.Line
import ru.tolsi.aik.geom.Rectangle
import ru.tolsi.aik.geom.debug.Color
import ru.tolsi.aik.geom.debug.DrawerBuffer
import java.awt.Dimension
import javax.swing.*


object SwingApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val buf = DrawerBuffer()
        val figures = listOf(
            Line(0, 0, 10, 10),
            Rectangle(2, 2, 4, 4)
        )
        figures.forEach {
            buf.draw(it, 0.5f, Color.values.get(it.hashCode() % Color.values.size))
        }
        val panel = GeometricPanelWithZoom(buf)
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