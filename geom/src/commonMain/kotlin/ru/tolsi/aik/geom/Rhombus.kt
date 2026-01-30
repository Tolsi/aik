package ru.tolsi.aik.geom

import ru.tolsi.aik.geom.math.allAlmostEqual
import ru.tolsi.aik.geom.math.almostZero
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

interface IRhombus : IParallelogram {
    val side: Double
    val diagonal1: Double
    val diagonal2: Double
}

open class Rhombus : Parallelogram, IRhombus {
    override val side: Double
        get() = p0.distanceTo(p1)

    override val diagonal1: Double
        get() = p0.distanceTo(p2)

    override val diagonal2: Double
        get() = p1.distanceTo(p3)

    private constructor(p0: IPoint, edge1: IPoint, edge2: IPoint) : super(p0, edge1, edge2, validate = false)

    override val area: Double
        get() = (diagonal1 * diagonal2) / 2.0

    companion object {
        operator fun invoke(
            center: IPoint,
            side: Double,
            angle: Angle,
            validate: Boolean = true
        ): Rhombus {
            // Calculate vertices from center, side length, and angle between sides
            val halfD1 = side * sin(angle.radians / 2)
            val halfD2 = side * cos(angle.radians / 2)

            val p0 = center + Point(-halfD2, 0.0)
            val p1 = center + Point(0.0, halfD1)
            val p2 = center + Point(halfD2, 0.0)
            val p3 = center + Point(0.0, -halfD1)

            return fromPoints(p0, p1, p2, p3, validate)
        }

        fun fromDiagonals(center: IPoint, d1: Double, d2: Double, validate: Boolean = true): Rhombus {
            val halfD1 = d1 / 2.0
            val halfD2 = d2 / 2.0

            // d1 is horizontal (p0 to p2), d2 is vertical (p1 to p3)
            val p0 = center + Point(-halfD1, 0.0)
            val p1 = center + Point(0.0, halfD2)
            val p2 = center + Point(halfD1, 0.0)
            val p3 = center + Point(0.0, -halfD2)

            return fromPoints(p0, p1, p2, p3, validate)
        }

        fun fromPoints(p0: IPoint, p1: IPoint, p2: IPoint, p3: IPoint, validate: Boolean = true): Rhombus {
            if (validate) {
                val sides = listOf(
                    p0.distanceTo(p1),
                    p1.distanceTo(p2),
                    p2.distanceTo(p3),
                    p3.distanceTo(p0)
                )
                require(allAlmostEqual(sides)) {
                    "All sides must be equal (got: ${sides})"
                }

                // Check diagonals perpendicular
                val diag1 = p2 - p0
                val diag2 = p3 - p1
                val dot = diag1.x * diag2.x + diag1.y * diag2.y
                require(almostZero(abs(dot))) {
                    "Diagonals must be perpendicular (dot product: $dot)"
                }
            }

            // Swap edges to ensure counter-clockwise orientation
            val edge1 = p3 - p0
            val edge2 = p1 - p0
            return Rhombus(p0, edge1, edge2)
        }

        fun fromSideAndHeight(base: IPoint, side: Double, height: Double, validate: Boolean = true): Rhombus {
            // Calculate horizontal offset using Pythagorean theorem
            val offset = sqrt(side * side - height * height)

            val p0 = base
            val p1 = base + Point(offset, height)
            val p2 = base + Point(2 * offset, 0.0)
            val p3 = base + Point(offset, -height)

            return fromPoints(p0, p1, p2, p3, validate)
        }
    }

    override fun toString(): String =
        "Rhombus(p0=${p0.toString()}, p1=${p1.toString()}, p2=${p2.toString()}, p3=${p3.toString()}, side=${side})"
}

//////////// INT

interface IRhombusInt : IParallelogramInt {
    val side: Int
    val diagonal1: Int
    val diagonal2: Int
}

inline class RhombusInt(val rhombus: Rhombus) : IRhombusInt {
    override val p0: IPointInt get() = rhombus.p0.int
    override val p1: IPointInt get() = rhombus.p1.int
    override val p2: IPointInt get() = rhombus.p2.int
    override val p3: IPointInt get() = rhombus.p3.int
    override val edge1: IPointInt get() = rhombus.edge1.int
    override val edge2: IPointInt get() = rhombus.edge2.int
    override val side: Int get() = rhombus.side.toInt()
    override val diagonal1: Int get() = rhombus.diagonal1.toInt()
    override val diagonal2: Int get() = rhombus.diagonal2.toInt()

    companion object {
        fun fromDiagonals(center: IPointInt, d1: Int, d2: Int, validate: Boolean = true): RhombusInt =
            RhombusInt(Rhombus.fromDiagonals(center.float, d1.toDouble(), d2.toDouble(), validate))

        fun fromPoints(p0: IPointInt, p1: IPointInt, p2: IPointInt, p3: IPointInt, validate: Boolean = true): RhombusInt =
            RhombusInt(Rhombus.fromPoints(p0.float, p1.float, p2.float, p3.float, validate))
    }

    override fun toString(): String =
        "RhombusInt(p0=${p0.toString()}, p1=${p1.toString()}, p2=${p2.toString()}, p3=${p3.toString()}, side=$side)"
}

val IRhombus.int get() = RhombusInt(Rhombus.fromPoints(p0, p1, p2, p3, validate = false))
val IRhombusInt.float get() = Rhombus.fromPoints(p0.float, p1.float, p2.float, p3.float, validate = false)
