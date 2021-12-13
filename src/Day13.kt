enum class PongMode {
    ReadingX,
    ReadingY,
    ReadingTile,
}

fun main() {
    fun part1(input: List<String>): Int {
        fun machine(input: List<String>, read: () -> Long, write: (Long) -> Unit) {
            val opcodes: MutableMap<Long, Long> = input[0].split(",")
                .mapIndexed { index, string -> Pair(index.toLong(), string.toLong()) }
                .associate { it }
                .toMutableMap()

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
                        setIndex(modes, 1, read())
                        i += 2
                    }
                    4L -> {
                        write(getIndex(modes, 1))
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
                    99L -> return
                    else -> throw Exception("invalid op $i")
                }
            }
        }

        var mode = PongMode.ReadingX
        var x = 0L
        var y = 0L

        val tiles = mutableMapOf<Pair<Long, Long>, Long>()

        machine(
            input = input,
            read = {
                throw Exception()
            },
            write = { value ->
                mode = when (mode) {
                    PongMode.ReadingX -> {
                        x = value
                        PongMode.ReadingY
                    }
                    PongMode.ReadingY -> {
                        y = value
                        PongMode.ReadingTile
                    }
                    PongMode.ReadingTile -> {
                        tiles[Pair(x, y)] = value
                        PongMode.ReadingX
                    }
                }
            }
        )

        return tiles.values.count { it == 2L }
    }

    fun part2(input: List<String>): Long {
        fun machine(input: List<String>, read: () -> Long, write: (Long) -> Unit) {
            val opcodes: MutableMap<Long, Long> = input[0].split(",")
                .mapIndexed { index, string -> Pair(index.toLong(), string.toLong()) }
                .associate { it }
                .toMutableMap()

            opcodes[0] = 2

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
                        setIndex(modes, 1, read())
                        i += 2
                    }
                    4L -> {
                        write(getIndex(modes, 1))
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
                    99L -> return
                    else -> throw Exception("invalid op $i")
                }
            }
        }

        var mode = PongMode.ReadingX
        var x = 0L
        var y = 0L

        val tiles = mutableMapOf<Pair<Long, Long>, Long>()
        var score = 0L

        machine(
            input = input,
            read = {
//                (tiles.keys.map { (_, y) -> y }.minOf { it }..tiles.keys.map { (_, y) -> y }.maxOf { it })
//                    .forEach { y ->
//                        (tiles.keys.map { (x, _) -> x }.minOf { it }..tiles.keys.map { (x, _) -> x }.maxOf { it })
//                            .forEach { x ->
//                                print(
//                                    when (tiles[Pair(x, y)]) {
//                                        0L -> ' '
//                                        1L -> '#'
//                                        2L -> 'X'
//                                        3L -> '-'
//                                        4L -> '.'
//                                        else -> ' '
//                                    }
//                                )
//                            }
//
//                        println()
//                    }

                //                readln()

                val (ballPosition, _) = tiles.asSequence().first { (_, v) -> v == 4L }
                val (paddlePosition, _) = tiles.asSequence().first { (_, v) -> v == 3L }

                if (ballPosition.first < paddlePosition.first) {
                    -1
                } else if (ballPosition.first > paddlePosition.first) {
                    1
                } else {
                    0
                }
            },
            write = { value ->
                mode = when (mode) {
                    PongMode.ReadingX -> {
                        x = value
                        PongMode.ReadingY
                    }
                    PongMode.ReadingY -> {
                        y = value
                        PongMode.ReadingTile
                    }
                    PongMode.ReadingTile -> {
                        if (x == -1L && y == 0L) {
                            score = value
                        } else {
                            tiles[Pair(x, y)] = value
                        }

                        PongMode.ReadingX
                    }
                }
            }
        )

        return score
//        (tiles.keys.map { (_, y) -> y }.minOf { it }..tiles.keys.map { (_, y) -> y }.maxOf { it })
//            .forEach { y ->
//                (tiles.keys.map { (x, _) -> x }.minOf { it }..tiles.keys.map { (x, _) -> x }.maxOf { it })
//                    .forEach { x ->
//                        print(
//                            when (tiles[Pair(x, y)]) {
//                                0L -> ' '
//                                1L -> '#'
//                                2L -> 'X'
//                                3L -> '-'
//                                4L -> '.'
//                                else -> ' '
//                            }
//                        )
//                    }
//
//                println()
//            }
    }

    // test if implementation meets criteria from the description, like:

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
