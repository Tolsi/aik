package ru.tolsi.aik.geom.draw

import ru.tolsi.aik.geom.IRectangle
import ru.tolsi.aik.geom.Rectangle

fun java.awt.Rectangle.toRectangle(): IRectangle {
    return Rectangle(this.x, this.y,  this.width, this.height)
}