import strokesGained.StrokesGainedBenchmarkRepository
import strokesGained.StrokesGainedForAllDistances
import kotlin.math.roundToInt

class BenchmarkAndInterpolation(
    // TODO: Also accept an interpolation interface?
    private val strokesGainedBenchmarkRepository: StrokesGainedBenchmarkRepository
): Benchmark {
    private val strokesGainedByGround = mutableMapOf<Ground, StrokesGainedForAllDistances>()

    init {
        // TODO: Do in constructor or companion object? Use cache or create object once?
        strokesGainedBenchmarkRepository.getGrounds().forEach { ground ->
            strokesGainedByGround.putIfAbsent(ground, calc(ground))
        }
    }

    override fun get(denominatedValue: DenominatedValue, ground: Ground): Double {
        return strokesGainedByGround.getOrElse(
            ground, {
                throw IllegalArgumentException("Ground $ground not configured")  // TODO: Custom exception
            }
        ).get(denominatedValue.distance.toInt())
    }

    private fun calc(ground: Ground): StrokesGainedForAllDistances {
        val strokesGainedByDistance = strokesGainedBenchmarkRepository.getAll(ground)

        val keysSorted = strokesGainedByDistance.keys.sortedBy { it.distance }

        var retVal = mutableMapOf<Int, Double>()

        for (i in (keysSorted.indices.take(keysSorted.size - 1))) {
            val distance1 = keysSorted[i]
            val strokesGained1 = strokesGainedByDistance[distance1]!!

            val distance2 = keysSorted[i + 1]
            val strokesGained2 = strokesGainedByDistance[distance2]!!

            val strokesGainedByInterpolatedDistances = extend(
                distance1.distance.roundToInt(),
                strokesGained1,
                distance2.distance.roundToInt(),
                strokesGained2,
                3  // TODO: Configurable
            ).toMap()

            retVal.putAll(strokesGainedByInterpolatedDistances)
        }

        // Add the last distance element which was excluded in the loop
        retVal.put(
            keysSorted.last().distance.roundToInt(),
            strokesGainedByDistance[keysSorted.last()]!!
        )

        return StrokesGainedForAllDistances(retVal)
    }
}