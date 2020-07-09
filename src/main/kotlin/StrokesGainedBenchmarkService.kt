// TODO: How to indicate if using Meters, Yards, Feet? By using template? <Meter>?
interface StrokesGainedBenchmarkService {
    fun tee(distance: Double): Double
    fun fairway(distance: Double): Double
    fun rough(distance: Double): Double
    fun recovery(distance: Double): Double
    fun puttInFeet(distance: Double): Double
    fun get(denominatedValue: DenominatedValue, ground: Ground): Double
}