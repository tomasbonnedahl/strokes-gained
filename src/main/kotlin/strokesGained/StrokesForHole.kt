package strokesGained

import strokesGained.StrokeFFS

data class StrokesForHole(
    val hole: Int,
    val distances: List<StrokeFFS>,
    val strokes: Int,  // TODO: Validate strokes based on distances?
    val putts: Int
)