import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test

class BenchmarkAndInterpolationTest {
    @Test
    fun `test simple interpolation`() {
        val benchmark = StrokesGainedPgaTourBenchmarkRepository(
            strokesByDistanceTee = mapOf(
                DenominatedValue(130.0, DistanceUnit.METERS) to 3.0,
                DenominatedValue(140.0, DistanceUnit.METERS) to 3.6
            ),
            strokesByDistanceFairway = emptyMap(),
            strokesByDistanceRough = emptyMap(),
            strokesByDistanceRecovery = emptyMap(),
            strokesByDistancePutt = emptyMap()
        )

        val subject = BenchmarkAndInterpolation(benchmark)

        mapOf(
            130.0 to 3.0,
            135.0 to 3.3,
            140.0 to 3.6
        ).forEach { (distance, expectedStrokesGained) ->
            assertThat(
                subject.get(
                    DenominatedValue(distance, DistanceUnit.METERS),
                    Ground.TEE
                )
            ).isEqualTo(expectedStrokesGained, within(0.000001))
        }
    }

    @Test
    fun `test interpolation with more than two data points`() {
        val benchmark = StrokesGainedPgaTourBenchmarkRepository(
            strokesByDistanceTee = mapOf(
                DenominatedValue(100.0, DistanceUnit.METERS) to 2.0,
                DenominatedValue(110.0, DistanceUnit.METERS) to 3.0,
                DenominatedValue(120.0, DistanceUnit.METERS) to 5.0
            ),
            strokesByDistanceFairway = emptyMap(),
            strokesByDistanceRough = emptyMap(),
            strokesByDistanceRecovery = emptyMap(),
            strokesByDistancePutt = emptyMap()
        )

        val subject = BenchmarkAndInterpolation(benchmark)

        mapOf(
            100.0 to 2.0,
            110.0 to 3.0,
            120.0 to 5.0
        ).forEach { (distance, expectedStrokesGained) ->
            assertThat(
                subject.get(
                    DenominatedValue(distance, DistanceUnit.METERS),
                    Ground.TEE
                )
            ).isEqualTo(expectedStrokesGained, within(0.000001))
        }

    }
}