package strokesGained

import DenominatedValue
import Ground

data class Stroke(
    val ground: Ground,
    val distanceToPin: DenominatedValue,
    val leadToPenalty: Boolean
)