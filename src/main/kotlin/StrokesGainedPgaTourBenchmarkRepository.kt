class StrokesGainedPgaTourBenchmarkRepository(
    // ******
    // TODO: Let this class be outside domain and fetch from db in any way that is fitting
    // ******

    // TODO: Add sand
    private val theMap: Map<Ground, Map<DenominatedValue, Double>>
): StrokesGainedBenchmarkRepository {
    override fun get(denominatedValue: DenominatedValue, ground: Ground): Double {
        TODO("Not impl")
//        return when (ground) {
//            Ground.TEE -> strokesByDistanceTee[denominatedValue.distance]!!
//            Ground.FAIRWAY -> strokesByDistanceFairway[denominatedValue.distance]!!
//            Ground.ROUGH -> strokesByDistanceRough[denominatedValue.distance]!!
//            Ground.GREEN -> strokesByDistancePutt[denominatedValue.distance]!!
//        }
    }

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