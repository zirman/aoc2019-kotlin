fun main() {
    fun part1(input: List<String>, inputValues: List<Int>): List<Int> {
        val opcodes = input[0].split(",").map { it.toInt() }.toMutableList()

        var inputI = 0
        val output = mutableListOf<Int>()

        var i = 0
        while (true) {
            val opcode = opcodes[i]
            val op = opcode % 100
            val modes = opcode / 100
            when (op) {
                1 -> {
                    val r1Mode = (modes / 1) % 10
                    val r2Mode = (modes / 10) % 10
                    assert((modes / 100) % 10 == 0)

                    opcodes[opcodes[i + 3]] =
                        when (r1Mode) {
                            0 -> {
                                opcodes[opcodes[i + 1]]
                            }
                            1 -> {
                                opcodes[i + 1]
                            }
                            else -> {
                                throw Exception()
                            }
                        } + when (r2Mode) {
                            0 -> {
                                opcodes[opcodes[i + 2]]
                            }
                            1 -> {
                                opcodes[i + 2]
                            }
                            else -> {
                                throw Exception()
                            }
                        }

                    i += 4
                }
                2 -> {
                    val r1Mode = (modes / 1) % 10
                    val r2Mode = (modes / 10) % 10
                    assert((modes / 100) % 10 == 0)

                    opcodes[opcodes[i + 3]] =
                        when (r1Mode) {
                            0 -> {
                                opcodes[opcodes[i + 1]]
                            }
                            1 -> {
                                opcodes[i + 1]
                            }
                            else -> {
                                throw Exception()
                            }
                        } * when (r2Mode) {
                            0 -> {
                                opcodes[opcodes[i + 2]]
                            }
                            1 -> {
                                opcodes[i + 2]
                            }
                            else -> {
                                throw Exception()
                            }
                        }

                    i += 4
                }
                3 -> {
                    opcodes[opcodes[i + 1]] =
                        inputValues[inputI]

                    inputI++
                    i += 2
                }
                4 -> {
                    val r1Mode = (modes / 1) % 10

                    output.add(
                        when (r1Mode) {
                            0 -> {
                                opcodes[opcodes[i + 1]]
                            }
                            1 -> {
                                opcodes[i + 1]
                            }
                            else -> {
                                throw Exception()
                            }
                        }
                    )

                    i += 2
                }
                99 -> {
                    return output
                }
                else -> {
                    println("invalid op $i")
                    throw Exception()
                }
            }
        }
    }

    fun part2(input: List<String>, inputValues: List<Int>): List<Int> {
        val opcodes = input[0].split(",").map { it.toInt() }.toMutableList()

        var inputI = 0
        val output = mutableListOf<Int>()

        var i = 0
        while (true) {
            val opcode = opcodes[i]
            val op = opcode % 100
            val modes = opcode / 100
            when (op) {
                1 -> {
                    val r1Mode = (modes / 1) % 10
                    val r2Mode = (modes / 10) % 10
                    assert((modes / 100) % 10 == 0)

                    opcodes[opcodes[i + 3]] =
                        when (r1Mode) {
                            0 -> {
                                opcodes[opcodes[i + 1]]
                            }
                            1 -> {
                                opcodes[i + 1]
                            }
                            else -> {
                                throw Exception()
                            }
                        } + when (r2Mode) {
                            0 -> {
                                opcodes[opcodes[i + 2]]
                            }
                            1 -> {
                                opcodes[i + 2]
                            }
                            else -> {
                                throw Exception()
                            }
                        }

                    i += 4
                }
                2 -> {
                    val r1Mode = (modes / 1) % 10
                    val r2Mode = (modes / 10) % 10
                    assert((modes / 100) % 10 == 0)

                    opcodes[opcodes[i + 3]] =
                        when (r1Mode) {
                            0 -> {
                                opcodes[opcodes[i + 1]]
                            }
                            1 -> {
                                opcodes[i + 1]
                            }
                            else -> {
                                throw Exception()
                            }
                        } * when (r2Mode) {
                            0 -> {
                                opcodes[opcodes[i + 2]]
                            }
                            1 -> {
                                opcodes[i + 2]
                            }
                            else -> {
                                throw Exception()
                            }
                        }

                    i += 4
                }
                3 -> {
                    assert((modes / 1) % 10 == 0)
                    opcodes[opcodes[i + 1]] =
                        inputValues[inputI]

                    inputI++
                    i += 2
                }
                4 -> {
                    val r1Mode = (modes / 1) % 10

                    output.add(
                        when (r1Mode) {
                            0 -> {
                                opcodes[opcodes[i + 1]]
                            }
                            1 -> {
                                opcodes[i + 1]
                            }
                            else -> {
                                throw Exception()
                            }
                        }
                    )

                    i += 2
                }
                5 -> {
                    val r1Mode = (modes / 1) % 10
                    val r2Mode = (modes / 10) % 10

                    if (
                        when (r1Mode) {
                            0 -> {
                                opcodes[opcodes[i + 1]]
                            }
                            1 -> {
                                opcodes[i + 1]
                            }
                            else -> {
                                throw Exception()
                            }
                        } != 0
                    ) {
                        i = when (r2Mode) {
                            0 -> {
                                opcodes[opcodes[i + 2]]
                            }
                            1 -> {
                                opcodes[i + 2]
                            }
                            else -> {
                                throw Exception()
                            }
                        }
                    } else {
                        i += 3
                    }
                }
                6 -> {
                    val r1Mode = (modes / 1) % 10
                    val r2Mode = (modes / 10) % 10

                    if (
                        when (r1Mode) {
                            0 -> {
                                opcodes[opcodes[i + 1]]
                            }
                            1 -> {
                                opcodes[i + 1]
                            }
                            else -> {
                                throw Exception()
                            }
                        } == 0
                    ) {
                        i = when (r2Mode) {
                            0 -> {
                                opcodes[opcodes[i + 2]]
                            }
                            1 -> {
                                opcodes[i + 2]
                            }
                            else -> {
                                throw Exception()
                            }
                        }
                    } else {
                        i += 3
                    }
                }
                7 -> {
                    val r1Mode = (modes / 1) % 10
                    val r2Mode = (modes / 10) % 10
                    assert((modes / 100) % 10 == 0)

                    opcodes[opcodes[i + 3]] =
                        if (
                            when (r1Mode) {
                                0 -> {
                                    opcodes[opcodes[i + 1]]
                                }
                                1 -> {
                                    opcodes[i + 1]
                                }
                                else -> {
                                    throw Exception()
                                }
                            } < when (r2Mode) {
                                0 -> {
                                    opcodes[opcodes[i + 2]]
                                }
                                1 -> {
                                    opcodes[i + 2]
                                }
                                else -> {
                                    throw Exception()
                                }
                            }
                        ) {
                            1
                        } else {
                            0
                        }

                    i += 4
                }
                8 -> {
                    val r1Mode = (modes / 1) % 10
                    val r2Mode = (modes / 10) % 10
                    assert((modes / 100) % 10 == 0)

                    opcodes[opcodes[i + 3]] =
                        if (
                            when (r1Mode) {
                                0 -> {
                                    opcodes[opcodes[i + 1]]
                                }
                                1 -> {
                                    opcodes[i + 1]
                                }
                                else -> {
                                    throw Exception()
                                }
                            } == when (r2Mode) {
                                0 -> {
                                    opcodes[opcodes[i + 2]]
                                }
                                1 -> {
                                    opcodes[i + 2]
                                }
                                else -> {
                                    throw Exception()
                                }
                            }
                        ) {
                            1
                        } else {
                            0
                        }

                    i += 4
                }
                99 -> {
                    return output
                }
                else -> {
                    println("invalid op $op at $i")
                    throw Exception()
                }
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    //val testInput = readInput("Day05_test")
    //check(part1(testInput, listOf(1)) == listOf(1))
    check(part2(listOf("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9"), listOf(0)) == listOf(0))
    check(part2(listOf("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9"), listOf(10)) == listOf(1))
    check(part2(listOf("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9"), listOf(0)) == listOf(0))

    val input = readInput("Day05")
    println(part1(input, listOf(1)))
    println(part2(input, listOf(5)))
}
