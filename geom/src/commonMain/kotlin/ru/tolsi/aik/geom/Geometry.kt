package ru.tolsi.aik.geom

import ru.tolsi.aik.geom.debug.DebugDrawer

// todo pass it to the figures?
object Geometry {
    var EPS: Double = 1e-9
    var bounds: Size = Size(100, 100)
    var debug: DebugDrawer = DebugDrawer.Mock
}