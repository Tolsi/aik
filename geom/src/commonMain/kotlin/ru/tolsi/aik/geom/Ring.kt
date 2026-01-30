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
