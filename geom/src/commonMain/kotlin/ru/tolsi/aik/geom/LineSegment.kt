package ru.tolsi.aik.geom

class LineSegment(override val from: IPoint, override val to: IPoint): ILine {
    override fun intersects(p: IPoint): Boolean {
        return intersectsAsSegment(p)
    }

    override fun intersects(l: ILine): IPoint? {
        return intersectsAsSegment(l)
    }
}