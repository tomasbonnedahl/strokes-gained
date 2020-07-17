package strokesGained

import DenominatedValue
import DistanceUnit
import Ground
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class StrokesGainedTest {
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

        Assertions.assertThat(strokesGained.total()).isEqualTo(-0.57)
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

        Assertions.assertThat(strokesGained.total()).isEqualTo(-0.3, Assertions.within(0.00001))
    }

    @Test
    fun `test all methods`() {
        val strokesGained = StrokesGained(
            listOf(
                StrokesGainedData(
                    -0.1,
                    DenominatedValue(350.0, DistanceUnit.METERS),
                    Ground.TEE
                ),
                StrokesGainedData(
                    -0.2,
                    DenominatedValue(150.0, DistanceUnit.METERS),
                    Ground.FAIRWAY
                ),
                StrokesGainedData(
                    0.4,
                    DenominatedValue(8.0, DistanceUnit.METERS),
                    Ground.GREEN
                ),
                StrokesGainedData(
                    0.1,
                    DenominatedValue(0.1, DistanceUnit.METERS),
                    Ground.GREEN
                )
            )
        )

        Assertions.assertThat(strokesGained.total()).isEqualTo(0.2, Assertions.within(0.00001))

        Assertions.assertThat(strokesGained.tee()).isEqualTo(-0.1, Assertions.within(0.00001))

        Assertions.assertThat(strokesGained.fairway()).isEqualTo(-0.2, Assertions.within(0.00001))
        Assertions.assertThat(
            strokesGained.fairway(
                DenominatedValue(140.0, DistanceUnit.METERS),
                DenominatedValue(160.0, DistanceUnit.METERS)
            )
        ).isEqualTo(-0.2, Assertions.within(0.00001))

        Assertions.assertThat(strokesGained.green()).isEqualTo(0.5, Assertions.within(0.00001))
        Assertions.assertThat(
            strokesGained.green(
                DenominatedValue(7.0, DistanceUnit.METERS),
                DenominatedValue(9.0, DistanceUnit.METERS)
            )
        ).isEqualTo(0.4, Assertions.within(0.00001))
    }

    @Test
    fun `test strokes gained with missing data`() {
        val strokesGained = StrokesGained(emptyList())

        Assertions.assertThat(strokesGained.total()).isEqualTo(0.0)
        Assertions.assertThat(strokesGained.tee()).isEqualTo(0.0)
        Assertions.assertThat(strokesGained.fairway()).isEqualTo(0.0)
        Assertions.assertThat(
            strokesGained.fairway(
                DenominatedValue(140.0, DistanceUnit.METERS),
                DenominatedValue(160.0, DistanceUnit.METERS)
            )
        ).isEqualTo(0.0)
        Assertions.assertThat(strokesGained.green()).isEqualTo(0.0)
        Assertions.assertThat(
            strokesGained.green(
                DenominatedValue(7.0, DistanceUnit.METERS),
                DenominatedValue(9.0, DistanceUnit.METERS)
            )
        ).isEqualTo(0.0)
    }
}