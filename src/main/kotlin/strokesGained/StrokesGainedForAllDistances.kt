package strokesGained

class StrokesGainedForAllDistances(
    private val strokesGainedByDistance: Map<Int, Double>
) {
    fun get(distance: Int): Double {
        return strokesGainedByDistance.getOrElse(distance, {
            throw IllegalArgumentException("Distance $distance not available")
        })
    }
}