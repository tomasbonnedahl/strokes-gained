package strokesGained

import DenominatedValue
import Ground

// TODO: How to indicate if using Meters, Yards, Feet? By using template? <Meter>?
interface StrokesGainedBenchmarkRepository {
    fun getAll(ground: Ground): Map<DenominatedValue, Double>
    fun getGrounds(): Set<Ground>
}