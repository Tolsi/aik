package ru.tolsi.aik.geom

data class Ray private constructor(override val from: Point, override val to: Point): ILine {
    override fun intersects(p: Point): Boolean {
        return intersectsAsRay(p)
    }

    override fun intersects(l: ILine): Point? {
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