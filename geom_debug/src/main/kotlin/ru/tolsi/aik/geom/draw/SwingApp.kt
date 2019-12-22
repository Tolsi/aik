package ru.tolsi.aik.geom.draw

import java.awt.*
import java.awt.geom.AffineTransform
import javax.swing.*
//import org.locationtech

object SwingApp {
    @JvmStatic
    fun main(args: Array<String>) {
//        LineString()
        val panel = GeometricPanelWithZoom()
        val zoom = ImageZoom(panel)
        val f = JFrame()
        f.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        f.contentPane.add(zoom.uIPanel, "North")
        f.contentPane.add(JScrollPane(panel))
        f.setSize(500, 600)
        f.setLocation(200, 200)
        f.isVisible = true
    }
}

internal class GeometricPanelWithZoom : ZoomablePanel() {
    val imageWidth = 120
    val imageHeight = 120

    init {
        scale = 4.0
        background = Color.lightGray
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2 = g as Graphics2D
        g2.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC
        )
        val x = (width - scale * imageWidth) / 2
        val y = (height - scale * imageHeight) / 2
        val at = AffineTransform.getTranslateInstance(x, y)
        at.scale(scale, scale)
        g2.transform(at)
        g2.color = Color.WHITE
        g2.stroke = BasicStroke(0.2f)
        g2.font = g2.font.deriveFont(3f)

        val startFromX = 10
        val startFromY = 10

        (0..100 step 5).forEach { i ->
            g2.drawLine(startFromX + i, startFromY + 0, startFromX + i, startFromY + 100)
            g2.drawLine(startFromX, startFromY + i, startFromX + 100, startFromY + i)

            g2.drawString("${(i / 5) + 1}", startFromX + (i - 0.5).toFloat(), startFromY - 3f)
            g2.drawString("${(i / 5) + 1}", startFromX - 5f, startFromY + (i + 0.7).toFloat())
        }
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

internal class ImageZoom(private val zoomable: ZoomablePanel) {
    val uIPanel: JPanel
        get() {
            val model = SpinnerNumberModel(zoomable.scale, 0.1, 30.0, .1)
            val spinner = JSpinner(model)
            spinner.preferredSize = Dimension(45, spinner.preferredSize.height)
            spinner.addChangeListener {
                val scale = (spinner.value as Double).toFloat()
                zoomable.updateScale(scale.toDouble())
            }
            val panel = JPanel()
            panel.add(JLabel("scale"))
            panel.add(spinner)
            return panel
        }

}