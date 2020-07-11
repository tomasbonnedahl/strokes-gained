import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test
import java.lang.Math.abs
import kotlin.math.roundToInt

class BenchmarkAndInterpolationTest {
    @Test
    fun `test getting in between`() {
        val benchmark = StrokesGainedPgaTourBenchmarkRepository(
            strokesByDistanceTee = mapOf(
                DenominatedValue(130.0, DistanceUnit.METERS) to 3.0,
                DenominatedValue(140.0, DistanceUnit.METERS) to 3.6
            ),

            // TODO: Who's responsible of interpolating??
            strokesByDistanceFairway = emptyMap(),
            strokesByDistanceRough = emptyMap(),
            strokesByDistanceRecovery = emptyMap(),
            strokesByDistancePutt = emptyMap()
        )

        val subject = BenchmarkAndInterpolation(benchmark)

        val sg = subject.get(
            DenominatedValue(135.0, DistanceUnit.METERS),
            Ground.TEE
        )
        assertThat(sg).isEqualTo(3.3, within(0.000001))
    }

    @Test
    fun `how to iterate a map`() {
        val map = mapOf(
            DenominatedValue(100.0, DistanceUnit.METERS) to 2.5,
            DenominatedValue(500.0, DistanceUnit.METERS) to 4.5
        )

        val keys = map.keys.sortedBy { it.distance }
        println("keys = ${keys}")

        for (i in (keys.indices - 1)) {
            println("i = ${i}")
            println("i = ${keys[i]}")
            println("i = ${map[keys[i]]}")
            println("i+ = ${i + 1}")
            println("i+ = ${keys[i + 1]}")
            println("i+ = ${map[keys[i + 1]]}")

            val extended = extend(
                keys[i].distance.roundToInt(),
                map[keys[i]]!!,
                keys[i + 1].distance.roundToInt(),
                map[keys[i + 1]]!!,
                3
            )

            println("extended = ${extended}")
        }
    }


    @Test
    fun `test interpolation`() {
        val distances = listOf(10, 20, 30)

        listOf(10, 11, 12, 13, 14, 15, 16).forEach {
            println("it = ${distances.closestValue(it)}")
        }

        val res = extend(10, 50.0, 20, 70.0, 2)
        println("res = ${res}")
    }

    // TODO: Put in extensions.kt
    fun List<Int>.closestValue(value: Int) = minBy { abs(value - it) }
}