package ru.tolsi.aik.geom

import ru.tolsi.aik.geom.internal.niceStr

interface ISquare : IRectangle {
    val side: Double
    override val width: Double get() = side
    override val height: Double get() = side

    companion object {
        inline operator fun invoke(x: Number, y: Number, side: Number): ISquare =
            Square(x, y, side)
    }
}

open class Square(
    override var x: Double,
    override var y: Double,
    override var side: Double
) : ISquare, Rectangle(x, y, side, side) {

    companion object {
        inline operator fun invoke(): Square = Square(0.0, 0.0, 0.0)
        inline operator fun invoke(x: Number, y: Number, side: Number): Square =
            Square(x.toDouble(), y.toDouble(), side.toDouble())

        fun fromCenter(center: IPoint, side: Double): Square =
            Square(center.x - side / 2, center.y - side / 2, side)

        inline fun fromCenter(center: IPoint, side: Number): Square =
            fromCenter(center, side.toDouble())
    }

    override var width: Double
        get() = side
        set(value) { side = value }

    override var height: Double
        get() = side
        set(value) { side = value }

    override val area: Double get() = side * side

    fun setTo(x: Double, y: Double, side: Double): Square {
        this.x = x
        this.y = y
        this.side = side
        return this
    }

    inline fun setTo(x: Number, y: Number, side: Number): Square =
        setTo(x.toDouble(), y.toDouble(), side.toDouble())

    fun toRectangle(): Rectangle = Rectangle(x, y, side, side)

    fun cloneSquare(): Square = Square(x, y, side)

    override fun toString(): String =
        "Square(x=${x.niceStr}, y=${y.niceStr}, side=${side.niceStr})"
}

//////////// INT

interface ISquareInt : IRectangleInt {
    val side: Int
    override val width: Int get() = side
    override val height: Int get() = side

    companion object {
        inline operator fun invoke(x: Number, y: Number, side: Number): ISquareInt =
            SquareInt(x.toInt(), y.toInt(), side.toInt())
    }
}

inline class SquareInt(val square: Square) : ISquareInt {
    override var x: Int
        get() = square.x.toInt()
        set(value) = run { square.x = value.toDouble() }

    override var y: Int
        get() = square.y.toInt()
        set(value) = run { square.y = value.toDouble() }

    override var side: Int
        get() = square.side.toInt()
        set(value) = run { square.side = value.toDouble() }

    override var width: Int
        get() = square.side.toInt()
        set(value) = run { square.side = value.toDouble() }

    override var height: Int
        get() = square.side.toInt()
        set(value) = run { square.side = value.toDouble() }

    companion object {
        operator fun invoke(): SquareInt = SquareInt(Square())
        inline operator fun invoke(x: Number, y: Number, side: Number): SquareInt =
            SquareInt(Square(x, y, side))
    }

    override fun toString(): String = "SquareInt(x=$x, y=$y, side=$side)"
}

fun SquareInt.setTo(x: Int, y: Int, side: Int) = this.apply {
    this.x = x
    this.y = y
    this.side = side
}

val ISquare.int get() = SquareInt(Square(x, y, side))
val ISquareInt.float get() = Square(x, y, side)
