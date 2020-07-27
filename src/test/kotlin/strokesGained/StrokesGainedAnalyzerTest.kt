package strokesGained

import Benchmark
import CourseAndDate
import DenominatedValue
import Ground
import Round
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class StrokesGainedAnalyzerTest {
    private lateinit var benchmark: Benchmark

    @BeforeEach
    fun configure() {
        class BenchmarkTestImpl(
            val strokesGainedByDistanceByGround: Map<Ground, Map<DenominatedValue, Double>>
        ) : Benchmark {
            override fun get(denominatedValue: DenominatedValue, ground: Ground): Double {
                return strokesGainedByDistanceByGround[ground]!!.get(denominatedValue)!!
            }
        }

        benchmark = BenchmarkTestImpl(
            mapOf(
                Ground.TEE to mapOf(
                    DenominatedValue(450.0, DistanceUnit.METERS) to 5.5,
                    DenominatedValue(400.0, DistanceUnit.METERS) to 5.0
                ),
                Ground.FAIRWAY to mapOf(
                    DenominatedValue(200.0, DistanceUnit.METERS) to 3.0,
                    DenominatedValue(100.0, DistanceUnit.METERS) to 2.0,
                    DenominatedValue(50.0, DistanceUnit.METERS) to 1.8
                ),
                Ground.GREEN to mapOf(
                    DenominatedValue(10.0, DistanceUnit.METERS) to 1.5,
                    DenominatedValue(1.0, DistanceUnit.METERS) to 1.0
                )
            )
        )
    }

    private fun getRound(): Round {
        return Round(
            CourseAndDate(
                "Täby GK",
                Date(2020, 2, 2)
            ),
            listOf(
                StrokesForHole(
                    1,
                    listOf(
                        Stroke(
                            Ground.TEE,
                            DenominatedValue(450.0, DistanceUnit.METERS),
                            false
                        ),
                        Stroke(
                            Ground.FAIRWAY,
                            DenominatedValue(200.0, DistanceUnit.METERS),
                            false
                        ),
                        Stroke(
                            Ground.FAIRWAY,
                            DenominatedValue(50.0, DistanceUnit.METERS),
                            false
                        ),
                        Stroke(
                            Ground.GREEN,
                            DenominatedValue(10.0, DistanceUnit.METERS),
                            false
                        ),
                        Stroke(
                            Ground.GREEN,
                            DenominatedValue(1.0, DistanceUnit.METERS),
                            false
                        )
                    ),
                    5,
                    2
                )
            )
        )
    }

    private fun assertStrokesGained(
        strokesGained: StrokesGained
    ) {
        assertThat(strokesGained.total()).isEqualTo(0.5, within(0.001))
        assertThat(strokesGained.tee()).isEqualTo(1.5, within(0.001))
        assertThat(strokesGained.fairway()).isEqualTo(-0.5, within(0.001))
        assertThat(strokesGained.fairway(
            DenominatedValue(180.0, DistanceUnit.METERS),
            DenominatedValue(210.0, DistanceUnit.METERS)))
            .isEqualTo(0.2, within(0.001))
        assertThat(strokesGained.fairway(
            DenominatedValue(40.0, DistanceUnit.METERS),
            DenominatedValue(60.0, DistanceUnit.METERS)))
            .isEqualTo(-0.7, within(0.001))
        assertThat(strokesGained.green()).isEqualTo(-0.5, within(0.001))
        assertThat(strokesGained.green(
            DenominatedValue(9.0, DistanceUnit.METERS),
            DenominatedValue(11.0, DistanceUnit.METERS)
        )).isEqualTo(-0.5, within(0.001))
        assertThat(strokesGained.green(
            DenominatedValue(0.8, DistanceUnit.METERS),
            DenominatedValue(1.2, DistanceUnit.METERS)
        )).isEqualTo(0.0, within(0.001))

    }

    @Test
    fun `test analyzing one round with one hole played`() {
        val strokesGainedAnalyzer = StrokesGainedAnalyzer(benchmark)
        val strokesGained = strokesGainedAnalyzer.analyze(getRound())
        assertStrokesGained(strokesGained)
    }

    @Test
    fun `test analyzing one round with two holes played`() {

    }

    @Test
    fun `test analyze one round a list of one round is identical`() {
        val strokesGainedAnalyzer = StrokesGainedAnalyzer(benchmark)
        val strokesGained = strokesGainedAnalyzer.analyze(listOf(getRound()))
        assertStrokesGained(strokesGained)
    }

    @Test
    fun `test analyzing multiple rounds`() {

    }

    @Test
    fun `test lead to penalty works`() {

    }

    @Test
    fun `test hio`() {
        val strokesGained = StrokesGainedAnalyzer(benchmark).analyze(
            Round(
                CourseAndDate(
                    "Täby GK",
                    Date(2020, 2, 2)
                ),
                listOf(
                    StrokesForHole(
                        1,
                        listOf(
                            Stroke(
                                Ground.TEE,
                                DenominatedValue(400.0, DistanceUnit.METERS),
                                false
                            )
                        ),
                        1,
                        0
                    )
                )
            )
        )

        assertThat(strokesGained.total()).isEqualTo(4.0)
        assertThat(strokesGained.tee()).isEqualTo(4.0)
    }

    @Test
    fun `test interpolation`() {
        // TODO: Should test belong to this class?
    }
}