import kotlin.time.measureTimedValue

typealias Input17 = List<Long>
typealias Result17 = Int

fun main() {
    fun List<String>.parse(): Input17 {
        return flatMap { line -> line.split(",").map { it.toLong() } }
    }

    fun List<Long>.part1(): Result17 {
        val opcodes: MutableMap<Long, Long> = this
            .mapIndexed { index, code -> index.toLong() to code }
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
                            else -> error("")
                        }
                        ) % 10
            ) {
                0L -> opcodes[opcodes[i + index] ?: 0] ?: 0
                1L -> opcodes[i + index] ?: 0
                2L -> opcodes[(opcodes[i + index] ?: 0) + rel] ?: 0
                else -> error("")
            }

        fun setIndex(modes: Long, index: Long, value: Long) {
            opcodes[
                (opcodes[i + index] ?: 0) + when (
                    (modes / when (index) {
                        1L -> 1
                        2L -> 10
                        3L -> 100
                        else -> error("")
                    }) % 10
                ) {
                    0L -> 0
                    2L -> rel
                    else -> error("")
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
                    error("invalid op")
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

                99L -> return run {
                    val s = output.joinToString("") { it.toInt().toChar().toString() }
                        .lines().dropLastWhile { it.isEmpty() }.map { it.toList() }
                    val size = s.toSize2()
                    s.foldIndexed(0) { row, acc, line ->
                        line.foldIndexed(acc) { col, acc2, _ ->
                            val p = Pos(row, col)
                            if (s[p] == '#' && p.next(size).count { s[it] == '#' } == 4) {
                                acc2 + (row * col)
                            } else {
                                acc2
                            }
                        }
                    }
                }

                else -> error("invalid op $i")
            }
        }
    }

    fun List<Long>.part2(inputValues: Iterator<Long>): String {
        val opcodes: MutableMap<Long, Long> = this
            .mapIndexed { index, code -> index.toLong() to code }
            .associate { it }
            .toMutableMap()

        opcodes[0] = 2
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
                            else -> error("")
                        }
                        ) % 10
            ) {
                0L -> opcodes[opcodes[i + index] ?: 0] ?: 0
                1L -> opcodes[i + index] ?: 0
                2L -> opcodes[(opcodes[i + index] ?: 0) + rel] ?: 0
                else -> error("")
            }

        fun setIndex(modes: Long, index: Long, value: Long) {
            opcodes[
                (opcodes[i + index] ?: 0) + when (
                    (modes / when (index) {
                        1L -> 1
                        2L -> 10
                        3L -> 100
                        else -> error("")
                    }) % 10
                ) {
                    0L -> 0
                    2L -> rel
                    else -> error("")
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
                    setIndex(modes, 1, inputValues.next())
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

                99L -> {
                    return output.joinToString("") {
                        if (it < 128) {
                            "${it.toInt().toChar()}"
                        } else {
                            "$it"
                        }
                    }
                }

                else -> error("invalid op $i")
            }
        }
    }

    val input = readInput("Day17")
    measureTimedValue { input.parse().part1() }.println()
    measureTimedValue {
        input.parse().part2(
            """
        A,A,B,C,B,C,B,A,C,A
        R,8,L,12,R,8
        L,10,L,10,R,8
        L,12,L,12,L,10,R,10
        n
    
    """
                .trimIndent()
                .map { it.code.toLong() }
                .iterator()
        )
    }.println()
}
