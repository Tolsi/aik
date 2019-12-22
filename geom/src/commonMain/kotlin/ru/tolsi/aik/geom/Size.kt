package ru.tolsi.aik.geom

import ru.tolsi.aik.geom.internal.*

interface ISize {
    val width: Double
    val height: Double

    companion object {
        inline operator fun invoke(width: Number, height: Number): ISize = Size(Point(width, height))
    }
}

inline class Size(val p: Point) : ISize, Sizeable {
    companion object {
        operator fun invoke(): Size = Size(Point(0, 0))
        inline operator fun invoke(width: Number, height: Number): Size = Size(Point(width, height))
    }

    override val size: Size get() = this

    override var width: Double
        set(value) = run { p.x = value }
        get() = p.x
    override var height: Double
        set(value) = run { p.y = value }
        get() = p.y

    fun setTo(width: Double, height: Double) = this.apply {
        this.width = width
        this.height = height
    }

    fun clone() = Size(width, height)

    override fun toString(): String = "Size(width=${width.niceStr}, height=${height.niceStr})"
}

inline fun Size.setTo(width: Number, height: Number) = setTo(width.toDouble(), height.toDouble())
fun Size.setTo(that: ISize) = setTo(that.width, that.height)
fun Size.setToScaled(sx: Double, sy: Double) = setTo((this.width * sx), (this.height * sy))
inline fun Size.setToScaled(sx: Number, sy: Number = sx) = setToScaled(sx.toDouble(), sy.toDouble())


val ISize.area: Double get() = width * height
val ISize.perimeter: Double get() = width * 2 + height * 2
val ISize.min: Double get() = kotlin.math.min(width, height)
val ISize.max: Double get() = kotlin.math.max(width, height)

interface ISizeInt {
    val width: Int
    val height: Int
}

inline class SizeInt(val size: Size) : ISizeInt {
    companion object {
    	operator fun invoke(): SizeInt = SizeInt(Size(0, 0))
        operator fun invoke(x: Int, y: Int): SizeInt = SizeInt(Size(x, y))
    }

    override var width: Int
        set(value) = run { size.width = value.toDouble() }
        get() = size.width.toInt()
    override var height: Int
        set(value) = run { size.height = value.toDouble() }
        get() = size.height.toInt()

    //override fun toString(): String = "SizeInt($width, $height)"
    override fun toString(): String = "SizeInt(width=$width, height=$height)"
}

fun SizeInt.setTo(width: Int, height: Int) = this.apply {
    this.width = width
    this.height = height
}

fun SizeInt.setTo(that: SizeInt) = setTo(that.width, that.height)

fun SizeInt.setToScaled(sx: Double, sy: Double) = setTo((this.width * sx).toInt(), (this.height * sy).toInt())
inline fun SizeInt.setToScaled(sx: Number, sy: Number = sx) = setToScaled(sx.toDouble(), sy.toDouble())


operator fun SizeInt.contains(v: SizeInt): Boolean = (v.width <= width) && (v.height <= height)
operator fun SizeInt.times(v: Double) = SizeInt(Size((width * v).toInt(), (height * v).toInt()))
inline operator fun SizeInt.times(v: Number) = times(v.toDouble())

fun Size.asInt(): SizeInt = SizeInt(this)
fun SizeInt.asDouble(): Size = this.size

interface Sizeable {
    val size: Size
}
