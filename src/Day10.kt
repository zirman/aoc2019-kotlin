import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt
import kotlin.math.sqrt

data class Vec2(val x: Float, val y: Float)

operator fun Vec2.minus(v: Vec2): Vec2 =
    Vec2(x - v.x, y - v.y)

fun Vec2.length(): Float =
    sqrt(x * x + y * y)

fun main() {
    fun part1(input: List<String>): Int {
        val cs = input.map { line -> line.map { it } }

        val asteroids = cs.indices.flatMap { y ->
            cs[y].indices.filter { x -> cs[y][x] == '#' }.map { x -> Vec2(x.toFloat(), y.toFloat()) }
        }

        return asteroids.map { asteroid ->
            asteroids.mapNotNull { b ->
                if (b != asteroid) {
                    val relative = b - asteroid
                    atan2(-relative.x, relative.y)
                } else {
                    null
                }
            }.toSet().size
        }.maxOf { it }
    }

    fun part2(input: List<String>): Int {
        val cs = input.map { line -> line.map { it } }

        val asteroids = cs.indices.flatMap { y ->
            cs[y].indices.filter { x -> cs[y][x] == '#' }.map { x -> Vec2(x.toFloat(), y.toFloat()) }
        }

        val station = asteroids.map { asteroid ->
            Pair(
                asteroid,
                asteroids
                    .mapNotNull { otherAsteroid ->
                        if (otherAsteroid != asteroid) {
                            val relative = otherAsteroid - asteroid
                            atan2(-relative.x, relative.y)
                        } else {
                            null
                        }
                    }
                    .toSet()
                    .size
            )
        }.maxByOrNull { it.second }!!.first

        val destructionOrder = asteroids
            .mapNotNull { position ->
                if (position != station) {
                    val relative = position - station
                    Triple(position, relative.length(), atan2(-relative.x, relative.y))
                } else {
                    null
                }
            }
            .groupBy { it.third }
            .flatMap { (_, positionDistanceTheta) ->
                positionDistanceTheta
                    .sortedBy { (_, distance, _) -> distance }
                    .mapIndexed { index, (position, _, theta) -> Pair(position, (index * 2 * PI) + theta) }
            }
            .sortedBy { (_, theta) -> theta }[199]
            .first

        return (destructionOrder.x.roundToInt() * 100) + destructionOrder.y.roundToInt()
    }

    // test if implementation meets criteria from the description, like:

    check(part1(readInput("Day10_test")) == 8)
    check(part1(readInput("Day10_test2")) == 33)
    check(part1(readInput("Day10_test3")) == 35)
    check(part1(readInput("Day10_test4")) == 210)
    check(part2(readInput("Day10_test4")) == 802)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
