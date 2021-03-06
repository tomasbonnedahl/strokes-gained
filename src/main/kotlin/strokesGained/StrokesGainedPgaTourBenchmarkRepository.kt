package strokesGained

import DenominatedValue
import Ground

class StrokesGainedPgaTourBenchmarkRepository(
    // ******
    // TODO: Let this class be outside domain and fetch from db in any way that is fitting
    // ******

    // TODO: Add sand
    private val theMap: Map<Ground, Map<DenominatedValue, Double>>
): StrokesGainedBenchmarkRepository {
    override fun getAll(ground: Ground): Map<DenominatedValue, Double> {
        return theMap.getOrElse(ground, {
            throw IllegalArgumentException("Ground $ground not supported")
        })
    }

    override fun getGrounds(): Set<Ground> {
        // TODO: Data driven? Add Recovery and Sand
        return theMap.keys
    }
}