import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test
import java.util.*

class DummyTest {
    class StrokesGainedRepoTestImpl(
        val strokesByDistanceByGround: Map<Ground, Map<DenominatedValue, Double>>
    ): StrokesGainedBenchmarkRepository {
        override fun get(denominatedValue: DenominatedValue, ground: Ground): Double {
            return strokesByDistanceByGround[ground]!![denominatedValue]!!
        }

        override fun getAll(ground: Ground): Map<DenominatedValue, Double> {
            return strokesByDistanceByGround[ground]!!
        }

        override fun getGrounds(): Set<Ground> {
            return strokesByDistanceByGround.keys
        }
    }

    @Test
    fun `test converting to sg`() {
        val benchmark = StrokesGainedRepoTestImpl(
            strokesByDistanceByGround = mapOf(
                Ground.TEE to mapOf(
                    DenominatedValue(128.0, DistanceUnit.METERS) to 3.0
                ),
                Ground.GREEN to mapOf(
                    DenominatedValue(3.0, DistanceUnit.METERS) to 1.7
                )
            )
        )

        val round = Round(
            CourseAndDate(
                "Täby GK",
                Date(2020, 6, 18)
            ),
            listOf(
                StrokesForHole(
                    1,
                    listOf(
                        StrokeFFS(
                            Ground.TEE,
                            DenominatedValue(128.0, DistanceUnit.METERS),
                            false
                        ),
                        StrokeFFS(
                            Ground.GREEN,
                            DenominatedValue(3.0, DistanceUnit.METERS),
                            false
                        )
                    ),
                    2,
                    1
                )
            )
        )

        val strokesGained = StrokesGainedAnalyzer(
            BenchmarkAndInterpolation(benchmark)
        ).analyze(round)

        // TODO: Should be able to configure the brackets to use, i.e. 'total', 'tee', 'Fairway 125-150', etc.
        assertThat(strokesGained.total()).isEqualTo(0.3, within(0.0001))
        assertThat(strokesGained.tee()).isEqualTo(0.3, within(0.0001))
//        assertThat(strokesGained.fairway()).isEqualTo(-2.0)
//        assertThat(
//            strokesGained.fairway(
//                DenominatedValue(125.0, DistanceUnit.METERS),
//                DenominatedValue(175.0, DistanceUnit.METERS)
//            )
//        ).isEqualTo(-2.0)
    }

    @Test
    fun `test simple`() {
        val strokesGained = StrokesGained(
            listOf(
                StrokesGainedData(
                    -0.57,
                    DenominatedValue(150.0, DistanceUnit.METERS),
                    Ground.FAIRWAY
                )
            )
        )

        assertThat(strokesGained.total()).isEqualTo(-0.57)
    }

    @Test
    fun `test total`() {
        val strokesGained = StrokesGained(
            listOf(
                StrokesGainedData(
                    -0.1,
                    DenominatedValue(150.0, DistanceUnit.METERS),
                    Ground.TEE
                ),
                StrokesGainedData(
                    -0.2,
                    DenominatedValue(250.0, DistanceUnit.METERS),
                    Ground.FAIRWAY
                )
            )
        )

        assertThat(strokesGained.total()).isEqualTo(-0.3, within(0.00001))
    }


}