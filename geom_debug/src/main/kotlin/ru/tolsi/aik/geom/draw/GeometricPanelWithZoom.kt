package ru.tolsi.aik.geom.draw

import ru.tolsi.aik.geom.Figure2D
import ru.tolsi.aik.geom.Rectangle
import ru.tolsi.aik.geom.debug.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import javax.swing.JPanel

class GeometricPanelWithZoom(val figures: List<Figure2D>) : JPanel() {
    val imageWidth = 120
    val imageHeight = 120
    var scale: Double = 1.0

    private var bufferImage: Image? = null
    private var bufferGraphics: Graphics? = null

    init {
        scale = 4.0
        background = Color.LightGray.toAWT()
    }

    private val draw = AWTDebugDrawer(Rectangle(10, 10, 100, 100), this)

    private fun resetBuffer() { // always keep track of the image size
        val bufferWidth = getPreferredSize().width
        val bufferHeight = getPreferredSize().height
        //    clean up the previous image
        if (bufferGraphics != null) {
            bufferGraphics!!.dispose()
            bufferGraphics = null
        }
        if (bufferImage != null) {
            bufferImage!!.flush()
            bufferImage = null
        }
        System.gc()
        //    create the new image with the size of the panel
        bufferImage = createImage(bufferWidth, bufferHeight)
        bufferGraphics = bufferImage!!.graphics

        //calls the paintbuffer method with
        //the offscreen graphics as a param
        paintBuffer(bufferGraphics!!)
    }

    private fun paintBuffer(g: Graphics) {
        draw.graphics = g as Graphics2D
        draw.clear()
        figures.forEach { draw.draw(it, 0.5f, Color.values.get(it.hashCode() % Color.values.size)) }
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if (bufferImage == null) resetBuffer()
        g.drawImage(bufferImage, 0, 0, this)
    }

    /**
     * For the scroll pane.
     */
    override fun getPreferredSize(): Dimension {
        val w = (scale * imageWidth).toInt()
        val h = (scale * imageHeight).toInt()
        return Dimension(w, h)
    }

    fun updateScale(s: Double) {
        scale = s
        resetBuffer()
        revalidate() // update the scroll pane
        repaint()
    }
}