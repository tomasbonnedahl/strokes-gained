package strokesGained

import Benchmark
import DenominatedValue
import Ground
import Round

class StrokesGainedAnalyzer(
    private val benchmark: Benchmark
) {
    fun analyze(
        round: Round
    ): StrokesGained {
        // TODO: When analyze(rounds) is implemented, call that with analyze(listOf(round))
        return StrokesGained(
            round.strokesPerHole.map { strokesForHole ->
                calculateStrokesGained(strokesForHole)
            }.flatten()
        )
    }

    fun analyze(
        rounds: List<Round>
    ): StrokesGained {
        return StrokesGained(
            rounds.flatMap { round ->
                round.strokesPerHole.flatMap { strokesForHole ->
                    calculateStrokesGained(strokesForHole)
                }
            }
        )
    }

    private fun calculateStrokesGained(
        strokesPerHole: StrokesForHole
    ): List<StrokesGainedData> {
        val sgs = mutableListOf<StrokesGainedData>()

        for (i in strokesPerHole.distances.indices.take(strokesPerHole.distances.size - 1)) {
            // Starting on the first, iterating up until the second to last

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
        stroke: Stroke,
        nextStroke: Stroke
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
        return benchmark.get(denominatedValue, ground)
    }
}