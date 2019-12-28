package ru.tolsi.aik.geom.debug

import ru.tolsi.aik.geom.Figure2D

interface DebugDrawer {
    fun draw(f: Figure2D, depth: Float, c: Color)
    fun clear()

    companion object {
        val Mock = object : DebugDrawer {
            override fun draw(f: Figure2D, depth: Float, c: Color) {}
            override fun clear() {}
        }
    }
}