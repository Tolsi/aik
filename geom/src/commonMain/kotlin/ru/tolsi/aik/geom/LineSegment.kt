package ru.tolsi.aik.geom

class LineSegment(override val from: Point, override val to: Point): ILine {
    override fun intersects(p: Point): Boolean {
        return intersectsAsSegment(p)
    }

    override fun intersects(l: ILine): Point? {
        return intersectsAsSegment(l)
    }
}