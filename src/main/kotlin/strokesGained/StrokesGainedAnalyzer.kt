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

        val strokes = strokesPerHole.distances + IN_THE_HOLE

        for (i in strokes.indices.take(strokes.size - 1)) {
            // Starting on the first, iterating up until the second to last

            val sg = calcSg(
                strokes[i],
                strokes[i + 1]
            )

            sgs.add(
                StrokesGainedData(
                    strokesGained = sg,
                    distanceToPin = strokes[i].distanceToPin,
                    ground = strokes[i].ground
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

        val sgNextStroke = if (nextStroke.distanceToPin.distance == 0.0)
            0.0
        else strokesGainedFromBenchmark(
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

    companion object {
        private val IN_THE_HOLE = Stroke(
            Ground.GREEN,
            DenominatedValue(0.0, DistanceUnit.METERS),
            false
        )
    }
}