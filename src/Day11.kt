enum class Dir {
    Up,
    Down,
    Left,
    Right,
}

enum class RobotMode {
    Drawing,
    Turning,
}

fun main() {
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

    fun part1(input: List<String>): Int {
        val tiles = mutableMapOf<Pair<Int, Int>, Long>()
        var x = 0
        var y = 0
        var dir = Dir.Up
        var mode = RobotMode.Drawing

        val paintedTiles = mutableSetOf<Pair<Int, Int>>()

        machine(
            input = input,
            read = {
                tiles[Pair(x, y)] ?: 0L
            },
            write = { value ->
                when (mode) {
                    RobotMode.Drawing -> {
                        if (value == 0L) {
                            tiles.remove(Pair(x, y))
                        } else {
                            tiles[Pair(x, y)] = value
                        }
                        paintedTiles.add(Pair(x, y))
                        mode = RobotMode.Turning
                    }
                    RobotMode.Turning -> {
                        dir = when (value) {
                            0L -> when (dir) {
                                Dir.Up -> Dir.Left
                                Dir.Down -> Dir.Right
                                Dir.Left -> Dir.Down
                                Dir.Right -> Dir.Up
                            }
                            1L -> when (dir) {
                                Dir.Up -> Dir.Right
                                Dir.Down -> Dir.Left
                                Dir.Left -> Dir.Up
                                Dir.Right -> Dir.Down
                            }
                            else -> {
                                throw Exception()
                            }
                        }

                        when (dir) {
                            Dir.Up -> {
                                y--
                            }
                            Dir.Down -> {
                                y++
                            }
                            Dir.Left -> {
                                x--
                            }
                            Dir.Right -> {
                                x++
                            }
                        }

                        mode = RobotMode.Drawing
                    }
                }
            },
        )

        return paintedTiles.size
    }

    fun part2(input: List<String>): String {
        val tiles = mutableMapOf(Pair(0, 0) to 1L)
        var x = 0
        var y = 0
        var dir = Dir.Up
        var mode = RobotMode.Drawing

        machine(
            input = input,
            read = {
                tiles[Pair(x, y)] ?: 0L
            },
            write = { value ->
                when (mode) {
                    RobotMode.Drawing -> {
                        if (value == 0L) {
                            tiles.remove(Pair(x, y))
                        } else {
                            tiles[Pair(x, y)] = value
                        }
                        mode = RobotMode.Turning
                    }
                    RobotMode.Turning -> {
                        dir = when (value) {
                            0L -> when (dir) {
                                Dir.Up -> Dir.Left
                                Dir.Down -> Dir.Right
                                Dir.Left -> Dir.Down
                                Dir.Right -> Dir.Up
                            }
                            1L -> when (dir) {
                                Dir.Up -> Dir.Right
                                Dir.Down -> Dir.Left
                                Dir.Left -> Dir.Up
                                Dir.Right -> Dir.Down
                            }
                            else -> {
                                throw Exception()
                            }
                        }

                        when (dir) {
                            Dir.Up -> {
                                y--
                            }
                            Dir.Down -> {
                                y++
                            }
                            Dir.Left -> {
                                x--
                            }
                            Dir.Right -> {
                                x++
                            }
                        }

                        mode = RobotMode.Drawing
                    }
                }
            },
        )

        return (tiles.keys.minOf { (_, y) -> y }..tiles.keys.maxOf { (_, y) -> y })
            .joinToString("\n") { i ->
                (tiles.keys.minOf { (x, _) -> x }..tiles.keys.maxOf { (x, _) -> x })
                    .map { k -> tiles[Pair(k, i)] ?: 0L }
                    .joinToString("") { if (it == 0L) " " else "#" }
            }
    }

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
