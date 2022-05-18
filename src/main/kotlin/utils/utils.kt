package utils

infix fun Double.round(decimals: Int): Double {
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

infix fun ClosedRange<Double>.step(step: Double): Iterable<Double> {
    require(start.isFinite())
    require(endInclusive.isFinite())
    require(step > 0.0) { "Step must be positive, was: $step." }
    val sequence = generateSequence(start) { previous ->
        if (previous == Double.POSITIVE_INFINITY) return@generateSequence null
        val next = previous + step
        if (next > endInclusive) null else next
    }
    return sequence.asIterable()
}

fun Number.toDegrees(): Double = Math.toDegrees(this.toDouble())
fun Number.toRadians(): Double = Math.toRadians(this.toDouble())