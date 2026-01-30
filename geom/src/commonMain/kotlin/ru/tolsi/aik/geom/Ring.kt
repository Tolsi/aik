@file:Suppress("NOTHING_TO_INLINE")

package ru.tolsi.aik.geom

import kotlin.math.PI
import kotlin.math.hypot

/**
 * Interface for a ring (annulus).
 * Represents the area between two concentric circles.
 */
interface IRing : IPolygon {
    val center: IPoint
    val innerRadius: Double
    val outerRadius: Double
    val width: Double get() = outerRadius - innerRadius
}

/**
 * Ring (annulus) - area between two concentric circles.
 *
 * The ring is defined by a center point and two radii (inner and outer).
 * The outer radius must be greater than the inner radius.
 *
 * @property x X-coordinate of center
 * @property y Y-coordinate of center
 * @property innerRadius Inner circle radius
 * @property outerRadius Outer circle radius
 * @property totalPoints Number of points to approximate each circle (default 32)
 * @property validate Whether to validate parameters (default true)
 */
open class Ring(
    val x: Double,
    val y: Double,
    override val innerRadius: Double,
    override val outerRadius: Double,
    val totalPoints: Int = 32,
    validate: Boolean = true
) : IRing {

    protected var _center: Point = Point(x, y)

    init {
        if (validate) {
            require(outerRadius > innerRadius) {
                "Outer radius ($outerRadius) must be greater than inner radius ($innerRadius)"
            }
            require(innerRadius >= 0) {
                "Inner radius must be non-negative, got $innerRadius"
            }
            require(totalPoints >= 3) {
                "Total points must be at least 3, got $totalPoints"
            }
        }
    }

    constructor(
        center: IPoint,
        innerRadius: Double,
        outerRadius: Double,
        totalPoints: Int = 32,
        validate: Boolean = true
    ) : this(center.x, center.y, innerRadius, outerRadius, totalPoints, validate)

    override val center: IPoint get() = _center

    override val points: IPointArrayList by lazy {
        PointArrayList(totalPoints * 2).apply {
            // Outer circle (counter-clockwise)
            for (i in 0 until totalPoints) {
                val ratio = i.toDouble() / totalPoints
                add(
                    _center.x + Angle.cos01(ratio) * outerRadius,
                    _center.y + Angle.sin01(ratio) * outerRadius
                )
            }

            // Inner circle (clockwise - reversed winding for hole)
            for (i in totalPoints - 1 downTo 0) {
                val ratio = i.toDouble() / totalPoints
                add(
                    _center.x + Angle.cos01(ratio) * innerRadius,
                    _center.y + Angle.sin01(ratio) * innerRadius
                )
            }
        }
    }

    override val closed: Boolean = true

    override val area: Double
        get() = PI * (outerRadius * outerRadius - innerRadius * innerRadius)

    override fun containsPoint(x: Double, y: Double): Boolean {
        val distance = hypot(_center.x - x, _center.y - y)
        return distance >= innerRadius && distance <= outerRadius
    }

    /**
     * Check if this ring intersects with a point.
     */
    infix fun intersects(point: IPoint): Boolean = containsPoint(point.x, point.y)

    /**
     * Check if this ring intersects with a circle.
     * Returns true if the circle overlaps with the ring area.
     */
    infix fun intersects(circle: Circle): Boolean {
        val dx = circle.x - _center.x
        val dy = circle.y - _center.y
        val distSquared = dx * dx + dy * dy
        val dist = kotlin.math.sqrt(distSquared)

        // Circle center distance from ring center
        val closestPoint = dist - circle.radius  // Closest point of circle to ring center
        val farthestPoint = dist + circle.radius  // Farthest point of circle from ring center

        // Check if circle overlaps with ring area
        // Circle intersects if: closest point <= outer radius AND farthest point >= inner radius
        return closestPoint <= outerRadius && farthestPoint >= innerRadius
    }

    /**
     * Check if this ring intersects with another ring.
     */
    infix fun intersects(other: IRing): Boolean {
        val dx = other.center.x - _center.x
        val dy = other.center.y - _center.y
        val dist = kotlin.math.sqrt(dx * dx + dy * dy)

        // Check if the rings' areas overlap
        // Outer circles must be close enough, inner circles must not completely separate them
        val outerOverlap = dist <= outerRadius + other.outerRadius
        val innerSeparation = dist >= kotlin.math.abs(innerRadius - other.innerRadius)

        return outerOverlap && innerSeparation
    }

    /**
     * Check if this ring intersects with a rectangle.
     */
    infix fun intersects(rect: IRectangle): Boolean {
        // Quick bounding box check
        val ringLeft = _center.x - outerRadius
        val ringRight = _center.x + outerRadius
        val ringTop = _center.y - outerRadius
        val ringBottom = _center.y + outerRadius

        if (ringRight < rect.left || ringLeft > rect.right ||
            ringBottom < rect.top || ringTop > rect.bottom
        ) {
            return false
        }

        // Check if any corner of rectangle is inside ring
        if (containsPoint(rect.left, rect.top) || containsPoint(rect.right, rect.top) ||
            containsPoint(rect.left, rect.bottom) || containsPoint(rect.right, rect.bottom)
        ) {
            return true
        }

        // Check if any point on ring is inside rectangle
        return points.any { rect.containsPoint(it.x, it.y) }
    }

    /**
     * Check if this ring intersects with a line segment.
     */
    infix fun intersects(line: ILine): Boolean {
        // Check if either endpoint is inside ring
        if (containsPoint(line.from.x, line.from.y) || containsPoint(line.to.x, line.to.y)) {
            return true
        }

        // Check if line crosses the ring (intersects outer circle but not completely inside inner circle)
        // Distance from center to line segment
        val dx = line.to.x - line.from.x
        val dy = line.to.y - line.from.y
        val lenSquared = dx * dx + dy * dy

        if (lenSquared < 1e-10) {
            // Degenerate line segment, just check the point
            return containsPoint(line.from.x, line.from.y)
        }

        // Project center onto line segment
        val t = ((_center.x - line.from.x) * dx + (_center.y - line.from.y) * dy) / lenSquared
        val projX = if (t < 0) line.from.x else if (t > 1) line.to.x else line.from.x + t * dx
        val projY = if (t < 0) line.from.y else if (t > 1) line.to.y else line.from.y + t * dy

        val distToLine = hypot(_center.x - projX, _center.y - projY)

        // Line intersects ring if closest point is within ring bounds
        return distToLine >= innerRadius && distToLine <= outerRadius
    }

    override fun toString(): String =
        "Ring(center=$_center, innerRadius=$innerRadius, outerRadius=$outerRadius)"

    companion object {
        inline operator fun invoke(
            x: Number,
            y: Number,
            innerRadius: Number,
            outerRadius: Number,
            totalPoints: Int = 32
        ) = Ring(
            x.toDouble(),
            y.toDouble(),
            innerRadius.toDouble(),
            outerRadius.toDouble(),
            totalPoints
        )
    }
}

/**
 * Integer variant of Ring.
 */
inline class RingInt(val ring: Ring) {
    constructor(
        x: Int,
        y: Int,
        innerRadius: Int,
        outerRadius: Int,
        totalPoints: Int = 32
    ) : this(
        Ring(
            x.toDouble(),
            y.toDouble(),
            innerRadius.toDouble(),
            outerRadius.toDouble(),
            totalPoints
        )
    )

    val x: Int get() = ring.x.toInt()
    val y: Int get() = ring.y.toInt()
    val innerRadius: Int get() = ring.innerRadius.toInt()
    val outerRadius: Int get() = ring.outerRadius.toInt()
    val width: Int get() = outerRadius - innerRadius
    val center: PointInt get() = PointInt(x, y)
}

// Conversion extensions
inline val IRing.int: RingInt
    get() = RingInt(this as? Ring ?: Ring(center, innerRadius, outerRadius))
inline val RingInt.float: IRing get() = ring
