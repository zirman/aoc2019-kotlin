fun main() {
    fun part1(input: List<String>, rows: Int, cols: Int): Int {
        val layers = input.joinToString("").map { it }.windowed(rows * cols, step = rows * cols)
        val layersWithLeastZeros = layers.minByOrNull { layer -> layer.count { it == '0' } }!!
        return layersWithLeastZeros.count { it == '1' } * layersWithLeastZeros.count { it == '2' }
    }

    fun part2(input: List<String>, rows: Int, cols: Int) {
        input
            .joinToString("").map { it }.windowed(rows * cols, step = rows * cols)
            .reduceRight { a, b ->
                b.indices.map { index ->
                    when (a[index]) {
                        '0' -> {
                            a[index]
                        }
                        '1' -> {
                            a[index]
                        }
                        '2' -> {
                            b[index]
                        }
                        else -> {
                            throw Exception()
                        }
                    }
                }
            }
            .windowed(cols, cols)
            .forEach { row ->
                println(
                    row.map { if (it == '0') ' ' else it }
                        .joinToString("")
                )
            }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(listOf("123456789012"), 3, 2) == 1)

    val input = readInput("Day09")
    println(part1(input, 6, 25))
    part2(input, 6, 25)
}
