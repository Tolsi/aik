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

data class DrawnObject(val f: Figure2D, val depth: Float, val c: Color)

class DrawerBuffer: DebugDrawer {
    val drawn: MutableList<DrawnObject> = mutableListOf()
    
    override fun draw(f: Figure2D, depth: Float, c: Color) {
        drawn.add(DrawnObject(f, depth, c))
    }

    override fun clear() {
        drawn.clear()
    }
}