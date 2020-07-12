// TODO: @Component
class CommonGroundMapper(
    private val groundByString: Map<String, Ground>,
    private val penaltyCharacter: String
) : GroundMapper {
    override fun ground(text: String): Ground {
        return groundByString[text] ?: throw IllegalArgumentException("Not possible to map $text in given mapper")
    }

    override fun penaltyCharacter(): String {
        return penaltyCharacter
    }

    companion object {
        private val groundByString = mapOf(
            "T" to Ground.TEE,
            "F" to Ground.FAIRWAY,
            "R" to Ground.ROUGH,
            "G" to Ground.GREEN
            // TODO: Add recovery and sand
        )

        fun create(): CommonGroundMapper {
            return CommonGroundMapper(groundByString, "P")
        }
    }
}