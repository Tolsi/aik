@file:Suppress("NOTHING_TO_INLINE")

package ru.tolsi.aik.geom

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Interface for a circular segment (lens shape).
 * Represents the region between a chord and an arc of a circle.
 */
interface ICircularSegment : IPolygon {
    val center: IPoint
    val radius: Double
    val startAngle: Angle
    val endAngle: Angle
    val sweepAngle: Angle
    val chordLength: Double
    val segmentHeight: Double
}

/**
 * Circular segment (lens shape) - part of a circle cut by a chord.
 *
 * The segment is defined by a center point, radius, and two angles (start and end).
 * Unlike a sector, it does NOT include the center - just the arc and the chord connecting the endpoints.
 *
 * @property x X-coordinate of center
 * @property y Y-coordinate of center
 * @property radius Radius of the circle
 * @property startAngle Starting angle
 * @property endAngle Ending angle
 * @property totalPoints Number of points to approximate the arc (default 32)
 * @property validate Whether to validate parameters (default true)
 */
open class CircularSegment(
    val x: Double,
    val y: Double,
    override val radius: Double,
    startAngle: Angle,
    endAngle: Angle,
    val totalPoints: Int = 32,
    validate: Boolean = true
) : ICircularSegment {

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

    override val chordLength: Double
        get() = 2 * radius * sin(sweepAngle.radians / 2)

    override val segmentHeight: Double
        get() = radius * (1 - cos(sweepAngle.radians / 2))

    override val points: IPointArrayList by lazy {
        PointArrayList(totalPoints + 1).apply {
            // Add arc points (NO center point)
            val sweep = sweepAngle.radians
            for (i in 0..totalPoints) {
                val ratio = i.toDouble() / totalPoints
                val angle = _startAngle.radians + sweep * ratio
                add(
                    _center.x + cos(angle) * radius,
                    _center.y + sin(angle) * radius
                )
            }
            // Polygon automatically closes with a chord line from last to first point
        }
    }

    override val closed: Boolean = true

    override val area: Double
        get() = (radius * radius * (sweepAngle.radians - sin(sweepAngle.radians))) / 2.0

    override fun containsPoint(x: Double, y: Double): Boolean {
        // Check if point is within radius
        val distance = hypot(x - _center.x, y - _center.y)
        if (distance > radius) return false

        // Check if angle is in range
        val pointAngle = atan2(y - _center.y, x - _center.x)
        val normalizedPointAngle = if (pointAngle < 0) pointAngle + 2 * PI else pointAngle

        val normalizedStart = if (_startAngle.radians < 0) _startAngle.radians + 2 * PI else _startAngle.radians
        val normalizedEnd = normalizedStart + sweepAngle.radians

        val angleInRange = if (normalizedEnd <= 2 * PI) {
            normalizedPointAngle >= normalizedStart && normalizedPointAngle <= normalizedEnd
        } else {
            // Wraps around 0
            normalizedPointAngle >= normalizedStart || normalizedPointAngle <= (normalizedEnd - 2 * PI)
        }

        if (!angleInRange) return false

        // Check if point is on the arc side of the chord (not on the center side)
        // Get chord endpoints
        val p1x = _center.x + cos(_startAngle.radians) * radius
        val p1y = _center.y + sin(_startAngle.radians) * radius
        val p2x = _center.x + cos(_endAngle.radians) * radius
        val p2y = _center.y + sin(_endAngle.radians) * radius

        // Compute which side of chord the point is on
        // Cross product: (p2 - p1) × (p - p1)
        val cross = (p2x - p1x) * (y - p1y) - (p2y - p1y) * (x - p1x)

        // Compute which side of chord the center is on
        val centerCross = (p2x - p1x) * (_center.y - p1y) - (p2y - p1y) * (_center.x - p1x)

        // Point must be on opposite side of chord from center
        return (cross * centerCross) <= 0
    }

    override fun toString(): String =
        "CircularSegment(center=$_center, radius=$radius, startAngle=$_startAngle, endAngle=$_endAngle)"

    companion object {
        inline operator fun invoke(
            x: Number,
            y: Number,
            radius: Number,
            startAngle: Angle,
            endAngle: Angle,
            totalPoints: Int = 32
        ) = CircularSegment(
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
 * Integer variant of CircularSegment.
 */
inline class CircularSegmentInt(val segment: CircularSegment) {
    constructor(
        x: Int,
        y: Int,
        radius: Int,
        startAngle: Angle,
        endAngle: Angle,
        totalPoints: Int = 32
    ) : this(
        CircularSegment(
            x.toDouble(),
            y.toDouble(),
            radius.toDouble(),
            startAngle,
            endAngle,
            totalPoints
        )
    )

    val x: Int get() = segment.x.toInt()
    val y: Int get() = segment.y.toInt()
    val radius: Int get() = segment.radius.toInt()
    val center: PointInt get() = PointInt(x, y)
    val startAngle: Angle get() = segment.startAngle
    val endAngle: Angle get() = segment.endAngle
    val sweepAngle: Angle get() = segment.sweepAngle
    val chordLength: Double get() = segment.chordLength
    val segmentHeight: Double get() = segment.segmentHeight
}

// Conversion extensions
inline val ICircularSegment.int: CircularSegmentInt
    get() = CircularSegmentInt(this as? CircularSegment ?: CircularSegment(center, radius, startAngle, endAngle))
inline val CircularSegmentInt.float: ICircularSegment get() = segment
