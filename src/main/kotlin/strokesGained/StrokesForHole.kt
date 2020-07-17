package strokesGained

data class StrokesForHole(
    val hole: Int,
    val distances: List<Stroke>,
    val strokes: Int,  // TODO: Validate strokes based on distances?
    val putts: Int
)