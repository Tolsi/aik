@file:Suppress("NOTHING_TO_INLINE")

package ru.tolsi.aik.geom

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Interface for a circular sector (pie slice).
 * Represents the region between two radii and an arc of a circle.
 */
interface ICircularSector : IPolygon {
    val center: IPoint
    val radius: Double
    val startAngle: Angle
    val endAngle: Angle
    val sweepAngle: Angle
}

/**
 * Circular sector (pie slice) - part of a circle between two radii.
 *
 * The sector is defined by a center point, radius, and two angles (start and end).
 * The shape includes the center point, the arc between the angles, and the two radii.
 *
 * @property x X-coordinate of center
 * @property y Y-coordinate of center
 * @property radius Radius of the circle
 * @property startAngle Starting angle
 * @property endAngle Ending angle
 * @property totalPoints Number of points to approximate the arc (default 32)
 * @property validate Whether to validate parameters (default true)
 */
open class CircularSector(
    val x: Double,
    val y: Double,
    override val radius: Double,
    startAngle: Angle,
    endAngle: Angle,
    val totalPoints: Int = 32,
    validate: Boolean = true
) : ICircularSector {

    protected var _center: Point = Point(x, y)
    protected var _startAngle: Angle = startAngle
    protected var _endAngle: Angle = endAngle

    init {
        if (validate) {
            require(radius > 0) { "Radius must be positive, got $radius" }
            require(totalPoints >= 3) { "Total points must be at least 3, got $totalPoints" }
        }
    }

    constructor(
        center: IPoint,
        radius: Double,
        startAngle: Angle,
        endAngle: Angle,
        totalPoints: Int = 32,
        validate: Boolean = true
    ) : this(center.x, center.y, radius, startAngle, endAngle, totalPoints, validate)

    override val center: IPoint get() = _center
    override val startAngle: Angle get() = _startAngle
    override val endAngle: Angle get() = _endAngle

    override val sweepAngle: Angle
        get() {
            val diff = _endAngle.radians - _startAngle.radians
            return when {
                diff >= 0 -> Angle(diff)
                else -> Angle(diff + 2 * PI)
            }
        }

    override val points: IPointArrayList by lazy {
        PointArrayList(totalPoints + 2).apply {
            // Counter-clockwise orientation: arc from end to start, then center
            val sweep = sweepAngle.radians

            // Add arc points from endAngle back to startAngle (reversed for CCW)
            for (i in totalPoints downTo 0) {
                val ratio = i.toDouble() / totalPoints
                val angle = _startAngle.radians + sweep * ratio
                add(
                    _center.x + cos(angle) * radius,
                    _center.y + sin(angle) * radius
                )
            }

            // Add center last (polygon closes back to first arc point)
            add(_center.x, _center.y)
        }
    }

    override val closed: Boolean = true

    override val area: Double
        get() = (sweepAngle.radians * radius * radius) / 2.0

    override fun containsPoint(x: Double, y: Double): Boolean {
        // Check if point is within radius
        val distance = hypot(x - _center.x, y - _center.y)
        if (distance > radius) return false

        // Check if angle is in range
        val pointAngle = atan2(y - _center.y, x - _center.x)
        val normalizedPointAngle = if (pointAngle < 0) pointAngle + 2 * PI else pointAngle

        val normalizedStart = if (_startAngle.radians < 0) _startAngle.radians + 2 * PI else _startAngle.radians
        val normalizedEnd = normalizedStart + sweepAngle.radians

        return if (normalizedEnd <= 2 * PI) {
            normalizedPointAngle >= normalizedStart && normalizedPointAngle <= normalizedEnd
        } else {
            // Wraps around 0
            normalizedPointAngle >= normalizedStart || normalizedPointAngle <= (normalizedEnd - 2 * PI)
        }
    }

    override fun toString(): String =
        "CircularSector(center=$_center, radius=$radius, startAngle=$_startAngle, endAngle=$_endAngle)"

    companion object {
        inline operator fun invoke(
            x: Number,
            y: Number,
            radius: Number,
            startAngle: Angle,
            endAngle: Angle,
            totalPoints: Int = 32
        ) = CircularSector(
            x.toDouble(),
            y.toDouble(),
            radius.toDouble(),
            startAngle,
            endAngle,
            totalPoints
        )
    }
}

/**
 * Integer variant of CircularSector.
 */
inline class CircularSectorInt(val sector: CircularSector) {
    constructor(
        x: Int,
        y: Int,
        radius: Int,
        startAngle: Angle,
        endAngle: Angle,
        totalPoints: Int = 32
    ) : this(
        CircularSector(
            x.toDouble(),
            y.toDouble(),
            radius.toDouble(),
            startAngle,
            endAngle,
            totalPoints
        )
    )

    val x: Int get() = sector.x.toInt()
    val y: Int get() = sector.y.toInt()
    val radius: Int get() = sector.radius.toInt()
    val center: PointInt get() = PointInt(x, y)
    val startAngle: Angle get() = sector.startAngle
    val endAngle: Angle get() = sector.endAngle
    val sweepAngle: Angle get() = sector.sweepAngle
}

// Conversion extensions
inline val ICircularSector.int: CircularSectorInt
    get() = CircularSectorInt(this as? CircularSector ?: CircularSector(center, radius, startAngle, endAngle))
inline val CircularSectorInt.float: ICircularSector get() = sector
