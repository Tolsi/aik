package ru.tolsi.aik.ai

import ru.tolsi.aik.ai.platformer.GameField
import ru.tolsi.aik.ai.platformer.Tile
import ru.tolsi.aik.geom.*
import ru.tolsi.aik.geom.debug.Color

object ViewBounds {
    fun drawViewAreaAndWallsIntersections(
        fromPoint: IPoint,
        angle: Angle,
        level: GameField<out Tile>,
        polygons: Collection<IPolygon>
    ) {
        val zeroAngleLine = fromPoint.toZeroAngleLine()
        val aimLine = zeroAngleLine.rotate(angle)
        val aimBoundsPoints = (-angle..angle).bounds().toList().map { angle ->
            level.boundLines().asSequence().mapNotNull {
                zeroAngleLine.rotate(angle).toRay().intersects(it)
            }.first()
        }

        val triangle = Triangle(
            aimBoundsPoints.get(0),
            fromPoint,
            aimBoundsPoints.get(1),
            fixOrientation = false,
            checkOrientation = false
        )

        val scanLine = aimLine.rotate(90.degrees)
        var leftAimLine = Line(fromPoint, aimBoundsPoints.get(1))
        var rightAimLine = Line(fromPoint, aimBoundsPoints.get(0))

        Geometry.debug.draw(triangle, 0.1f, Color.Blue)
        var iterateTriangle = triangle


        // todo нахождение лицевых граней полигонов, сейчас это все
        val allPoints = polygons
            .flatMap { poly -> poly.points.map { it to poly } }
        allPoints.sortedBy { fromPoint.distanceTo(it.first) }.forEach { (point, poly) ->
            if (!iterateTriangle.pointInsideTriangle(point)) {
                return@forEach
            }
            val movedScanLine = scanLine.moveTo(point)
            val leftPoint = leftAimLine.intersects(movedScanLine)!!
            val rightPoint = rightAimLine.intersects(movedScanLine)!!
            Geometry.debug.draw(Line(fromPoint, leftPoint), 0.1f, Color.Green)
            Geometry.debug.draw(Line(fromPoint, rightPoint), 0.1f, Color.Green)
            val line = Line(leftPoint, rightPoint)
//                drawPolygon(poly)
//            drawPolygon(iterateTriangle.toPolygon())
//            debug.draw(
//                CustomData.Line(
//                    leftPoint.toVec2Float(),
//                    rightPoint.toVec2Float(),
//                    0.1f,
//                    Color.GRAY.toColorFloat()
//                )
//            )
            val clipped = poly.lineIntersection(line)?.points() ?: listOf()
            if (clipped.size >= 2) {
                Geometry.debug.draw(Line(clipped.first(), clipped.last()), 0.2f, Color.Magenta)
                val uniquePoint = clipped.epsRemove(line.points()).toSet().firstOrNull()
                if (uniquePoint != null) {
                    if (clipped.epsContains(leftPoint)) {
                        leftAimLine = Line(fromPoint, uniquePoint)
                    } else if (clipped.epsContains(rightPoint)) {
                        rightAimLine = Line(fromPoint, uniquePoint)
                    } else {
                        val i = 0
                    }
                    Geometry.debug.draw(Line(fromPoint, leftAimLine.to), 0.1f, Color.Green)
                    Geometry.debug.draw(Line(fromPoint, rightAimLine.to), 0.1f, Color.Green)
                    iterateTriangle = Triangle(
                        leftAimLine.times(10.0).to,
                        fromPoint,
                        rightAimLine.times(10.0).to,
                        fixOrientation = false,
                        checkOrientation = false
                    )
                    val i = 0
                }
            }
        }
    }
}