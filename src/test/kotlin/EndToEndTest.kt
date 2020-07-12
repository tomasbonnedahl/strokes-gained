import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test
import strokesGained.*
import java.util.*

class EndToEndTest {
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

        // TODO: A no data test. should throw exception?
//        assertThat(
//            strokesGained.fairway(
//                DenominatedValue(125.0. DistanceUnit.METERS).
//                DenominatedValue(135.0. DistanceUnit.METERS)
//            )
//        ).isEqualTo(0.083. within(0.00001))  // TODO: epsilon
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

    @Test
    fun `test end to end`() {
        val converter = Converter.of(
            CommonGroundMapper.create()
        )

        val holeEntries = listOf(
//            "431T". "210F". "39F". "5G". "0.2G"
            "431T", "210F", "39F", "5G", "1G"
        ).map {
            DistanceToPin(it)
        }.map {
            converter.fromEntryToStroke(it)
        }

        val round = Round(
            CourseAndDate(
                "Täby GK",
                Date(2020, 6, 18)
            ),
            listOf(
                StrokesForHole(
                    1,  // TODO: Get from enumeration
                    holeEntries,
                    5,  // TODO: Calculate as part of the input? Remove from here otherwise?
                    2
                )
            )
        )

        val benchmark = StrokesGainedRepoTestImpl(
            mapOf(
                Ground.TEE to mapOf(
                    DenominatedValue(91.0, DistanceUnit.METERS) to 2.92,
                    DenominatedValue(110.0, DistanceUnit.METERS) to 2.99,
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
                Ground.FAIRWAY to mapOf(
                    DenominatedValue(9.0, DistanceUnit.METERS) to 2.18,
                    DenominatedValue(18.0, DistanceUnit.METERS) to 2.4,
                    DenominatedValue(27.0, DistanceUnit.METERS) to 2.52,
                    DenominatedValue(37.0, DistanceUnit.METERS) to 2.6,
                    DenominatedValue(46.0, DistanceUnit.METERS) to 2.66,
                    DenominatedValue(55.0, DistanceUnit.METERS) to 2.7,
                    DenominatedValue(64.0, DistanceUnit.METERS) to 2.72,
                    DenominatedValue(73.0, DistanceUnit.METERS) to 2.75,
                    DenominatedValue(82.0, DistanceUnit.METERS) to 2.77,
                    DenominatedValue(91.0, DistanceUnit.METERS) to 2.8,
                    DenominatedValue(110.0, DistanceUnit.METERS) to 2.85,
                    DenominatedValue(128.0, DistanceUnit.METERS) to 2.91,
                    DenominatedValue(146.0, DistanceUnit.METERS) to 2.98,
                    DenominatedValue(165.0, DistanceUnit.METERS) to 3.08,
                    DenominatedValue(183.0, DistanceUnit.METERS) to 3.19,
                    DenominatedValue(201.0, DistanceUnit.METERS) to 3.32,
                    DenominatedValue(219.0, DistanceUnit.METERS) to 3.45,
                    DenominatedValue(238.0, DistanceUnit.METERS) to 3.58,
                    DenominatedValue(256.0, DistanceUnit.METERS) to 3.69,
                    DenominatedValue(274.0, DistanceUnit.METERS) to 3.78,
                    DenominatedValue(293.0, DistanceUnit.METERS) to 3.84,
                    DenominatedValue(311.0, DistanceUnit.METERS) to 3.88,
                    DenominatedValue(329.0, DistanceUnit.METERS) to 3.95,
                    DenominatedValue(347.0, DistanceUnit.METERS) to 4.03,
                    DenominatedValue(366.0, DistanceUnit.METERS) to 4.11,
                    DenominatedValue(384.0, DistanceUnit.METERS) to 4.19,
                    DenominatedValue(402.0, DistanceUnit.METERS) to 4.27,
                    DenominatedValue(421.0, DistanceUnit.METERS) to 4.34,
                    DenominatedValue(439.0, DistanceUnit.METERS) to 4.42,
                    DenominatedValue(457.0, DistanceUnit.METERS) to 4.5,
                    DenominatedValue(475.0, DistanceUnit.METERS) to 4.58,
                    DenominatedValue(494.0, DistanceUnit.METERS) to 4.66,
                    DenominatedValue(512.0, DistanceUnit.METERS) to 4.74,
                    DenominatedValue(530.0, DistanceUnit.METERS) to 4.82,
                    DenominatedValue(549.0, DistanceUnit.METERS) to 4.89
                ),
                Ground.ROUGH to mapOf(
                    DenominatedValue(9.0, DistanceUnit.METERS) to 2.34,
                    DenominatedValue(18.0, DistanceUnit.METERS) to 2.59,
                    DenominatedValue(27.0, DistanceUnit.METERS) to 2.7,
                    DenominatedValue(37.0, DistanceUnit.METERS) to 2.78,
                    DenominatedValue(46.0, DistanceUnit.METERS) to 2.87,
                    DenominatedValue(55.0, DistanceUnit.METERS) to 2.91,
                    DenominatedValue(64.0, DistanceUnit.METERS) to 2.93,
                    DenominatedValue(73.0, DistanceUnit.METERS) to 2.96,
                    DenominatedValue(82.0, DistanceUnit.METERS) to 2.99,
                    DenominatedValue(91.0, DistanceUnit.METERS) to 3.02,
                    DenominatedValue(110.0, DistanceUnit.METERS) to 3.08,
                    DenominatedValue(128.0, DistanceUnit.METERS) to 3.15,
                    DenominatedValue(146.0, DistanceUnit.METERS) to 3.23,
                    DenominatedValue(165.0, DistanceUnit.METERS) to 3.31,
                    DenominatedValue(183.0, DistanceUnit.METERS) to 3.42,
                    DenominatedValue(201.0, DistanceUnit.METERS) to 3.53,
                    DenominatedValue(219.0, DistanceUnit.METERS) to 3.64,
                    DenominatedValue(238.0, DistanceUnit.METERS) to 3.74,
                    DenominatedValue(256.0, DistanceUnit.METERS) to 3.83,
                    DenominatedValue(274.0, DistanceUnit.METERS) to 3.9,
                    DenominatedValue(293.0, DistanceUnit.METERS) to 3.95,
                    DenominatedValue(311.0, DistanceUnit.METERS) to 4.02,
                    DenominatedValue(329.0, DistanceUnit.METERS) to 4.11,
                    DenominatedValue(347.0, DistanceUnit.METERS) to 4.21,
                    DenominatedValue(366.0, DistanceUnit.METERS) to 4.3,
                    DenominatedValue(384.0, DistanceUnit.METERS) to 4.4,
                    DenominatedValue(402.0, DistanceUnit.METERS) to 4.49,
                    DenominatedValue(421.0, DistanceUnit.METERS) to 4.58,
                    DenominatedValue(439.0, DistanceUnit.METERS) to 4.68,
                    DenominatedValue(457.0, DistanceUnit.METERS) to 4.77,
                    DenominatedValue(475.0, DistanceUnit.METERS) to 4.87,
                    DenominatedValue(494.0, DistanceUnit.METERS) to 4.96,
                    DenominatedValue(512.0, DistanceUnit.METERS) to 5.06,
                    DenominatedValue(530.0, DistanceUnit.METERS) to 5.15,
                    DenominatedValue(549.0, DistanceUnit.METERS) to 5.25
                ),
                Ground.GREEN to mapOf(
                    DenominatedValue(0.1, DistanceUnit.METERS) to 1.0,
                    DenominatedValue(1.0, DistanceUnit.METERS) to 1.1,
                    DenominatedValue(2.0, DistanceUnit.METERS) to 1.5,
                    DenominatedValue(3.0, DistanceUnit.METERS) to 1.9,
                    DenominatedValue(10.0, DistanceUnit.METERS) to 2.0
                )
            )
        )

        val strokesGained = StrokesGainedAnalyzer(
            BenchmarkAndInterpolation(benchmark)
        ).analyze(round)

        // Lock-in values, verified outside the application
        assertThat(strokesGained.total()).isEqualTo(-0.869)
        assertThat(strokesGained.tee()).isEqualTo(-0.154)
        assertThat(strokesGained.fairway()).isEqualTo(-0.544)
        assertThat(strokesGained.green()).isEqualTo(-0.171)
    }

    @Test
    fun `test taking end of string`() {
        val s = "0.4G"
        val asfsa = s.partition { c -> c.isDigit() }

        if (s.contains(".")) { // TODO: Support ,?
            val x = s.split(".")
            println("x = ${x}")

            val y = x[1].partition { c -> c.isDigit() }
            println("y = ${y}")

            val z = x[0] + "." + y.first
            println("z = ${z}")
            val zz = z.toDouble()
        }

        println("asfsa = ${asfsa}")

    }
}