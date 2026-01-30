package ru.tolsi.aik.geom.draw

import ru.tolsi.aik.geom.*
import ru.tolsi.aik.geom.debug.Color
import ru.tolsi.aik.geom.debug.DrawerBuffer
import java.awt.Dimension
import javax.swing.*
import kotlin.math.PI


object SwingApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val buf = DrawerBuffer()

        // Grid layout: 5 columns x 3 rows, 1.125x original size (125% of 0.9x)
        val col = 3.94  // Column spacing (3.15 * 1.25)
        val row = 4.5  // Row spacing (3.6 * 1.25)

        // Row 1 (Point shown as small rectangle)
        buf.draw(Point(1.13, 1.69).toRectangleWithCenterInPoint(1.13), 1.5f, Color.Red)

        buf.draw(LineSegment(Point(col + 0.56, 0.56), Point(col + 2.25, 2.25)), 1.5f, Color.Blue)

        val triangle = Triangle(Point(2*col + 0.56, 0.56), Point(2*col + 2.81, 0.56), Point(2*col + 1.69, 2.81))
        buf.draw(triangle, 1.5f, Color.Green)

        val circle = Circle(3*col + 1.69, 1.69, 1.13, totalPoints = 16)
        buf.draw(circle, 1.5f, Color.Magenta)

        val rect = Rectangle.fromBounds(4*col + 0.63, 0.63, 4*col + 3.13, 3.13)
        buf.draw(rect, 1.5f, Color.Cyan)

        // Row 2
        val ellipse = Ellipse(1.69, row + 1.69, 1.46, 0.9, totalPoints = 32)
        buf.draw(ellipse, 1.5f, Color.Yellow)

        val kite = Kite(
            Point(col + 1.69, row + 0.56),
            Point(col + 2.59, row + 1.69),
            Point(col + 1.69, row + 2.81),
            Point(col + 0.79, row + 1.69)
        )
        buf.draw(kite, 1.5f, Color.Orange)

        val parallelogram = Parallelogram(Point(2*col + 0.9, row + 0.9), Point(1.58, 0.0), Point(0.34, 1.35))
        buf.draw(parallelogram, 1.5f, Color.Pink)

        val rhombus = Rhombus.fromDiagonals(Point(3*col + 1.69, row + 1.69), 2.03, 1.69)
        buf.draw(rhombus, 1.5f, Color.LightBlue)

        val trapezoid = Trapezoid(
            Point(4*col + 0.79, row + 0.79),
            Point(4*col + 2.59, row + 0.79),
            Point(4*col + 2.25, row + 2.59),
            Point(4*col + 1.13, row + 2.59)
        )
        buf.draw(trapezoid, 1.5f, Color.LightGreen)

        // Row 3
        val stadium = Stadium(1.69, 2*row + 1.69, width = 2.25, height = 1.35, totalPoints = 16)
        buf.draw(stadium, 1.5f, Color.Cyan)

        val ring = Ring(col + 1.69, 2*row + 1.69, 0.56, 1.35, totalPoints = 32)
        buf.draw(ring, 1.5f, Color.Gray)

        val sector1 = CircularSector(
            2*col + 1.69, 2*row + 1.69,
            radius = 1.35,
            startAngle = Angle.fromDegrees(30),
            endAngle = Angle.fromDegrees(150),
            totalPoints = 16
        )
        buf.draw(sector1, 1.5f, Color.Brown)

        val segment = CircularSegment(
            3*col + 1.69, 2*row + 1.69,
            radius = 1.35,
            startAngle = Angle.fromRadians(0.0),
            endAngle = Angle.fromRadians(PI * 0.7),
            totalPoints = 16
        )
        buf.draw(segment, 1.5f, Color.Purple)

        // Clipping example: CircularSector × Stadium (with overlap highlight)
        val sector2 = CircularSector(
            4*col + 1.13, 2*row + 1.69,
            radius = 1.58,
            startAngle = Angle.fromRadians(0.0),
            endAngle = Angle.fromRadians(PI),
            totalPoints = 16
        )
        val stadium2 = Stadium(4*col + 1.8, 2*row + 1.69, width = 2.03, height = 1.13, totalPoints = 16)

        buf.draw(sector2, 1.5f, Color.Blue)
        buf.draw(stadium2, 1.5f, Color.Green)

        val clipped = sector2.clip(stadium2)
        if (clipped.points.size > 0) {
            buf.draw(clipped, 2.5f, Color.Red)
        }

        // Window setup

        val panel = GeometricPanelWithZoom(buf)
        val scrollPane = JScrollPane(panel)

        val model = SpinnerNumberModel(panel.scale, 0.1, 30.0, 0.5)
        val spinner = JSpinner(model)
        spinner.preferredSize = Dimension(60, spinner.preferredSize.height)
        spinner.addChangeListener {
            try {
                val newScale = (spinner.value as Double)
                println("Scale changing to: $newScale")
                panel.updateScale(newScale)
                scrollPane.revalidate()
                println("Scale updated successfully")
            } catch (e: Exception) {
                println("Error updating scale: ${e.message}")
                e.printStackTrace()
            }
        }
        val zoomPanel = JPanel()
        zoomPanel.add(JLabel("scale"))
        zoomPanel.add(spinner)

        val f = JFrame("AIK Geometry Demo - All Shapes & Operations")
        f.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        f.contentPane.add(zoomPanel, "North")
        f.contentPane.add(scrollPane)
        f.setSize(800, 900)
        f.setLocation(200, 100)
        f.isVisible = true
    }
}