import java.math.RoundingMode

fun extend(
    x1: Int,  // Inclusive
    y1: Double,
    x2: Int,  // Exclusive
    y2: Double,
    decimals: Int
): List<Pair<Int, Double>> {
    val xDiff = x2 - x1
    val yStep = (y2 - y1) / xDiff

    return (0 until xDiff).map { i ->
        // number.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
        Pair(x1 + i, (y1 + yStep * i).toBigDecimal().setScale(decimals, RoundingMode.HALF_EVEN).toDouble())
    }
}