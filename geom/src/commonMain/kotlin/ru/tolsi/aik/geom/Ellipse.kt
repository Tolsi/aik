@file:Suppress("NOTHING_TO_INLINE")

package ru.tolsi.aik.geom

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Interface for an ellipse.
 * Represents an oval shape with two focal points and semi-major/minor axes.
 */
interface IEllipse : IPolygon {
    val center: IPoint
    val semiMajorAxis: Double
    val semiMinorAxis: Double
    val rotation: Angle
    val focalDistance: Double get() = sqrt(semiMajorAxis.pow(2) - semiMinorAxis.pow(2))
}

/**
 * Ellipse - oval shape with two focal points.
 *
 * The ellipse is defined by a center point, two semi-axes (major and minor), and an optional rotation.
 * When rotation is 0, the major axis is aligned with the X-axis.
 *
 * @property x X-coordinate of center
 * @property y Y-coordinate of center
 * @property semiMajorAxis Semi-major axis length (a) - must be >= semiMinorAxis
 * @property semiMinorAxis Semi-minor axis length (b)
 * @property rotation Rotation angle of the ellipse (default: 0)
 * @property totalPoints Number of points to approximate the ellipse (default 64 for smooth curve)
 * @property validate Whether to validate parameters (default true)
 */
open class Ellipse(
    val x: Double,
    val y: Double,
    override val semiMajorAxis: Double,
    override val semiMinorAxis: Double,
    override val rotation: Angle = Angle.ZERO,
    val totalPoints: Int = 64,
    validate: Boolean = true
) : IEllipse {

    protected var _center: Point = Point(x, y)

    init {
        if (validate) {
            require(semiMajorAxis >= semiMinorAxis) {
                "Semi-major axis ($semiMajorAxis) must be >= semi-minor axis ($semiMinorAxis)"
            }
            require(semiMinorAxis > 0) {
                "Semi-minor axis must be positive, got $semiMinorAxis"
            }
            require(totalPoints >= 3) {
                "Total points must be at least 3, got $totalPoints"
            }
        }
    }

    constructor(
        center: IPoint,
        semiMajorAxis: Double,
        semiMinorAxis: Double,
        rotation: Angle = Angle.ZERO,
        totalPoints: Int = 64,
        validate: Boolean = true
    ) : this(center.x, center.y, semiMajorAxis, semiMinorAxis, rotation, totalPoints, validate)

    override val center: IPoint get() = _center

    override val points: IPointArrayList by lazy {
        PointArrayList(totalPoints).apply {
            val cosR = cos(rotation.radians)
            val sinR = sin(rotation.radians)

            for (i in 0 until totalPoints) {
                val t = 2 * PI * i / totalPoints
                val cosT = cos(t)
                val sinT = sin(t)

                // Parametric ellipse with rotation
                val localX = semiMajorAxis * cosT
                val localY = semiMinorAxis * sinT

                add(
                    x + localX * cosR - localY * sinR,
                    y + localX * sinR + localY * cosR
                )
            }
        }
    }

    override val closed: Boolean = true

    override val area: Double
        get() = PI * semiMajorAxis * semiMinorAxis

    override fun containsPoint(x: Double, y: Double): Boolean {
        // Transform point to ellipse-local space (inverse rotation)
        val dx = x - this.x
        val dy = y - this.y
        val cosR = cos(-rotation.radians)
        val sinR = sin(-rotation.radians)

        val localX = dx * cosR - dy * sinR
        val localY = dx * sinR + dy * cosR

        // Check if inside unit ellipse: (x/a)² + (y/b)² <= 1
        val normalized = (localX / semiMajorAxis).pow(2) + (localY / semiMinorAxis).pow(2)
        return normalized <= 1.0
    }

    /**
     * Returns true if this ellipse is a circle (both axes are equal).
     */
    val isCircle: Boolean
        get() = semiMajorAxis == semiMinorAxis

    override fun toString(): String =
        "Ellipse(center=$_center, a=$semiMajorAxis, b=$semiMinorAxis, rotation=$rotation)"

    companion object {
        inline operator fun invoke(
            x: Number,
            y: Number,
            semiMajorAxis: Number,
            semiMinorAxis: Number,
            rotation: Angle = Angle.ZERO,
            totalPoints: Int = 64
        ) = Ellipse(
            x.toDouble(),
            y.toDouble(),
            semiMajorAxis.toDouble(),
            semiMinorAxis.toDouble(),
            rotation,
            totalPoints
        )
    }
}

/**
 * Integer variant of Ellipse.
 */
inline class EllipseInt(val ellipse: Ellipse) {
    constructor(
        x: Int,
        y: Int,
        semiMajorAxis: Int,
        semiMinorAxis: Int,
        rotation: Angle = Angle.ZERO,
        totalPoints: Int = 64
    ) : this(
        Ellipse(
            x.toDouble(),
            y.toDouble(),
            semiMajorAxis.toDouble(),
            semiMinorAxis.toDouble(),
            rotation,
            totalPoints
        )
    )

    val x: Int get() = ellipse.x.toInt()
    val y: Int get() = ellipse.y.toInt()
    val semiMajorAxis: Int get() = ellipse.semiMajorAxis.toInt()
    val semiMinorAxis: Int get() = ellipse.semiMinorAxis.toInt()
    val center: PointInt get() = PointInt(x, y)
    val rotation: Angle get() = ellipse.rotation
    val focalDistance: Double get() = ellipse.focalDistance
    val isCircle: Boolean get() = ellipse.isCircle
}

// Conversion extensions
inline val IEllipse.int: EllipseInt
    get() = EllipseInt(this as? Ellipse ?: Ellipse(center, semiMajorAxis, semiMinorAxis, rotation))
inline val EllipseInt.float: IEllipse get() = ellipse
