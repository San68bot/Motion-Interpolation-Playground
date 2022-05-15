package utils

fun Double.round(decimals: Int = 2): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}

//wrap -180 to 180 to 0 to 360
fun Double.wrap360(): Double {
    var result = this
    if (result < 0) result += 360
    if (result > 360) result -= 360
    return result
}

//wrap 0 to 360 to -180 to 180
fun Double.wrap180(): Double {
    var result = this
    if (result < -180) result += 360
    if (result > 180) result -= 360
    return result
}

fun Number.toDegrees(): Double = Math.toDegrees(this.toDouble())
fun Number.toRadians(): Double = Math.toRadians(this.toDouble())