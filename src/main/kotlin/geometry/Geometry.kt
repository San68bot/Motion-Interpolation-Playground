package geometry

import kotlin.math.cos
import kotlin.math.sin

data class Point(var x: Double, var y: Double)

data class Position(var x: Double, var y: Double, var theta: Double) {
    constructor(point: Point, theta: Double) : this(point.x, point.y, theta)

    val point: Point
        get() = Point(x, y)

    val deg: Double
        get() = theta.toDegrees()

    fun headingVec() = Point(cos(theta), sin(theta))

    override fun toString() = String.format("(%.3f, %.3f, %.3f°)", x, y, deg)
}

fun Number.toDegrees(): Double = Math.toDegrees(this.toDouble())
fun Number.toRadians(): Double = Math.toRadians(this.toDouble())