import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ConverterTest {
    private lateinit var converter: Converter

    @BeforeEach
    fun configureSystemUnderTest() {
        class GroundMapperImpl : GroundMapper {
            val GroundByCharacter = mapOf(
                "T" to Ground.TEE,
                "F" to Ground.FAIRWAY,
                "R" to Ground.ROUGH,
                "G" to Ground.GREEN,
                "Rec" to Ground.RECOVERY
                // TODO: Add TI (tap-in)
            )

            override fun ground(text: String): Ground {
                return GroundByCharacter[text] ?: throw IllegalArgumentException("Can't interpret $text")
            }

            override fun penaltyCharacter(): String {
                return "P"
            }
        }
        converter = Converter(GroundMapperImpl())
    }

    @Test
    fun `test tee stroke from entry`() {
        val stroke = converter.fromEntryToStroke(DistanceToPin("210T"))
        assertThat(stroke.ground).isEqualTo(Ground.TEE)
        assertThat(stroke.distanceToPin.unit).isEqualTo(DistanceUnit.METERS)
        assertThat(stroke.distanceToPin.distance).isEqualTo(210.0)
        assertThat(stroke.leadToPenalty).isEqualTo(false)
    }

    @Test
    fun `test tee stroke from entry with penalty`() {
        val stroke = converter.fromEntryToStroke(DistanceToPin("210TP"))
        assertThat(stroke.ground).isEqualTo(Ground.TEE)
        assertThat(stroke.distanceToPin.unit).isEqualTo(DistanceUnit.METERS)
        assertThat(stroke.distanceToPin.distance).isEqualTo(210.0)
        assertThat(stroke.leadToPenalty).isEqualTo(true)
    }

    @Test
    fun `test putt stroke from entry`() {
        val stroke = converter.fromEntryToStroke(DistanceToPin("21G"))
        assertThat(stroke.ground).isEqualTo(Ground.GREEN)
        assertThat(stroke.distanceToPin.unit).isEqualTo(DistanceUnit.METERS)
        assertThat(stroke.distanceToPin.distance).isEqualTo(21.0)
        assertThat(stroke.leadToPenalty).isEqualTo(false)
    }

    @Test
    fun `test putt stroke from entry with penalty`() {
        val stroke = converter.fromEntryToStroke(DistanceToPin("21GP"))
        assertThat(stroke.ground).isEqualTo(Ground.GREEN)
        assertThat(stroke.distanceToPin.unit).isEqualTo(DistanceUnit.METERS)
        assertThat(stroke.distanceToPin.distance).isEqualTo(21.0)
        assertThat(stroke.leadToPenalty).isEqualTo(true)
    }

    @Test
    fun `test recovery`() {
        val stroke = converter.fromEntryToStroke(DistanceToPin("210Rec"))
        assertThat(stroke.ground).isEqualTo(Ground.RECOVERY)
        assertThat(stroke.distanceToPin.unit).isEqualTo(DistanceUnit.METERS)
        assertThat(stroke.distanceToPin.distance).isEqualTo(210.0)
        assertThat(stroke.leadToPenalty).isEqualTo(false)
    }

    @Test
    fun `test recovery with decimals and penalty`() {
        val stroke = converter.fromEntryToStroke(DistanceToPin("25.5RecP"))
        assertThat(stroke.ground).isEqualTo(Ground.RECOVERY)
        assertThat(stroke.distanceToPin.unit).isEqualTo(DistanceUnit.METERS)
        assertThat(stroke.distanceToPin.distance).isEqualTo(25.5)
        assertThat(stroke.leadToPenalty).isEqualTo(true)
    }

    @Test
    fun `test all grounds`() {
        mapOf(
            "T" to Ground.TEE,
            "F" to Ground.FAIRWAY,
            "R" to Ground.ROUGH,
            "G" to Ground.GREEN,
            "Rec" to Ground.RECOVERY
        ).forEach { (input, ground) ->
            val stroke = converter.fromEntryToStroke(DistanceToPin("100${input}"))
            assertThat(stroke.ground).isEqualTo(ground)
            assertThat(stroke.distanceToPin.unit).isEqualTo(DistanceUnit.METERS)
            assertThat(stroke.distanceToPin.distance).isEqualTo(100.0)
            assertThat(stroke.leadToPenalty).isEqualTo(false)
        }
    }

    @Test
    fun `test lower case input should work`() {
        // TODO
    }
}