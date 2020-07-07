import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ConverterTest {
    private lateinit var converter: Converter

    @BeforeEach
    fun configureSystemUnderTest() {
        class GroundMapperImpl : GroundMapper {
            val apas = mapOf(
                "T" to Ground.TEE,
                "F" to Ground.FAIRWAY,
                "R" to Ground.ROUGH,
                "G" to Ground.GREEN
            )

            override fun ground(text: String): Ground {
                return apas[text] ?: throw IllegalArgumentException("Can't interpret $text")
            }

            override fun penaltyCharacter(): String {
                return "P"
            }
        }
        converter = Converter(GroundMapperImpl())
    }

    @Test
    fun `test tee stroke from entry`() {
        val stroke = converter.fromEntryToStroke(Entry("210T"))
        assertThat(stroke.ground).isEqualTo(Ground.TEE)
        assertThat(stroke.distanceUnit).isEqualTo(DistanceUnit.METERS)
        assertThat(stroke.distanceToPin).isEqualTo(210.0)
        assertThat(stroke.leadToPenalty).isEqualTo(false)
    }


    @Test
    fun `test tee stroke from entry with penalty`() {
        val stroke = converter.fromEntryToStroke(Entry("210TP"))
        assertThat(stroke.ground).isEqualTo(Ground.TEE)
        assertThat(stroke.distanceUnit).isEqualTo(DistanceUnit.METERS)
        assertThat(stroke.distanceToPin).isEqualTo(210.0)
        assertThat(stroke.leadToPenalty).isEqualTo(true)
    }

    @Test
    fun `test putt stroke from entry`() {
        val stroke = converter.fromEntryToStroke(Entry("21G"))
        assertThat(stroke.ground).isEqualTo(Ground.GREEN)
        assertThat(stroke.distanceUnit).isEqualTo(DistanceUnit.METERS)
        assertThat(stroke.distanceToPin).isEqualTo(21.0)
        assertThat(stroke.leadToPenalty).isEqualTo(false)
    }

    @Test
    fun `test putt stroke from entry with penalty`() {
        val stroke = converter.fromEntryToStroke(Entry("21GP"))
        assertThat(stroke.ground).isEqualTo(Ground.GREEN)
        assertThat(stroke.distanceUnit).isEqualTo(DistanceUnit.METERS)
        assertThat(stroke.distanceToPin).isEqualTo(21.0)
        assertThat(stroke.leadToPenalty).isEqualTo(true)
    }
}