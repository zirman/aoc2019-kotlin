fun main() {
    fun part1(input: List<String>): Int {
        val opcodes = input[0].split(",").map { it.toInt() }.toMutableList()

        opcodes[1] = 12
        opcodes[2] = 2

        var i = 0
        while (true) {
            when (opcodes[i]) {
                1 -> {
                    opcodes[opcodes[i + 3]] =
                        opcodes[opcodes[i + 1]] + opcodes[opcodes[i + 2]]

                    i += 4
                }
                2 -> {
                    opcodes[opcodes[i + 3]] =
                        opcodes[opcodes[i + 1]] * opcodes[opcodes[i + 2]]

                    i += 4
                }
                99 -> {
                    return opcodes[0]
                }
                else -> {
                    throw Exception()
                }
            }
        }
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    //check(part1(testInput) == 3500)
//    check(part2(testInput) == TODO())

    val input = readInput("Day02")
    println(part1(input))
//    println(part2(input))
}
