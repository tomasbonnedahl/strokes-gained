import strokesGained.StrokeFFS

// TODO: Interface to this?
class Converter(
    private val groundMapper: GroundMapper
) {
    private fun partition(input: String): Pair<String, String> {
        return input.partition { c ->
            c.isDigit()
        }
    }

    fun fromEntryToStroke(distanceToPin: DistanceToPin): StrokeFFS {
        val distanceAndGround = if (distanceToPin.text.contains(".")) { // TODO: Support ,?
            val splitOnDecimalSeparator = distanceToPin.text.split(".")
            val halfDistanceAndGround = partition(splitOnDecimalSeparator[1])
            halfDistanceAndGround.copy(
                first = splitOnDecimalSeparator[0] + "." + halfDistanceAndGround.first,
                second = halfDistanceAndGround.second
            )
        } else {
            partition(distanceToPin.text)
        }

        return StrokeFFS(
            ground = getGroundFromString(distanceAndGround.second),
            distanceToPin = DenominatedValue(distanceAndGround.first.toDouble(), DistanceUnit.METERS),  // TODO: Get as config
            leadToPenalty = leadToPenalty(distanceAndGround.second)
        )
    }

    private fun getGroundFromString(lastThingsOfString: String): Ground {
        // TODO: Use an arg lib??
        return when (lastThingsOfString.length) {
            1 -> groundMapper.ground(lastThingsOfString.last().toString())
            2 -> groundMapper.ground(lastThingsOfString.first().toString())
            else -> throw IllegalArgumentException("Wrong number of chars following an entry: $lastThingsOfString")
        }
    }

    private fun leadToPenalty(input: String): Boolean {
        return input.contains(groundMapper.penaltyCharacter())
    }

    companion object {
        fun of(groundMapper: GroundMapper): Converter {
            return Converter(groundMapper)
        }
    }
}