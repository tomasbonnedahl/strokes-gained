data class HoleEntry(
    val distances: List<DistanceToPin>,
    val hole: Int,
    val strokes: Int,  // TODO: Validate strokes based on distances?
    val putts: Int
)