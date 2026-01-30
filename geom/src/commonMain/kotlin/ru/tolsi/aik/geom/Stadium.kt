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

        PointArrayList(pointsPerSemiCircle * 2 + 2).apply {
            // Counter-clockwise winding order for Sutherland-Hodgman clipping
            // Start at top-right and go counter-clockwise

            // Top right connection point
            val topRight = Point(x + halfStraight, y + radius)
            add(topRight.x, topRight.y)

            // Right semicircle (90° down to 270°, going backwards for CCW)
            // Go from top to bottom on right side
            val rightCenter = Point(x + halfStraight, y)
            for (i in (pointsPerSemiCircle - 1) downTo 1) {
                val angle = 3.0 * PI / 2.0 + i.toDouble() * PI / pointsPerSemiCircle
                add(
                    rightCenter.x + kotlin.math.cos(angle) * radius,
                    rightCenter.y + kotlin.math.sin(angle) * radius
                )
            }

            // Bottom right connection
            val bottomRight = Point(x + halfStraight, y - radius)
            add(bottomRight.x, bottomRight.y)

            // Bottom left connection
            val bottomLeft = Point(x - halfStraight, y - radius)
            add(bottomLeft.x, bottomLeft.y)

            // Left semicircle (270° up to 90°, going backwards for CCW)
            // Go from bottom to top on left side
            val leftCenter = Point(x - halfStraight, y)
            for (i in (pointsPerSemiCircle - 1) downTo 1) {
                val angle = PI / 2.0 + i.toDouble() * PI / pointsPerSemiCircle
                add(
                    leftCenter.x + kotlin.math.cos(angle) * radius,
                    leftCenter.y + kotlin.math.sin(angle) * radius
                )
            }

            // Top left connection (polygon will auto-close back to top-right)
            val topLeft = Point(x - halfStraight, y + radius)
            add(topLeft.x, topLeft.y)
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
     * Check if this stadium intersects with a point.
     */
    infix fun intersects(point: IPoint): Boolean = containsPoint(point.x, point.y)

    /**
     * Check if this stadium intersects with a circle.
     */
    infix fun intersects(circle: Circle): Boolean {
        val radius = height / 2.0
        val halfStraight = (width - height) / 2.0

        // Check if circle intersects with middle rectangle
        val rectLeft = this.x - halfStraight
        val rectRight = this.x + halfStraight
        val rectTop = this.y - radius
        val rectBottom = this.y + radius

        // Closest point on rectangle to circle center
        val closestX = circle.x.coerceIn(rectLeft, rectRight)
        val closestY = circle.y.coerceIn(rectTop, rectBottom)
        val distToRect = hypot(circle.x - closestX, circle.y - closestY)

        if (distToRect <= circle.radius) {
            return true
        }

        // Check if circle intersects with left semicircle
        val leftCenterX = this.x - halfStraight
        val distToLeft = hypot(circle.x - leftCenterX, circle.y - this.y)
        if (distToLeft <= radius + circle.radius) {
            return true
        }

        // Check if circle intersects with right semicircle
        val rightCenterX = this.x + halfStraight
        val distToRight = hypot(circle.x - rightCenterX, circle.y - this.y)
        if (distToRight <= radius + circle.radius) {
            return true
        }

        return false
    }

    /**
     * Check if this stadium intersects with a rectangle.
     */
    infix fun intersects(rect: IRectangle): Boolean {
        val radius = height / 2.0
        val halfStraight = (width - height) / 2.0

        // Quick bounding box check
        val stadiumLeft = this.x - halfStraight - radius
        val stadiumRight = this.x + halfStraight + radius
        val stadiumTop = this.y - radius
        val stadiumBottom = this.y + radius

        if (stadiumRight < rect.left || stadiumLeft > rect.right ||
            stadiumBottom < rect.top || stadiumTop > rect.bottom
        ) {
            return false
        }

        // Check if any corner of rectangle is inside stadium
        if (containsPoint(rect.left, rect.top) || containsPoint(rect.right, rect.top) ||
            containsPoint(rect.left, rect.bottom) || containsPoint(rect.right, rect.bottom)
        ) {
            return true
        }

        // Check if any point on stadium is inside rectangle
        return points.any { rect.containsPoint(it.x, it.y) }
    }

    /**
     * Check if this stadium intersects with a line segment.
     */
    infix fun intersects(line: ILine): Boolean {
        // Check if either endpoint is inside stadium
        if (containsPoint(line.from.x, line.from.y) || containsPoint(line.to.x, line.to.y)) {
            return true
        }

        val radius = height / 2.0
        val halfStraight = (width - height) / 2.0

        // Check intersection with middle rectangle
        val rectLeft = this.x - halfStraight
        val rectRight = this.x + halfStraight
        val rectTop = this.y - radius
        val rectBottom = this.y + radius

        // Simple line-rectangle intersection using endpoints
        if ((line.from.x >= rectLeft && line.from.x <= rectRight &&
             line.from.y >= rectTop && line.from.y <= rectBottom) ||
            (line.to.x >= rectLeft && line.to.x <= rectRight &&
             line.to.y >= rectTop && line.to.y <= rectBottom)) {
            return true
        }

        // Check if line intersects any edge of approximated polygon
        return points.indices.any { i ->
            val p1 = points[i]
            val p2 = points[(i + 1) % points.size]
            val edge = LineSegment(p1, p2)
            line.intersects(edge) != null
        }
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
