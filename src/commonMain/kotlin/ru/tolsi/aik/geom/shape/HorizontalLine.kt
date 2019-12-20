package ru.tolsi.aik.geom.shape

import ru.tolsi.aik.geom.Point

object HorizontalLine {
    fun intersectionsWithLine(
        ax: Double, ay: Double,
        bx0: Double, by0: Double, bx1: Double, by1: Double
    ): Int {
        return if (((by1 > ay) != (by0 > ay)) && (ax < (bx0 - bx1) * (ay - by1) / (by0 - by1) + bx1)) 1 else 0
    }
}
