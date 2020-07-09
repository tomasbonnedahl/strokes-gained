class StrokesGainedAnalyzer(
    val strokesGainedBenchmarkService: StrokesGainedBenchmarkService
) {
    fun analyze(
        round: Round
    ): StrokesGained {
        val result = round.strokesPerHole.map { it ->
            calculateStrokesGained(it)
        }

        result.forEach { it ->
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
            val strokesGained1 = strokesGainedFromBenchmark(
                strokesPerHole.distances[i].distanceToPin,
                strokesPerHole.distances[i].ground
            )

            val strokesGained2 = strokesGainedFromBenchmark(
                strokesPerHole.distances[i + 1].distanceToPin,
                strokesPerHole.distances[i + 1].ground
            )

            val sg = strokesGained1 - strokesGained2 - 1 - if (strokesPerHole.distances[i].leadToPenalty) 1 else 0

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

    private fun strokesGainedFromBenchmark(
        denominatedValue: DenominatedValue,
        ground: Ground
    ): Double {
        return strokesGainedBenchmarkService.get(denominatedValue, ground)
    }
}