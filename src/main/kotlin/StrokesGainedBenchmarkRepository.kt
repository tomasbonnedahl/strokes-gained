// TODO: How to indicate if using Meters, Yards, Feet? By using template? <Meter>?
interface StrokesGainedBenchmarkRepository {
    fun get(denominatedValue: DenominatedValue, ground: Ground): Double
    fun getAll(ground: Ground): Map<DenominatedValue, Double>
    fun getGrounds(): Set<Ground>
}