class StrokesGainedAnalyzer(
//    val strokesGainedBenchmarkRepository: StrokesGainedBenchmarkRepository
//    val strokesGainedPgaTourBenchmarkRepository: StrokesGainedPgaTourBenchmarkRepository
//    val strokesGainedBenchmarkRepository: StrokesGainedBenchmarkRepository
    val benchmarkAndInterpolation: BenchmarkAndInterpolation  // TODO: interface
) {
    fun analyze(
        round: Round
    ): StrokesGained {
        val result = round.strokesPerHole.map { it ->
            calculateStrokesGained(it)
        }

        result.forEach {
            it.forEach { it2 ->
                println("it2 = ${it2}")
            }
        }

        return StrokesGained(
            result.flatten()
        )
    }

    fun analyze(
        rounds: List<Round>
    ): StrokesGained {
//        val asf = rounds.map(this::analyze).flatMap { it ->  }
        TODO("ASF")
    }

    private fun calculateStrokesGained(strokesPerHole: StrokesForHole): List<StrokesGainedData> {
//        distancesToPin.forEach { println("it = ${it}") }
        strokesPerHole.distances.forEach { println("it = ${it}") }

        val sgs = mutableListOf<StrokesGainedData>()

        for (i in strokesPerHole.distances.indices - 1) {
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
//        return strokesGainedBenchmarkRepository.get(denominatedValue, ground)
        return benchmarkAndInterpolation.get(denominatedValue, ground)
    }
}