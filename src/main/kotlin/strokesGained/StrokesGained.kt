package strokesGained

import DenominatedValue
import Ground
import java.math.RoundingMode

// TODO: Interface instead with several implementations? Depend on interface, etc.
class StrokesGained(
    private val all: List<StrokesGainedData>
) {
    fun total(): Double {
        return rounded(all.sumByDouble(StrokesGainedData::strokesGained))
    }

    fun tee(): Double {
        return strokesGained(Ground.TEE)
    }

    fun fairway(): Double {
        return strokesGained(Ground.FAIRWAY)
    }

    fun green(): Double {
        return strokesGained(Ground.GREEN)
    }

    fun fairway(
        minInclusive: DenominatedValue,
        maxExclusive: DenominatedValue
    ): Double {
        return rounded(all.filter {
            it.ground == Ground.FAIRWAY
        }.filter {
            it.distanceToPin.distance >= minInclusive.distance  // TODO: Compare both distance and unit
        }.filter {
            it.distanceToPin.distance < maxExclusive.distance  // TODO: Compare both distance and unit
        }.sumByDouble(StrokesGainedData::strokesGained))
    }

    fun green(
        minInclusive: DenominatedValue,
        maxExclusive: DenominatedValue
    ): Double {
        TODO("Implement this")
    }

    private fun strokesGained(ground: Ground): Double {
        return rounded(all.filter {
            it.ground == ground
        }.sumByDouble(StrokesGainedData::strokesGained))
    }

    // TODO: Do as extension
    private fun rounded(value: Double): Double {
        return value.toBigDecimal().setScale(3, RoundingMode.HALF_EVEN).toDouble()
    }

}