import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CommonGroundMapperTest {
    @Test
    fun `test defaults`() {
        val mapper = CommonGroundMapper.create()
        assertThat(mapper.penaltyCharacter()).isEqualTo("P")

        mapOf(
            "T" to Ground.TEE,
            "F" to Ground.FAIRWAY,
            "R" to Ground.ROUGH,
            "G" to Ground.GREEN,
            "Rec" to Ground.RECOVERY,
            "S" to Ground.SAND
        ).forEach { (char, ground) ->
            assertThat(mapper.ground(char)).isEqualTo(ground)
        }
    }
}