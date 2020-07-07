class Converter(
    private val groundMapper: GroundMapper
) {
    fun fromEntryToStroke(entry: Entry): Stroke {
        val lastThingsOfString = entry.text.dropWhile(Char::isDigit)
        val distance = entry.text.takeWhile(Char::isDigit).toDouble()

        return Stroke(
            ground = getGroundFromString(lastThingsOfString),
            distanceToPin = distance,
            distanceUnit = DistanceUnit.METERS, // TODO: Get as configuration/setup somehow
            leadToPenalty = leadToPenalty(lastThingsOfString)
        )
    }

    private fun getGroundFromString(lastThingsOfString: String): Ground {
        return when (lastThingsOfString.length) {
            1 -> groundMapper.ground(lastThingsOfString.last().toString())
            2 -> groundMapper.ground(lastThingsOfString.first().toString())
            else -> throw IllegalArgumentException("Wrong number of chars following an entry: $lastThingsOfString")
        }
    }

    private fun leadToPenalty(lastThingsOfString: String): Boolean {
        return lastThingsOfString.length == 2 && lastThingsOfString.takeLast(1) == groundMapper.penaltyCharacter()
    }

    companion object Factory {
        fun of(groundMapper: GroundMapper): Converter {
            return Converter(groundMapper)
        }
    }
}