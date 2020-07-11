// TODO: How to indicate if using Meters, Yards, Feet? By using template? <Meter>?
interface StrokesGainedBenchmarkRepository {
    fun tee(distance: Double): Double
    fun fairway(distance: Double): Double
    fun rough(distance: Double): Double
    fun recovery(distance: Double): Double
    fun puttInFeet(distance: Double): Double

    fun get(denominatedValue: DenominatedValue, ground: Ground): Double
    fun getAll(ground: Ground): Map<DenominatedValue, Double>
    fun getGrounds(): Set<Ground>
}