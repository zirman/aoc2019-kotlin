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
        runBlocking {

        }
        var x = (1..10_000).flatMap { this }
        for (z in 1..100) {
            x = buildList {
                var w = 1
                while (size < x.size) {
                    var i = w
                    var sign = 1
                    var sum = 0
                    while (i - 1 in x.indices) {
                        sum = (sum + (sign * (i - 1..(i + w - 2).coerceIn(x.indices)).fold(0) { a, b -> (a + x[b]) % 10 })) % 10
                        sign = -sign
                        i += w + w
                    }
                    println("$w $size ${x.size}")
                    //println((sum % 10).absoluteValue)
                    add((sum % 10).absoluteValue)
                    w++
                }
            }.also { println(z) }
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
