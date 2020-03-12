package ru.tolsi.aik.geom.draw

import ru.tolsi.aik.geom.Rectangle
import ru.tolsi.aik.geom.bottom
import ru.tolsi.aik.geom.debug.Color
import ru.tolsi.aik.geom.debug.DrawerBuffer
import java.awt.*
import javax.swing.JPanel

class GeometricPanelWithZoom(val drawer: DrawerBuffer) : JPanel() {
    val imageWidth = 120
    val imageHeight = 120
    var scale: Double = 1.0

    private var bufferImage: Image? = null
    private var bufferGraphics: Graphics? = null

    init {
        scale = 4.0
        background = Color.LightGray.toAWT()
    }

    private val draw = AWTDebugDrawer(Rectangle(0, 0, 100, 100), this)

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
        drawer.drawn.forEach { draw.draw(it.f, it.depth, it.c) }
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if (bufferImage == null) resetBuffer()
        g.drawImage(bufferImage, 0, 0, this)

        // todo draw numbers close to all lines and screen sizes
        g as Graphics2D

        g.color = Color.White.copy(alpha = 30u).toAWT()
        g.stroke = BasicStroke(0.2f)
        g.font = g.font.deriveFont(draw.zoom(3f))

        val startFromX = visibleRect.x + 1
        val startFromY = visibleRect.toRectangle().bottom.toInt() + 1

        // y
        ((visibleRect.x / draw.multiply)..((visibleRect.width + visibleRect.x) / draw.multiply)).filter { it % draw.multiply == 0 }
            .forEach { i ->
                g.drawString("${(i / 5)}", startFromX, startFromY - draw.zoom(i))
            }

        // x
        ((visibleRect.y / draw.multiply)..((visibleRect.height +
                visibleRect.y) / draw.multiply)).filter { it % draw.multiply == 0 }
            .forEach { i ->
                g.drawString("${(i / 5)}", startFromX + draw.zoom(i), startFromY)
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

    fun updateScale(s: Double) {
        scale = s
        resetBuffer()
        revalidate() // update the scroll pane
        repaint()
    }
}