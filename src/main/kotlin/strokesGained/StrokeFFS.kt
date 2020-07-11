package strokesGained

import DenominatedValue
import Ground

data class StrokeFFS(
    val ground: Ground,
    val distanceToPin: DenominatedValue,
    val leadToPenalty: Boolean
)