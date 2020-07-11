import strokesGained.StrokesForHole

data class Round(
    val courseAndDate: CourseAndDate,
    val strokesPerHole: List<StrokesForHole>
)