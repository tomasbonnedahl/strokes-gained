import strokesGained.StrokeFFS

// TODO: Interface to this?
class Converter(
    private val groundMapper: GroundMapper
) {
    fun fromEntryToStroke(distanceToPin: DistanceToPin): StrokeFFS {
        val lastThingsOfString = distanceToPin.text.dropWhile(Char::isDigit)
        val distance = distanceToPin.text.takeWhile(Char::isDigit).toDouble()

        return StrokeFFS(
            ground = getGroundFromString(lastThingsOfString),
            distanceToPin = DenominatedValue(distance, DistanceUnit.METERS),  // TODO: Get as config
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

    companion object {
        fun of(groundMapper: GroundMapper): Converter {
            return Converter(groundMapper)
        }
    }
}