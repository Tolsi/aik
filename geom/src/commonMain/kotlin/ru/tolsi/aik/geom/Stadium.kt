@file:Suppress("NOTHING_TO_INLINE")

package ru.tolsi.aik.geom

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.hypot

/**
 * Interface for a stadium shape.
 * Represents a rectangle with semicircular ends (rounded rectangle with circular caps).
 */
interface IStadium : IPolygon {
    val center: IPoint
    val width: Double
    val height: Double
    val radius: Double get() = height / 2.0
    val straightLength: Double get() = width - height
}

/**
 * Stadium shape - rectangle with semicircular ends.
 *
 * A stadium (also called discorectangle or obround) consists of a rectangle with
 * semicircles attached to opposite sides. The width must be >= height so that
 * the semicircles fit properly on the short sides.
 *
 * @property x X-coordinate of center
 * @property y Y-coordinate of center
 * @property width Total width (including semicircles)
 * @property height Height (diameter of semicircles)
 * @property totalPoints Number of points to approximate each semicircle (default 16)
 * @property validate Whether to validate parameters (default true)
 */
open class Stadium(
    val x: Double,
    val y: Double,
    override val width: Double,
    override val height: Double,
    val totalPoints: Int = 16,
    validate: Boolean = true
) : IStadium {

    protected var _center: Point = Point(x, y)

    init {
        if (validate) {
            require(width >= height) {
                "Width ($width) must be >= height ($height) for stadium shape"
            }
            require(height > 0) {
                "Height must be positive, got $height"
            }
            require(totalPoints >= 2) {
                "Total points per semicircle must be at least 2, got $totalPoints"
            }
        }
    }

    constructor(
        center: IPoint,
        width: Double,
        height: Double,
        totalPoints: Int = 16,
        validate: Boolean = true
    ) : this(center.x, center.y, width, height, totalPoints, validate)

    override val center: IPoint get() = _center

    override val points: IPointArrayList by lazy {
        val radius = height / 2.0
        val halfStraight = (width - height) / 2.0
        val pointsPerSemiCircle = totalPoints

        PointArrayList(pointsPerSemiCircle * 2 + 4).apply {
            // Left semicircle (270° to 90°, or -π/2 to π/2)
            val leftCenter = Point(x - halfStraight, y)
            for (i in 0..pointsPerSemiCircle) {
                val angle = PI * (1.5 + 0.5 * i / pointsPerSemiCircle)
                add(
                    leftCenter.x + kotlin.math.cos(angle) * radius,
                    leftCenter.y + kotlin.math.sin(angle) * radius
                )
            }

            // Right semicircle (90° to 270°, or π/2 to 3π/2)
            val rightCenter = Point(x + halfStraight, y)
            for (i in 0..pointsPerSemiCircle) {
                val angle = PI * (0.5 + i / pointsPerSemiCircle)
                add(
                    rightCenter.x + kotlin.math.cos(angle) * radius,
                    rightCenter.y + kotlin.math.sin(angle) * radius
                )
            }
        }
    }

    override val closed: Boolean = true

    override val area: Double
        get() {
            val radius = height / 2.0
            val straightLength = width - height
            // Area = circle + rectangle = πr² + 2r*L
            return PI * radius * radius + 2 * radius * straightLength
        }

    override fun containsPoint(x: Double, y: Double): Boolean {
        val radius = height / 2.0
        val halfStraight = (width - height) / 2.0

        // Check middle rectangle region
        if (abs(x - this.x) <= halfStraight && abs(y - this.y) <= radius) {
            return true
        }

        // Check left semicircle
        if (x < this.x - halfStraight) {
            val leftDist = hypot(x - (this.x - halfStraight), y - this.y)
            return leftDist <= radius
        }

        // Check right semicircle
        if (x > this.x + halfStraight) {
            val rightDist = hypot(x - (this.x + halfStraight), y - this.y)
            return rightDist <= radius
        }

        return false
    }

    /**
     * Returns true if this stadium is actually a circle (width == height).
     */
    val isCircle: Boolean
        get() = width == height

    override fun toString(): String =
        "Stadium(center=$_center, width=$width, height=$height)"

    companion object {
        inline operator fun invoke(
            x: Number,
            y: Number,
            width: Number,
            height: Number,
            totalPoints: Int = 16
        ) = Stadium(
            x.toDouble(),
            y.toDouble(),
            width.toDouble(),
            height.toDouble(),
            totalPoints
        )
    }
}

/**
 * Integer variant of Stadium.
 */
inline class StadiumInt(val stadium: Stadium) {
    constructor(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        totalPoints: Int = 16
    ) : this(
        Stadium(
            x.toDouble(),
            y.toDouble(),
            width.toDouble(),
            height.toDouble(),
            totalPoints
        )
    )

    val x: Int get() = stadium.x.toInt()
    val y: Int get() = stadium.y.toInt()
    val width: Int get() = stadium.width.toInt()
    val height: Int get() = stadium.height.toInt()
    val radius: Int get() = (stadium.height / 2).toInt()
    val straightLength: Int get() = (stadium.width - stadium.height).toInt()
    val center: PointInt get() = PointInt(x, y)
    val isCircle: Boolean get() = stadium.isCircle
}

// Conversion extensions
inline val IStadium.int: StadiumInt
    get() = StadiumInt(this as? Stadium ?: Stadium(center, width, height))
inline val StadiumInt.float: IStadium get() = stadium
