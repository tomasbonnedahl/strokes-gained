import kotlin.math.roundToInt

class BenchmarkAndInterpolation(
    // TODO: Accept interpolation interface
    val strokesGainedBenchmarkRepository: StrokesGainedBenchmarkRepository
) {
    private val valuesByGround = mutableMapOf<Ground, StrokesGainedForAllDistances>()

    init {
        // TODO: Do in constructor or companion object? Use cache or create object once?
        strokesGainedBenchmarkRepository.getGrounds().forEach { ground ->
            valuesByGround.putIfAbsent(ground, calc(ground))
        }
    }

    fun calc(ground: Ground): StrokesGainedForAllDistances {
        val strokesGainedByDistance = strokesGainedBenchmarkRepository.getAll(ground)

        val keysSorted = strokesGainedByDistance.keys.sortedBy { it.distance }
//        for (i in keysSorted - 1) {
//        for (i in keysSorted - 1) {

        var retVal = mutableMapOf<Int, Double>()

        for (i in (keysSorted.indices - 1)) {
            val key = keysSorted[i]
            val theValue = strokesGainedByDistance[key]!!
            println("i = ${i} ${key} ${theValue}")
//            val theValue2 = asfaf[i + 1]!!

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

            println("something = ${something}")
        }

        println("retVal = ${retVal}")
        return StrokesGainedForAllDistances(retVal)
    }

    fun get(denominatedValue: DenominatedValue, ground: Ground): Double {
        // TODO: Safety
        return valuesByGround[ground]!!.get(denominatedValue.distance.toInt())
    }
}