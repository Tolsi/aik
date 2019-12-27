package ru.tolsi.aik.geom

open class Ray (override val from: IPoint, override val to: IPoint): ILine {
    override fun intersects(p: IPoint): Boolean {
        return intersectsAsRay(p)
    }

    override fun intersects(l: ILine): IPoint? {
        return intersectsAsRay(l)
    }

    // todo make global bounds? make for a ray
//        fun fromPointAndAngle(from: Point, angle: Point): Line {
//            val speedPoint = angle.copy().normalize()
//            val infiniteLine = Line(from, speedPoint)
//            val intersectionWithLevel = Global.level.boundLines().map { infiniteLine.infiniteIntersects(it) }.filterNotNull().first()
//            return Line(from, intersectionWithLevel)
//        }


}