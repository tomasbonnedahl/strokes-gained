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

        assertThat(strokesGained.total()).isEqualTo(0.3, within(0.0001))
        assertThat(strokesGained.tee()).isEqualTo(0.3, within(0.0001))
    }

    @Test
    fun `test convert to sg with fairway brackets and interpolation`() {
        val benchmark = StrokesGainedRepoTestImpl(
            strokesByDistanceByGround = mapOf(
                Ground.TEE to mapOf(
                    DenominatedValue(100.0, DistanceUnit.METERS) to 2.7,
                    DenominatedValue(400.0, DistanceUnit.METERS) to 3.7
                ),
                Ground.FAIRWAY to mapOf(
                    DenominatedValue(100.0, DistanceUnit.METERS) to 2.6,
                    DenominatedValue(300.0, DistanceUnit.METERS) to 3.3
                ),
                Ground.GREEN to mapOf(
                    DenominatedValue(0.1, DistanceUnit.METERS) to 1.0,
                    DenominatedValue(3.0, DistanceUnit.METERS) to 1.3,
                    DenominatedValue(10.0, DistanceUnit.METERS) to 1.8
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
                    2,
                    listOf(
                        StrokeFFS(
                            Ground.TEE,
                            DenominatedValue(360.0, DistanceUnit.METERS),
                            false
                        ),
                        StrokeFFS(
                            Ground.FAIRWAY,
                            DenominatedValue(140.0, DistanceUnit.METERS),
                            false
                        ),
                        StrokeFFS(
                            Ground.GREEN,
                            DenominatedValue(8.0, DistanceUnit.METERS),
                            false
                        ),
                        StrokeFFS(
                            Ground.GREEN,
                            DenominatedValue(0.5, DistanceUnit.METERS),
                            false
                        )
                    ),
                    4,
                    2
                )
            )
        )

        val strokesGained = StrokesGainedAnalyzer(
            BenchmarkAndInterpolation(benchmark)
        ).analyze(round)

        assertThat(strokesGained.total()).isEqualTo(-0.433, within(0.0001))
        assertThat(strokesGained.tee()).isEqualTo(-0.173, within(0.0001))
        assertThat(strokesGained.fairway()).isEqualTo(0.083, within(0.00001))
        assertThat(
            strokesGained.fairway(
                DenominatedValue(125.0, DistanceUnit.METERS),
                DenominatedValue(175.0, DistanceUnit.METERS)
            )
        ).isEqualTo(0.083, within(0.00001))  // TODO: epsilon

        // TODO: A no data test, should throw exception?
//        assertThat(
//            strokesGained.fairway(
//                DenominatedValue(125.0, DistanceUnit.METERS),
//                DenominatedValue(135.0, DistanceUnit.METERS)
//            )
//        ).isEqualTo(0.083, within(0.00001))  // TODO: epsilon
        assertThat(strokesGained.green()).isEqualTo(-0.343, within(0.00001))
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