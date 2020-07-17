import strokesGained.Stroke

// TODO: Interface to this?
class Converter(
    private val groundMapper: GroundMapper
) {
    fun fromEntryToStroke(distanceToPin: DistanceToPin): Stroke {
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

        return Stroke(
            ground = getGroundFromString(distanceAndGround.second),
            distanceToPin = DenominatedValue(distanceAndGround.first.toDouble(), DistanceUnit.METERS),  // TODO: Get as config
            leadToPenalty = leadToPenalty(distanceAndGround.second)
        )
    }

    private fun partition(input: String): Pair<String, String> {
        return input.partition(Char::isDigit)
    }

    private fun getGroundFromString(input: String): Ground {
        return groundMapper.ground(input.removeSuffix(groundMapper.penaltyCharacter()))
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