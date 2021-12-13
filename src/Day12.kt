import kotlin.math.absoluteValue
import kotlin.math.max

data class Vec3(val x: Int, val y: Int, val z: Int)

operator fun Vec3.plus(v: Vec3): Vec3 =
    Vec3(x + v.x, y + v.y, z + v.z)

data class Moon(var position: Vec3, var velocity: Vec3)

fun main() {
    fun part1(input: List<String>, iterations: Int): Long {
        val moons = input.map { line ->
            val groupValues = Regex("""<x=(-?\d+), y=(-?\d+), z=(-?\d+)>""").matchEntire(line)!!.groupValues
            Moon(
                Vec3(
                    groupValues[1].toInt(),
                    groupValues[2].toInt(),
                    groupValues[3].toInt()
                ),
                Vec3(0, 0, 0)
            )
        }

        for (i in 1..iterations) {
            moons.forEach { moon ->
                val (position, velocity) = moon

                moon.velocity = moons.fold(velocity) { velocityAcc, otherMoon ->
                    if (moon != otherMoon) {
                        val other = otherMoon.position
                        Vec3(
                            x = velocityAcc.x + if (other.x > position.x) 1 else if (other.x < position.x) -1 else 0,
                            y = velocityAcc.y + if (other.y > position.y) 1 else if (other.y < position.y) -1 else 0,
                            z = velocityAcc.z + if (other.z > position.z) 1 else if (other.z < position.z) -1 else 0
                        )
                    } else {
                        velocityAcc
                    }
                }
            }

            moons.forEach { moon ->
                moon.position = moon.position + moon.velocity
            }
        }

        return moons.fold(0L) { energy, (position, velocity) ->
            energy + (position.x.absoluteValue + position.y.absoluteValue + position.z.absoluteValue) *
                    (velocity.x.absoluteValue + velocity.y.absoluteValue + velocity.z.absoluteValue)
        }
    }

    fun part2(input: List<String>): Long {
        fun iterate(positions: IntArray, velocities: IntArray) {
            positions.indices.forEach { i ->
                (i + 1 until positions.size).forEach { k ->
                    if (positions[i] < positions[k]) {
                        velocities[i]++
                        velocities[k]--
                    } else if (positions[i] > positions[k]) {
                        velocities[i]--
                        velocities[k]++
                    }
                }
            }

            positions.indices.forEach { index -> positions[index] += velocities[index] }
        }

        fun cycles(p: List<Int>): Long {
            val initialPositions = p.toIntArray()
            val initialVelocities = p.map { 0 }.toIntArray()
            val positions = initialPositions.copyOf()
            val velocities = initialVelocities.copyOf()

            for (i in 1..Long.MAX_VALUE) {
                iterate(positions, velocities)

                if (positions.contentEquals(initialPositions) &&
                    velocities.contentEquals(initialVelocities)
                ) {
                    return i
                }
            }

            throw Exception()
        }

        val positions = input.map { line ->
            val groupValues = Regex("""<x=(-?\d+), y=(-?\d+), z=(-?\d+)>""").matchEntire(line)!!.groupValues
            Vec3(
                groupValues[1].toInt(),
                groupValues[2].toInt(),
                groupValues[3].toInt()
            )
        }

        val xCycles = cycles(positions.map { (x, _, _) -> x })
        val yCycles = cycles(positions.map { (_, y, _) -> y })
        val zCycles = cycles(positions.map { (_, _, z) -> z })

        val max = max(max(xCycles, yCycles), zCycles)

        for (i in max until Long.MAX_VALUE step max) {
            if (i % xCycles == 0L && i % yCycles == 0L && i % zCycles == 0L) {
                return i
            }
        }

        throw Exception()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    val testInput2 = readInput("Day12_test2")
    check(part1(testInput, 10) == 179L)
    check(part1(testInput2, 100) == 1_940L)
    check(part2(testInput) == 2_772L)
    check(part2(testInput2) == 4_686_774_924L)

    val input = readInput("Day12")
    println(part1(input, 1000))
    println(part2(input))
}
