package ru.tolsi.aik.geom.internal

import ru.tolsi.aik.geom.math.*

internal val Float.niceStr: String get() = if (almostEquals(this.toLong().toFloat(), this)) "${this.toLong()}" else "$this"
internal val Double.niceStr: String get() = if (almostEquals(this.toLong().toDouble(), this)) "${this.toLong()}" else "$this"

internal infix fun Double.umod(other: Double): Double {
    val remainder = this % other
    return when {
        remainder < 0 -> remainder + other
        else -> remainder
    }
}