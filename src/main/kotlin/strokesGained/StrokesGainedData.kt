package strokesGained

import DenominatedValue
import Ground

data class StrokesGainedData(
    val strokesGained: Double,
    val distanceToPin: DenominatedValue,
    val ground: Ground
)