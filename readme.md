[![Jitpack](https://jitpack.io/v/Tolsi/aik.svg)](https://jitpack.io/#Tolsi/aik)

# AIK

AIK is yet another one AI library for multi-platform Kotlin (primarily for JVM).

## AI

Grid-based pathfinding algorithms with support for different movement patterns and cost functions.

[T] Path finding for an open map (A*, Dijkstra)

[T] Path finding for a partially open map (A* with obstacles)

[T] Path finding for a limited range of movements (A* with max range, reachable cells calculation)

## Geometry

AIK Geometry is a [2D](https://en.wikipedia.org/wiki/Two-dimensional_space) [Euclidean](https://en.wikipedia.org/wiki/Euclidean_geometry) geometry library.

Initially forked from [Korma](https://github.com/korlibs/korma), I found their hierarchy quite inconvenient and decided to make it convenient for my goals.

All coordinates are stored as Double, so for geometric operations, the approximation with the Epsillon value is used.

For some operations, objects with integer points will be declared.

# Planned [geometric shapes](https://en.wikipedia.org/wiki/Geometric_shape) and geometric operations

☐ - TODO, [T] - need testing, ☑ - done

I chose the geometric shapes that I used and the most popular in my opinion from the [wiki list](https://en.wikipedia.org/wiki/List_of_two-dimensional_geometric_shapes).

[T] [Point](https://en.wikipedia.org/wiki/Point_(geometry)). Also it is Vector1D

[T] [Line](https://en.wikipedia.org/wiki/Line_(geometry))

[T] [Polyline](https://en.wikipedia.org/wiki/Polygonal_chain)

[T] [Ray](https://en.wikipedia.org/wiki/Line_(geometry)#Ray) - half-open directed line segment

[T] [LineSegment](https://en.wikipedia.org/wiki/Line_segment) - closed line segment.  Also it is [Vector2D](https://en.wikipedia.org/wiki/Euclidean_vector)

[T] [Triangle](https://en.wikipedia.org/wiki/Triangle) ([topics](https://en.wikipedia.org/wiki/List_of_triangle_topics))

[T] [Rectangle](https://en.wikipedia.org/wiki/Rectangle)

[T] [Square](https://en.wikipedia.org/wiki/Square_(geometry)) ([topics](https://en.wikipedia.org/wiki/List_of_circle_topics))

[T] [Parallelogram](https://en.wikipedia.org/wiki/Parallelogram)

[T] [Trapezoid](https://en.wikipedia.org/wiki/Trapezoid)

[T] [Polygon](https://en.wikipedia.org/wiki/Polygon) ([topics](https://en.wikipedia.org/wiki/List_of_polygons,_polyhedra_and_polytopes)) - simple convex polygons

[T] [Vertex](https://en.wikipedia.org/wiki/Vertex_(geometry))

[T] Triangle Vertex - vertex with additional information about the triangle (angles, bisectors, medians, altitudes)

[T] [Circle](https://en.wikipedia.org/wiki/Circle)

[T] [Circular sector](https://en.wikipedia.org/wiki/Circular_sector)

[T] [Circular segment](https://en.wikipedia.org/wiki/Circular_segment)

[T] [Ring](https://en.wikipedia.org/wiki/Annulus_(mathematics))

[T] [Ellipse](https://en.wikipedia.org/wiki/Ellipse)

[T] [Stadium](https://en.wikipedia.org/wiki/Stadium_(geometry)) (previously listed as "Oval")

[T] [Rhombus](https://en.wikipedia.org/wiki/Rhombus)

[T] [Kite](https://en.wikipedia.org/wiki/Kite_(geometry))


