class StrokesGainedPgaTourBenchmarkRepository(
    // TODO: Add sand
//    val strokesByDistanceTee: Map<Double, Double>,  // TODO: Double -> DenominatedValue
    val strokesByDistanceTee: Map<DenominatedValue, Double>,  // TODO: Double -> DenominatedValue
    val strokesByDistanceFairway: Map<Double, Double>,
    val strokesByDistanceRough: Map<Double, Double>,
    val strokesByDistanceRecovery: Map<Double, Double>,
    val strokesByDistancePutt: Map<Double, Double>
): StrokesGainedBenchmarkRepository {
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
        TODO("Not impl")
//        return when (ground) {
//            Ground.TEE -> strokesByDistanceTee[denominatedValue.distance]!!
//            Ground.FAIRWAY -> strokesByDistanceFairway[denominatedValue.distance]!!
//            Ground.ROUGH -> strokesByDistanceRough[denominatedValue.distance]!!
//            Ground.GREEN -> strokesByDistancePutt[denominatedValue.distance]!!
//        }
    }

    override fun getAll(ground: Ground): Map<DenominatedValue, Double> {
        return when (ground) {
//            Ground.TEE -> mapOf(
//                DenominatedValue(100.0, DistanceUnit.METERS) to 2.5,
//                DenominatedValue(500.0, DistanceUnit.METERS) to 4.5
//                )
            Ground.TEE -> strokesByDistanceTee
            else -> TODO("Not implemented")
        }
    }

    override fun getGrounds(): Set<Ground> {
        // TODO: Data driven? Add Recovery and Sand
//        return setOf(Ground.TEE, Ground.FAIRWAY, Ground.ROUGH, Ground.GREEN)
        return setOf(Ground.TEE)
    }
}