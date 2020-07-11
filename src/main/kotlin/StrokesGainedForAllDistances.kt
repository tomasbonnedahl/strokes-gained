class StrokesGainedForAllDistances(
    val apa: Map<Int, Double>
) {
    fun get(distance: Int): Double {
        return apa.get(distance)!! // TODO: Safety
    }
}