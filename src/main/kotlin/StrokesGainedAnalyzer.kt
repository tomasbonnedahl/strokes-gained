class StrokesGainedAnalyzer(
    private val benchmarkAndInterpolation: BenchmarkAndInterpolation  // TODO: interface
) {
    fun analyze(
        round: Round
    ): StrokesGained {
        return StrokesGained(
            round.strokesPerHole.map { strokesForHole ->
                calculateStrokesGained(strokesForHole)
            }.flatten()
        )
    }

    fun analyze(
        rounds: List<Round>
    ): StrokesGained {
//        val asf = rounds.map(this::analyze).flatMap { it ->  }
        TODO("Return one object for all rounds, additative")
    }

    private fun calculateStrokesGained(strokesPerHole: StrokesForHole): List<StrokesGainedData> {
        strokesPerHole.distances.forEach { println("it = ${it}") }

        val sgs = mutableListOf<StrokesGainedData>()

        for (i in strokesPerHole.distances.indices.take(strokesPerHole.distances.size - 1)) {
            // Starting on the first, iterating up until the second to last
            println("i = ${i}")

            val sg = calcSg(
                strokesPerHole.distances[i],
                strokesPerHole.distances[i + 1]
            )

            sgs.add(
                StrokesGainedData(
                    strokesGained = sg,
                    distanceToPin = strokesPerHole.distances[i].distanceToPin,
                    ground = strokesPerHole.distances[i].ground
                )
            )
        }
        return sgs
    }

    private fun calcSg(
        stroke: StrokeFFS,
        nextStroke: StrokeFFS
    ): Double {
        val sgStroke = strokesGainedFromBenchmark(
            stroke.distanceToPin,
            stroke.ground
        )

        val sgNextStroke = strokesGainedFromBenchmark(
            nextStroke.distanceToPin,
            nextStroke.ground
        )

        return sgStroke - sgNextStroke - 1 - if (stroke.leadToPenalty) 1 else 0
    }

    private fun strokesGainedFromBenchmark(
        denominatedValue: DenominatedValue,
        ground: Ground
    ): Double {
        return benchmarkAndInterpolation.get(denominatedValue, ground)
    }
}