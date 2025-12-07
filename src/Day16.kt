import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.time.measureTimedValue

typealias Input16 = List<Int>
typealias Result16 = Long

fun main() {
    fun base(phase: Int) = sequence {
        while (true) {
            yield(0)
            yield(1)
            yield(0)
            yield(-1)
        }
    }
        .flatMap { x -> buildList { repeat(phase) { add(x) } } }
        .drop(1)

    fun List<String>.parse(): Input16 {
        return flatMap { line -> line.map { it.digitToInt() } }
    }

    fun Input16.part1(): Result16 {
        var foo = this
        for (phase in 1..100) {
            val k = mutableListOf<Int>()
            for (i in 1..foo.size) {
                k.add(abs(foo.zip(base(i).take(foo.size).toList()).sumOf { (a, b) -> a * b } % 10))
            }
            foo = k
        }
        return foo.take(8).fold(0) { a, b -> (a * 10) + b }
    }

    fun Input16.part2(): Result16 {
        var x = (1..10_000).flatMap { this }
        println(x.size)
        for (z in 1..100) {
            val cores = 1
            x = runBlocking(Dispatchers.Default) {
                (0..<cores).map { core ->
                    async {
                        buildList {
                            ((x.size * core) / cores..<(x.size * (core + 1)) / cores).also { println(it) }.forEach { w ->
                                var i = w + 1
                                var sign = 1
                                var sum = 0
                                while (i - 1 in x.indices) {
                                    sum =
                                        (sum + (sign * (i - 1..(i + w - 2).coerceIn(x.indices)).fold(0) { a, b -> (a + x[b]) % 10 })) % 10
                                    sign = -sign
                                    i += 2 * (w + 1)
                                    if (core == 0) {
                                        println("$i / ${(x.size * (core + 1)) / cores}")
                                    }
                                }
                                add((sum % 10).absoluteValue)
                            }
                            println("asdf")
                        }
                    }
                }.awaitAll().flatMap { it }
            }
            println(z)
        }
        return x.drop(take(7).fold(0) { a, b -> (a * 10) + b }).take(8).fold(0) { a, b -> (a * 10) + b }
    }
    // test if implementation meets criteria from the description, like:
//    val testInput1 = readInput("Day16_1_test")
//    check(testInput1.parse().part1() == 24176176L)
//    val testInput2 = readInput("Day16_2_test")
//    check(testInput2.parse().part2() == 84462026L)
    val input = readInput("Day16")
//    measureTimedValue { input.parse().part1() }.println()
    measureTimedValue { input.parse().part2() }.println()
}
