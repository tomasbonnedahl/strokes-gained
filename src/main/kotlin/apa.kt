data class Stroke(
    val ground: Ground,
    val distanceToPin: Double,  // TODO: Do int instead?
    val distanceUnit: DistanceUnit,
    val leadToPenalty: Boolean
)

data class StrokesGained(
    val strokesGained: Double
)

// 410T (410 to pin from tee), 210F (210 to pin from tee)
