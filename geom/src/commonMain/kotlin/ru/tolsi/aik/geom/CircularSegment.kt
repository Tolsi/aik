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

    /**
     * Check if this circular segment intersects with a point.
     */
    infix fun intersects(point: IPoint): Boolean = containsPoint(point.x, point.y)

    /**
     * Check if this circular segment intersects with a circle.
     * Returns true if the circle overlaps with any part of the segment.
     */
    infix fun intersects(circle: Circle): Boolean {
        // Distance between centers
        val dx = circle.x - _center.x
        val dy = circle.y - _center.y
        val distSquared = dx * dx + dy * dy
        val dist = kotlin.math.sqrt(distSquared)

        // Check if circles overlap
        if (dist > radius + circle.radius) return false  // Too far apart
        if (dist < kotlin.math.abs(radius - circle.radius)) {
            // One circle completely inside the other - check if segment arc overlaps
            // For simplicity, check if any point on segment boundary is in circle
            return points.any { circle.containsPoint(it.x, it.y) }
        }

        // Circles intersect - check if intersection points are within segment
        // Use polygon approximation for detailed check
        return points.any { circle.containsPoint(it.x, it.y) } ||
                circle.points.any { containsPoint(it.x, it.y) }
    }

    /**
     * Check if this circular segment intersects with a rectangle.
     */
    infix fun intersects(rect: IRectangle): Boolean {
        // Quick bounding box check
        val segLeft = _center.x - radius
        val segRight = _center.x + radius
        val segTop = _center.y - radius
        val segBottom = _center.y + radius

        if (segRight < rect.left || segLeft > rect.right ||
            segBottom < rect.top || segTop > rect.bottom
        ) {
            return false
        }

        // Check if any corner of rectangle is inside segment
        if (containsPoint(rect.left, rect.top) || containsPoint(rect.right, rect.top) ||
            containsPoint(rect.left, rect.bottom) || containsPoint(rect.right, rect.bottom)
        ) {
            return true
        }

        // Check if any point on segment is inside rectangle
        return points.any { rect.containsPoint(it.x, it.y) }
    }

    /**
     * Check if this circular segment intersects with a line segment.
     */
    infix fun intersects(line: ILine): Boolean {
        // Check if either endpoint is inside segment
        if (containsPoint(line.from.x, line.from.y) || containsPoint(line.to.x, line.to.y)) {
            return true
        }

        // Check if line intersects any edge of the approximated polygon
        return points.indices.any { i ->
            val p1 = points[i]
            val p2 = points[(i + 1) % points.size]
            val edge = LineSegment(p1, p2)
            line.intersects(edge) != null
        }
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
