import kotlin.math.roundToInt

class BenchmarkAndInterpolation(
    // TODO: Accept interpolation interface?
    private val strokesGainedBenchmarkRepository: StrokesGainedBenchmarkRepository
) {
    private val strokesGainedByGround = mutableMapOf<Ground, StrokesGainedForAllDistances>()

    init {
        // TODO: Do in constructor or companion object? Use cache or create object once?
        strokesGainedBenchmarkRepository.getGrounds().forEach { ground ->
            strokesGainedByGround.putIfAbsent(ground, calc(ground))
        }
    }

    fun calc(ground: Ground): StrokesGainedForAllDistances {
        val strokesGainedByDistance = strokesGainedBenchmarkRepository.getAll(ground)

        val keysSorted = strokesGainedByDistance.keys.sortedBy { it.distance }

        var retVal = mutableMapOf<Int, Double>()

        for (i in (keysSorted.indices.take(keysSorted.size - 1))) {
            val key = keysSorted[i]
            val theValue = strokesGainedByDistance[key]!!

            val keyNext = keysSorted[i + 1]
            val theValueNext = strokesGainedByDistance[keyNext]!!

            val something = extend(
                key.distance.roundToInt(),
                theValue,
                keyNext.distance.roundToInt(),
                theValueNext,
                3
            ).toMap()

            retVal.putAll(something)

        }

        // Add the last distance element which was excluded in the loop
        retVal.put(
            keysSorted.last().distance.roundToInt(),
            strokesGainedByDistance[keysSorted.last()]!!
        )

        return StrokesGainedForAllDistances(retVal)
    }

    fun get(denominatedValue: DenominatedValue, ground: Ground): Double {
        return strokesGainedByGround.getOrElse(
            ground, {
                throw IllegalArgumentException("Ground $ground not configured")  // TODO: Custom exception
            }
        ).get(denominatedValue.distance.toInt())

//        return strokesGainedByGround[ground]!!.get(denominatedValue.distance.toInt())
    }
}