import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test
import java.util.*

class DummyTest {
    val benchmarkTemp = StrokesGainedPgaTourBenchmarkRepository(
        strokesByDistanceTee = mapOf(
            DenominatedValue(91.0, DistanceUnit.METERS) to 2.92,
            DenominatedValue(110.0, DistanceUnit.METERS) to 2.99,  // TODO: Use DenominatedValue (always have a distance connected to the unit across the domain)
            DenominatedValue(128.0, DistanceUnit.METERS) to 2.97,
            DenominatedValue(146.0, DistanceUnit.METERS) to 2.99,
            DenominatedValue(165.0, DistanceUnit.METERS) to 3.05,
            DenominatedValue(183.0, DistanceUnit.METERS) to 3.12,
            DenominatedValue(201.0, DistanceUnit.METERS) to 3.17,
            DenominatedValue(219.0, DistanceUnit.METERS) to 3.25,
            DenominatedValue(238.0, DistanceUnit.METERS) to 3.45,
            DenominatedValue(256.0, DistanceUnit.METERS) to 3.65,
            DenominatedValue(274.0, DistanceUnit.METERS) to 3.71,
            DenominatedValue(293.0, DistanceUnit.METERS) to 3.79,
            DenominatedValue(311.0, DistanceUnit.METERS) to 3.86,
            DenominatedValue(329.0, DistanceUnit.METERS) to 3.92,
            DenominatedValue(347.0, DistanceUnit.METERS) to 3.96,
            DenominatedValue(366.0, DistanceUnit.METERS) to 3.99,
            DenominatedValue(384.0, DistanceUnit.METERS) to 4.02,
            DenominatedValue(402.0, DistanceUnit.METERS) to 4.08,
            DenominatedValue(421.0, DistanceUnit.METERS) to 4.17,
            DenominatedValue(439.0, DistanceUnit.METERS) to 4.28,
            DenominatedValue(457.0, DistanceUnit.METERS) to 4.41,
            DenominatedValue(475.0, DistanceUnit.METERS) to 4.54,
            DenominatedValue(494.0, DistanceUnit.METERS) to 4.65,
            DenominatedValue(512.0, DistanceUnit.METERS) to 4.74,
            DenominatedValue(530.0, DistanceUnit.METERS) to 4.79,
            DenominatedValue(549.0, DistanceUnit.METERS) to 4.82
        ),

        // TODO: Who's responsible of interpolating??
        strokesByDistanceFairway = mapOf(
            91.0 to 2.92,
            110.0 to 2.99,  // TODO: Use DenominatedValue (always have a distance connected to the unit across the domain)
            128.0 to 2.97,
            146.0 to 2.99,
            165.0 to 3.05,
            183.0 to 3.12,
            201.0 to 3.17,
            219.0 to 3.25,
            238.0 to 3.45,
            256.0 to 3.65,
            274.0 to 3.71,
            293.0 to 3.79,
            311.0 to 3.86,
            329.0 to 3.92,
            347.0 to 3.96,
            366.0 to 3.99,
            384.0 to 4.02,
            402.0 to 4.08,
            421.0 to 4.17,
            439.0 to 4.28,
            457.0 to 4.41,
            475.0 to 4.54,
            494.0 to 4.65,
            512.0 to 4.74,
            530.0 to 4.79,
            549.0 to 4.82
        ),
        strokesByDistanceRough = mapOf(
            91.0 to 2.92,
            110.0 to 2.99,  // TODO: Use DenominatedValue (always have a distance connected to the unit across the domain)
            128.0 to 2.97,
            146.0 to 2.99,
            165.0 to 3.05,
            183.0 to 3.12,
            201.0 to 3.17,
            219.0 to 3.25,
            238.0 to 3.45,
            256.0 to 3.65,
            274.0 to 3.71,
            293.0 to 3.79,
            311.0 to 3.86,
            329.0 to 3.92,
            347.0 to 3.96,
            366.0 to 3.99,
            384.0 to 4.02,
            402.0 to 4.08,
            421.0 to 4.17,
            439.0 to 4.28,
            457.0 to 4.41,
            475.0 to 4.54,
            494.0 to 4.65,
            512.0 to 4.74,
            530.0 to 4.79,
            549.0 to 4.82
        ),
        strokesByDistanceRecovery = mapOf(
            91.0 to 2.92,
            110.0 to 2.99,  // TODO: Use DenominatedValue (always have a distance connected to the unit across the domain)
            128.0 to 2.97,
            146.0 to 2.99,
            165.0 to 3.05,
            183.0 to 3.12,
            201.0 to 3.17,
            219.0 to 3.25,
            238.0 to 3.45,
            256.0 to 3.65,
            274.0 to 3.71,
            293.0 to 3.79,
            311.0 to 3.86,
            329.0 to 3.92,
            347.0 to 3.96,
            366.0 to 3.99,
            384.0 to 4.02,
            402.0 to 4.08,
            421.0 to 4.17,
            439.0 to 4.28,
            457.0 to 4.41,
            475.0 to 4.54,
            494.0 to 4.65,
            512.0 to 4.74,
            530.0 to 4.79,
            549.0 to 4.82
        ),
        strokesByDistancePutt = mapOf(
            1.0 to 2.92,
            2.0 to 2.99,
            3.0 to 1.69,
            4.0 to 2.99,
            20.0 to 3.05,
            30.0 to 3.12
        )
    )

    @Test
    fun `test converting to sg old`() {
//        val hole = HoleEntry(
//            listOf(
//                DistanceToPin("431T"),
//                DistanceToPin("210R"),
//                DistanceToPin("39F"),
//                DistanceToPin("8G"),
//                DistanceToPin("0.5G")
//            ),
//            1,
//            5,
//            2
//        )

//        val roundEntry = RoundEntry(
//            CourseAndDate(
//                "Täby GK",
//                Date(2020, 6, 18)
//            ),
//            listOf(hole),
//            DistanceUnit.METERS
//        )
//
//        class GroundMapperImpl : GroundMapper {
//            val GroundByCharacter = mapOf(
//                "T" to Ground.TEE,
//                "F" to Ground.FAIRWAY,
//                "R" to Ground.ROUGH,
//                "G" to Ground.GREEN
//            )
//
//            override fun ground(text: String): Ground {
//                return GroundByCharacter[text] ?: throw IllegalArgumentException("Can't interpret $text")
//            }
//
//            override fun penaltyCharacter(): String {
//                return "P"
//            }
//        }
//
//        val converter = Converter(GroundMapperImpl())
    }

    @Test
    fun `test converting to sg`() {
        val benchmark = StrokesGainedPgaTourBenchmarkRepository(
            strokesByDistanceTee = mapOf(
                DenominatedValue(128.0, DistanceUnit.METERS) to 3.0
            ),

            // TODO: Who's responsible of interpolating??
            strokesByDistanceFairway = emptyMap(),
            strokesByDistanceRough = emptyMap(),
            strokesByDistanceRecovery = emptyMap(),
            strokesByDistancePutt = mapOf(
                3.0 to 1.7
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

//        val strokesGained = StrokesGainedAnalyzer(benchmark).analyze(round)
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