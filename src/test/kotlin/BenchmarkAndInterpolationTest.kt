import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.should
import io.kotest.matchers.string.startWith
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test

class BenchmarkAndInterpolationTest {
    @Test
    fun `test simple interpolation`() {
        val benchmark = DummyTest.StrokesGainedRepoTestImpl(  // TODO: Put this somewhere else
            strokesByDistanceByGround = mapOf(
                Ground.TEE to mapOf(
                    DenominatedValue(130.0, DistanceUnit.METERS) to 3.0,
                    DenominatedValue(140.0, DistanceUnit.METERS) to 3.6
                )
            )
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
        val benchmark = DummyTest.StrokesGainedRepoTestImpl(
            strokesByDistanceByGround = mapOf(
                Ground.TEE to mapOf(
                    DenominatedValue(100.0, DistanceUnit.METERS) to 2.0,
                    DenominatedValue(110.0, DistanceUnit.METERS) to 3.0,
                    DenominatedValue(120.0, DistanceUnit.METERS) to 5.0
                )
            )
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

    @Test
    fun `test getting distance not supported, expect exception`() {
        val benchmark = DummyTest.StrokesGainedRepoTestImpl(
            strokesByDistanceByGround = mapOf(
                Ground.TEE to mapOf(
                    DenominatedValue(100.0, DistanceUnit.METERS) to 2.0,
                    DenominatedValue(110.0, DistanceUnit.METERS) to 3.0
                )
            )

        )

        val subject = BenchmarkAndInterpolation(benchmark)

        val exception = shouldThrow<IllegalArgumentException> {
            subject.get(DenominatedValue(200.0, DistanceUnit.METERS), Ground.TEE)
        }

        exception.message should startWith("Distance 200 not available")
    }

    @Test
    fun `test getting non-supported ground, expect exception`() {
        class StrokesGainedBenchmarkRepoTestImpl: StrokesGainedBenchmarkRepository {
            override fun get(denominatedValue: DenominatedValue, ground: Ground): Double {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getAll(ground: Ground): Map<DenominatedValue, Double> {
                return mapOf(
                    DenominatedValue(100.0, DistanceUnit.METERS) to 1.23
                )
            }

            override fun getGrounds(): Set<Ground> {
                return setOf(Ground.FAIRWAY)
            }
        }

        val subject = BenchmarkAndInterpolation(StrokesGainedBenchmarkRepoTestImpl())

        val exception = shouldThrow<IllegalArgumentException> {
            subject.get(
                DenominatedValue(100.0, DistanceUnit.METERS),
                Ground.TEE
            )
        }

        exception.message should startWith("Ground TEE not configured")
    }
}