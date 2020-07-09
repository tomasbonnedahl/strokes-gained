class StrokesGainedPgaTourBenchmarkService(
    // TODO: Add sand
    val strokesByDistanceTee: Map<Double, Double>,  // TODO: Double -> DenominatedValue
    val strokesByDistanceFairway: Map<Double, Double>,
    val strokesByDistanceRough: Map<Double, Double>,
    val strokesByDistanceRecovery: Map<Double, Double>,  // TODO: Have an enum for the categories
    val strokesByDistancePutt: Map<Double, Double>
): StrokesGainedBenchmarkService {
    override fun tee(distance: Double): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fairway(distance: Double): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun rough(distance: Double): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun recovery(distance: Double): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun puttInFeet(distance: Double): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun get(denominatedValue: DenominatedValue, ground: Ground): Double {
        return when (ground) {
            Ground.TEE -> strokesByDistanceTee[denominatedValue.distance]!!
            Ground.FAIRWAY -> strokesByDistanceFairway[denominatedValue.distance]!!
            Ground.ROUGH -> strokesByDistanceRough[denominatedValue.distance]!!
            Ground.GREEN -> strokesByDistancePutt[denominatedValue.distance]!!
        }
    }
}