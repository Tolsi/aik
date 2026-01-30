# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

AIK is a multi-platform Kotlin library for 2D Euclidean geometry and AI algorithms (pathfinding, TSP, field-of-view). The project consists of three modules:
- **geom**: Core 2D geometry library with shapes (Point, Line, Polygon, Triangle, Rectangle, Circle) and algorithms
- **ai**: Pathfinding (BFS/DFS), TSP solvers, and field-of-view rendering
- **geom_debug**: JVM-only Swing-based visualization for debugging geometric operations

Originally forked from [Korma](https://github.com/korlibs/korma) but redesigned with a different hierarchy.

## Build Commands

### Build Requirements
- Gradle 9.3.1 (via wrapper)
- Kotlin 2.3.0
- Java 25 (builds successfully, compiles to JVM target 21)

### Common Commands
```bash
# Build all modules
./gradlew build

# Build specific module
./gradlew :geom:build
./gradlew :ai:build
./gradlew :geom_debug:build

# Run tests
./gradlew test

# Run tests for specific module
./gradlew :geom:jvmTest
./gradlew :ai:jvmTest

# Clean build
./gradlew clean

# Publish to Maven Local
./gradlew publishToMavenLocal
```

## Project Structure

```
aik/
├── geom/                           # Core geometry module
│   └── src/
│       ├── commonMain/kotlin/      # Multi-platform geometry code
│       │   └── ru/tolsi/aik/geom/
│       │       ├── Point.kt        # 2D point with vector operations
│       │       ├── Line.kt         # Infinite lines
│       │       ├── LineSegment.kt  # Bounded line segments
│       │       ├── Ray.kt          # Half-infinite rays
│       │       ├── Polygon.kt      # Closed polygons with Sutherland-Hodgman clipping
│       │       ├── Triangle.kt     # Triangles with specialized operations
│       │       ├── Rectangle.kt    # Axis-aligned bounding boxes
│       │       ├── Circle.kt       # Circles (approximated as polygons)
│       │       ├── Polyline.kt     # Open paths
│       │       ├── Figure2D.kt     # Root interface for 2D shapes
│       │       ├── Angle.kt        # Angle handling in radians
│       │       ├── PointArrayList.kt  # Efficient point storage
│       │       ├── debug/          # Debug drawing interface
│       │       ├── math/           # Math utilities
│       │       └── range/          # Range types
│       ├── jvmMain/kotlin/         # JVM-specific implementations
│       └── commonTest/kotlin/      # Multi-platform tests
├── ai/                             # AI algorithms module
│   └── src/commonMain/kotlin/
│       └── ru/tolsi/aik/ai/
│           ├── PathFinding.kt      # BFS/DFS grid pathfinding
│           ├── TSP.kt              # Travelling Salesman Problem solvers
│           ├── ViewBounds.kt       # Field-of-view rendering
│           └── platformer/
│               └── GameField.kt    # Tile-based grid with polygon conversion
└── geom_debug/                     # JVM-only debug visualization
    └── src/main/kotlin/
        └── ru/tolsi/aik/geom/draw/
            ├── AWTDraw.kt          # Swing/AWT rendering backend
            ├── GeometricPanelWithZoom.kt  # Interactive visualization panel
            └── SwingApp.kt         # Standalone Swing application
```

## Architecture Patterns

### Interface + Implementation Pattern (CRITICAL)
**ALWAYS use interfaces (I-prefix) instead of concrete classes for all geometric types.**

This pattern enables support for both Int and Double (Decimal) realizations:
- **Interface** (I-prefix): `IPoint`, `ILine`, `IPolygon`, `IKite`, `IRectangle`, etc.
- **Implementation**: `Point`, `Line`, `Polygon`, `Kite`, `Rectangle`, etc.
- **Integer variants**: `PointInt`, `RectangleInt`, `KiteInt`, etc. (inline classes)
- **Extensions**: Operator overloading and helper functions

**Pattern structure:**
```kotlin
// 1. Interface with I-prefix
interface IKite : IPolygon {
    val p0: IPoint
    val p1: IPoint
    // ... properties
}

// 2. Implementation (open class for extensibility)
open class Kite(
    p0: IPoint,
    p1: IPoint,
    // ...
    validate: Boolean = true
) : IKite {
    protected var _p0: Point = Point(p0.x, p0.y)
    // ... protected storage

    override val p0: IPoint get() = _p0
    // ... implement interface
}

// 3. Integer variant (inline class)
inline class KiteInt(val kite: Kite) {
    // ... int accessors
}
```

**Why this matters:**
- Enables polymorphism across Double/Int types
- Supports multi-platform implementations
- Maintains immutable interface contracts while allowing mutable internal state
- Consistent with existing codebase architecture

### Mutable vs Immutable
- Interfaces define immutable contracts (`IPoint`, `IRectangle`, `ISize`)
- Concrete classes are mutable for algorithm efficiency
- Convert via `.mutable` and `.immutable` properties

### Double vs Integer Variants
- Double precision: `Point`, `Rectangle`, `Size` for geometry
- Integer variants: `PointInt`, `RectangleInt`, `SizeInt` for discrete grids
- Cross-convert via `.int` and `.float` properties

### Epsilon-based Comparison
All floating-point comparisons use epsilon tolerance:
- Global constant: `Geometry.EPS = 1e-9`
- Point comparison uses `PointEPSKey` for hash-based collections
- All geometric operations account for floating-point precision

### Polygon Orientation (Winding Order)

**CRITICAL**: All closed polygons (IPolygon implementations) MUST use **counter-clockwise (CCW)** point ordering for polygon clipping operations to work correctly.

**Why CCW orientation is required:**

The Sutherland-Hodgman polygon clipping algorithm (`IPolygon.clip()`) uses the cross product to determine if a point is "inside" or "outside" relative to each edge:

```kotlin
val pos = (x2 - x1) * (py - y1) - (y2 - y1) * (px - x1)
if (pos < 0) { /* point is inside */ }
```

This formula computes the Z-component of the cross product, where:
- **Negative value** → point is to the RIGHT of the directed edge (from p1 to p2)
- **Positive value** → point is to the LEFT of the directed edge

For a **counter-clockwise polygon**, as you traverse the edges in order, the interior of the polygon is on your **right side** (negative cross product). This is why the algorithm checks `pos < 0` for "inside".

For a **clockwise polygon**, the interior would be on the left side, causing the clipping algorithm to fail (returning empty results or incorrect geometry).

**Checking orientation:**

Use the signed area (shoelace formula) to determine orientation:
```kotlin
fun signedArea(points: IPointArrayList): Double {
    var area = 0.0
    var j = points.size - 1
    for (i in points.indices) {
        area += (points.getX(j) + points.getX(i)) * (points.getY(j) - points.getY(i))
        j = i
    }
    return area / 2.0
}
```
- **Positive signed area** → counter-clockwise (CCW) ✓ correct
- **Negative signed area** → clockwise (CW) ✗ needs reversal

**Shapes requiring CCW orientation:**
- Stadium (fixed in Stadium.kt:70-107)
- Circle (already CCW via Angle.cos01/sin01)
- Ellipse
- CircularSector
- CircularSegment
- Ring
- All quadrilaterals (Rectangle, Kite, Parallelogram, Rhombus, Trapezoid, Square)
- Triangle (has fixOrientation parameter to handle this)
- Custom Polygon instances

**Testing orientation:**

All shape test files should include a `testCounterClockwiseOrientation()` test that verifies positive signed area.

## Key Geometric Hierarchies

```
Figure2D (root interface)
├── ILine (infinite lines, rays, segments)
│   ├── Line (unbounded)
│   ├── LineSegment (bounded segment, also Vector2D)
│   └── Ray (half-open segment)
├── IPolygon extends Figure2D, WithArea
│   ├── Polygon (general N-sided polygon)
│   ├── Rectangle (axis-aligned bounding box)
│   ├── Triangle (3-point polygon with specialized operations)
│   └── Circle (approximated as N-sided polygon)
└── Polyline (open path)
```

## Core Algorithms

### Geometry (geom module)
- **Shoelace formula**: Polygon area calculation (Polygon.kt:12-25)
- **Ray casting**: Point-in-polygon containment test (Point.kt)
- **Sutherland-Hodgman**: Polygon clipping algorithm (Polygon.kt:53-68)
- **Cohen-Sutherland**: Line-polygon intersection (Polygon.kt:47-50)
- **Barycentric coordinates**: Point-in-triangle test (Triangle.kt)
- **Incircle test**: For Delaunay triangulation support (Triangle.kt)

### AI Algorithms (ai module)
- **BFS/DFS pathfinding**: Grid-based shortest path (PathFinding.kt)
- **TSP solver**: Two-way BFS approach recommended over DFS (TSP.kt)
  - `twoWaysBfsTravellingSalesmanProblem()` - faster, recommended
  - `dfsTravellingSalesmanProblem()` - slower alternative
- **Field-of-view**: Ray-casting with polygon clipping for vision cones (ViewBounds.kt)

## Dependencies

- **com.soywiz:korlibs-datastructure:6.0.0** - Kotlin Data Structures from KorGE/Korlibs (Queue, Stack, Array2, DoubleArrayList, IntArrayList)
  - Package: `korlibs.datastructure.*`
  - Part of the KorGE/Korlibs ecosystem
- **Kotlin stdlib** (2.3.0) - Multi-platform standard library
- **JUnit** (jvmTest only) - Testing framework

## Debug Visualization

The geometry module includes a pluggable debug renderer:

```kotlin
// In your code, inject the debug drawer
Geometry.debug = AWTDebugDrawer(panel)

// Draw shapes during operations
Geometry.debug.draw(myPolygon, depth = 0.5f, Color.Red)
```

Default is `DebugDrawer.Mock` (no-op). Swap with `AWTDebugDrawer` for Swing visualization.

## Testing

Tests use Kotlin's `kotlin.test` framework:
- Common tests in `src/commonTest/kotlin/` (multi-platform)
- JVM-specific tests in `src/jvmTest/kotlin/`
- Test files: `PointTests.kt`, `LineTests.kt`, `LineSegmentTests.kt`, `RayTests.kt`

## Multi-platform Support

Current targets:
- **JVM** (primary target, Java 8 compatible)
- **macOS** (configured but minimal implementation)
- **JavaScript** (disabled, can be re-enabled in build.gradle)

Platform-specific code goes in `{platform}Main/` and `{platform}Test/` directories.

## Important Notes

### Coordinate System
- All coordinates are stored as `Double`
- Y-axis typically points up (mathematical convention)
- In debug visualization, Y-axis is inverted for screen coordinates

### Point Classification
Lines and segments classify points relative to themselves:
- `LEFT`, `RIGHT` - point is left/right of line
- `BETWEEN` - point is on segment between endpoints
- `START`, `END` - point is at segment endpoint
- `BEHIND`, `BEYOND` - point is on line but outside segment bounds

### Performance Considerations
- Use `PointArrayList` instead of `List<Point>` for large collections (separate X/Y arrays)
- Use integer variants (`PointInt`, `RectangleInt`) for grid-based operations
- TSP: prefer `twoWaysBfsTravellingSalesmanProblem()` over DFS variant
- Polygon simplification removes collinear points: use `IPolygon.simplify()`

## Shape Constructors Reference

**IMPORTANT**: Different shapes accept different parameter types. Always check constructor signatures before instantiating shapes.

### Quick Reference Table

| Shape | Accepts IPoint | Accepts x,y coords | Notes |
|-------|----------------|-------------------|-------|
| **Line** | ✓ | ✓ (via operator) | `Line(from: IPoint, to: IPoint)` or `Line(x1, y1, x2, y2)` |
| **LineSegment** | ✓ | ✗ | `LineSegment(from: IPoint, to: IPoint)` only |
| **Ray** | ✓ | ✗ | `Ray(from: IPoint, to: IPoint)` only |
| **Triangle** | ✓ | ✗ | `Triangle(p0, p1, p2, fixOrientation, checkOrientation)` |
| **Rectangle** | ✗ | ✓ | `Rectangle(x, y, width, height)` |
| **Circle** | ✗ | ✓ | `Circle(x, y, radius, totalPoints)` |
| **Polygon** | ✓ | ✗ | `Polygon(points: IPointArrayList)` |
| **Polyline** | ✓ | ✗ | `Polyline(points: IPointArrayList)` |
| **Kite** | ✓ | ✗ | `Kite(p0, p1, p2, p3, validate)` |
| **Parallelogram** | ✓ | ✗ | `Parallelogram(p0, edge1, edge2, validate)` |
| **Trapezoid** | ✓ | ✗ | `Trapezoid(p0, p1, p2, p3, validate)` |
| **CircularSector** | ✓ | ✓ | Both: `(x, y, ...)` or `(center: IPoint, ...)` |
| **CircularSegment** | ✓ | ✓ | Both: `(x, y, ...)` or `(center: IPoint, ...)` |
| **Ring** | ✓ | ✓ | Both: `(x, y, ...)` or `(center: IPoint, ...)` |
| **Stadium** | ✓ | ✓ | Both: `(x, y, ...)` or `(center: IPoint, ...)` |
| **Ellipse** | ✓ | ✓ | Both: `(x, y, ...)` or `(center: IPoint, ...)` |

### Detailed Constructor Signatures

#### Lines and Rays
```kotlin
// Line - accepts both
Line(from: IPoint, to: IPoint)
Line(x1: Number, y1: Number, x2: Number, y2: Number)  // via operator invoke

// LineSegment - IPoint only
LineSegment(from: IPoint, to: IPoint)

// Ray - IPoint only
Ray(from: IPoint, to: IPoint)
```

#### Polygonal Shapes
```kotlin
// Triangle - IPoint only (via operator invoke)
Triangle(p0: IPoint, p1: IPoint, p2: IPoint,
         fixOrientation: Boolean = false,
         checkOrientation: Boolean = true)

// Rectangle - raw coordinates
Rectangle(x: Double, y: Double, width: Double, height: Double)
Rectangle(x: Number, y: Number, width: Number, height: Number)  // via operator

// Polygon - collection of points
Polygon(points: IPointArrayList)

// Polyline - collection of points
Polyline(points: IPointArrayList)
```

#### Quadrilaterals
```kotlin
// Kite - IPoint only
Kite(p0: IPoint, p1: IPoint, p2: IPoint, p3: IPoint, validate: Boolean = true)

// Parallelogram - IPoint only
Parallelogram(p0: IPoint, edge1: IPoint, edge2: IPoint, validate: Boolean = true)

// Trapezoid - IPoint only
Trapezoid(p0: IPoint, p1: IPoint, p2: IPoint, p3: IPoint, validate: Boolean = true)
```

#### Circular Shapes
```kotlin
// Circle - raw coordinates only
Circle(x: Double, y: Double, radius: Double, totalPoints: Int = 32)

// CircularSector - both variants
CircularSector(x: Double, y: Double, radius: Double,
               startAngle: Angle, endAngle: Angle, totalPoints: Int = 32)
CircularSector(center: IPoint, radius: Double,
               startAngle: Angle, endAngle: Angle, totalPoints: Int = 32)

// CircularSegment - both variants
CircularSegment(x: Double, y: Double, radius: Double,
                startAngle: Angle, endAngle: Angle, totalPoints: Int = 32)
CircularSegment(center: IPoint, radius: Double,
                startAngle: Angle, endAngle: Angle, totalPoints: Int = 32)

// Ring - both variants
Ring(x: Double, y: Double, innerRadius: Double, outerRadius: Double,
     totalPoints: Int = 32)
Ring(center: IPoint, innerRadius: Double, outerRadius: Double,
     totalPoints: Int = 32)

// Ellipse - both variants
Ellipse(x: Double, y: Double, semiMajorAxis: Double, semiMinorAxis: Double,
        rotation: Angle = Angle.ZERO, totalPoints: Int = 64)
Ellipse(center: IPoint, semiMajorAxis: Double, semiMinorAxis: Double,
        rotation: Angle = Angle.ZERO, totalPoints: Int = 64)

// Stadium - both variants (WARNING: may have point duplication issues)
Stadium(x: Double, y: Double, width: Double, height: Double,
        totalPoints: Int = 16)
Stadium(center: IPoint, width: Double, height: Double,
        totalPoints: Int = 16)
```

### Common Patterns

**Creating shapes with Point objects:**
```kotlin
val p1 = Point(10, 20)
val p2 = Point(30, 40)

val line = Line(p1, p2)              // ✓ Works
val segment = LineSegment(p1, p2)    // ✓ Works
val triangle = Triangle(p1, p2, Point(50, 60))  // ✓ Works
```

**Creating shapes with raw coordinates:**
```kotlin
val line = Line(10, 20, 30, 40)           // ✓ Works (operator invoke)
val rect = Rectangle(10, 20, 100, 50)     // ✓ Works
val circle = Circle(50, 50, 25)           // ✓ Works
val sector = CircularSector(50, 50, 25, Angle.ZERO, Angle.fromDegrees(90))  // ✓ Works
```

**Common mistakes to avoid:**
```kotlin
// ✗ WRONG - LineSegment doesn't accept raw coordinates
val segment = LineSegment(10, 20, 30, 40)  // COMPILE ERROR

// ✓ CORRECT
val segment = LineSegment(Point(10, 20), Point(30, 40))

// ✗ WRONG - Circle requires raw coordinates, not Point
val circle = Circle(Point(50, 50), 25)  // COMPILE ERROR (unless using secondary constructor)

// ✓ CORRECT
val circle = Circle(50, 50, 25)

// ✗ WRONG - Triangle parameter names changed
val tri = Triangle(p1, p2, p3, validate = false)  // COMPILE ERROR

// ✓ CORRECT
val tri = Triangle(p1, p2, p3, fixOrientation = false, checkOrientation = false)
```
