@file:Suppress("NOTHING_TO_INLINE")

package ru.tolsi.aik.geom

import ru.tolsi.aik.geom.math.almostEquals

/**
 * Interface for a geometric vertex with metadata.
 * Represents a point wrapper with arbitrary key-value data for graph/triangulation algorithms.
 */
interface IVertex : Figure2D {
    val point: IPoint
    fun getMetadata(key: String): Any?
    fun hasMetadata(key: String): Boolean

    override val points: IPointArrayList
        get() = PointArrayList(1).apply { add(point) }

    override val closed: Boolean
        get() = false

    override fun containsPoint(x: Double, y: Double): Boolean =
        almostEquals(point.x, x) && almostEquals(point.y, y)

    companion object {
        operator fun invoke(x: Double, y: Double, metadata: Map<String, Any> = emptyMap()): IVertex =
            Vertex(x, y, metadata)
        operator fun invoke(point: IPoint, metadata: Map<String, Any> = emptyMap()): IVertex =
            Vertex(point, metadata)
    }
}

/**
 * Geometric vertex with metadata storage.
 * A point wrapper that can store arbitrary key-value data for use in graph algorithms,
 * triangulation, or other geometric computations that need to associate data with points.
 *
 * @property point The underlying geometric point
 * @property metadata Initial metadata map (copied to internal storage)
 */
open class Vertex(
    override val point: IPoint,
    metadata: Map<String, Any> = emptyMap()
) : IVertex {
    protected val _metadata: MutableMap<String, Any> = metadata.toMutableMap()

    constructor(x: Double, y: Double, metadata: Map<String, Any> = emptyMap()) :
            this(Point(x, y), metadata)

    /**
     * Sets metadata value for the given key.
     */
    fun setMetadata(key: String, value: Any) {
        _metadata[key] = value
    }

    /**
     * Gets metadata value for the given key, or null if not present.
     */
    override fun getMetadata(key: String): Any? = _metadata[key]

    /**
     * Checks if metadata contains the given key.
     */
    override fun hasMetadata(key: String): Boolean = _metadata.containsKey(key)

    /**
     * Removes metadata value for the given key.
     */
    fun removeMetadata(key: String): Any? = _metadata.remove(key)

    /**
     * Returns immutable copy of all metadata.
     */
    fun getAllMetadata(): Map<String, Any> = _metadata.toMap()

    /**
     * Clears all metadata.
     */
    fun clearMetadata() {
        _metadata.clear()
    }

    override fun toString(): String = "Vertex(point=$point, metadata=$_metadata)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Vertex) return false
        return almostEquals(point.x, other.point.x) &&
                almostEquals(point.y, other.point.y) &&
                _metadata == other._metadata
    }

    override fun hashCode(): Int {
        var result = PointEPSKey(Point(point)).hashCode()
        result = 31 * result + _metadata.hashCode()
        return result
    }
}

/**
 * Integer variant of Vertex.
 */
inline class VertexInt(val vertex: Vertex) {
    constructor(x: Int, y: Int, metadata: Map<String, Any> = emptyMap()) :
            this(Vertex(x.toDouble(), y.toDouble(), metadata))

    val x: Int get() = vertex.point.x.toInt()
    val y: Int get() = vertex.point.y.toInt()
    val point: PointInt get() = PointInt(x, y)

    fun setMetadata(key: String, value: Any) = vertex.setMetadata(key, value)
    fun getMetadata(key: String): Any? = vertex.getMetadata(key)
    fun hasMetadata(key: String): Boolean = vertex.hasMetadata(key)
}

// Conversion extensions
inline val IVertex.int: VertexInt get() = VertexInt(this as? Vertex ?: Vertex(point))
inline val VertexInt.float: IVertex get() = vertex
