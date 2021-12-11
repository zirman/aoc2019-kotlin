fun main() {
    fun part1(input: List<String>, inputValue: Long): List<Long> {
        val opcodes: MutableMap<Long, Long> = input[0].split(",")
            .mapIndexed { index, string -> Pair(index.toLong(), string.toLong()) }
            .associate { it }
            .toMutableMap()

        val output = mutableListOf<Long>()

        var i = 0L
        var rel = 0L

        fun getIndex(modes: Long, index: Long): Long =
            when (
                (
                        modes / when (index) {
                            1L -> 1
                            2L -> 10
                            3L -> 100
                            else -> throw Exception()
                        }
                        ) % 10
            ) {
                0L -> opcodes[opcodes[i + index] ?: 0] ?: 0
                1L -> opcodes[i + index] ?: 0
                2L -> opcodes[(opcodes[i + index] ?: 0) + rel] ?: 0
                else -> throw Exception()
            }

        fun setIndex(modes: Long, index: Long, value: Long) {
            opcodes[
                    (opcodes[i + index] ?: 0) + when (
                        (modes / when (index) {
                            1L -> 1
                            2L -> 10
                            3L -> 100
                            else -> throw Exception()
                        }) % 10
                    ) {
                        0L -> 0
                        2L -> rel
                        else -> throw Exception()
                    }
            ] = value
        }

        while (true) {
            val opcode = opcodes[i] ?: 0
            val op = opcode % 100
            val modes = opcode / 100

            when (op) {
                1L -> {
                    setIndex(modes, 3, getIndex(modes, 1) + getIndex(modes, 2))
                    i += 4
                }
                2L -> {
                    setIndex(modes, 3, getIndex(modes, 1) * getIndex(modes, 2))
                    i += 4
                }
                3L -> {
                    setIndex(modes, 1, inputValue)
                    i += 2
                }
                4L -> {
                    output.add(getIndex(modes, 1))
                    i += 2
                }
                5L -> {
                    if (getIndex(modes, 1) != 0L) {
                        i = getIndex(modes, 2)
                    } else {
                        i += 3
                    }
                }
                6L -> {
                    if (getIndex(modes, 1) == 0L) {
                        i = getIndex(modes, 2)
                    } else {
                        i += 3
                    }
                }
                7L -> {
                    setIndex(modes, 3, if (getIndex(modes, 1) < getIndex(modes, 2)) 1 else 0)
                    i += 4
                }
                8L -> {
                    setIndex(modes, 3, if (getIndex(modes, 1) == getIndex(modes, 2)) 1 else 0)
                    i += 4
                }
                9L -> {
                    rel += getIndex(modes, 1)
                    i += 2
                }
                99L -> return output
                else -> throw Exception("invalid op $i")
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    check(
        part1(listOf("109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99"), 1) ==
                listOf(109L, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99)
    )
    check(part1(listOf("1102,34915192,34915192,7,4,7,99,0"), 1) == listOf(1_219_070_632_396_864L))
    check(part1(listOf("104,1125899906842624,99"), 1) == listOf(1_125_899_906_842_624L))

    val input = readInput("Day09")
    println(part1(input, 1))
    println(part1(input, 2))
}
