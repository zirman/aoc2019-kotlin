fun main() {
    fun part1(input: List<String>): Long {
        fun amplifier(input: List<String>, inputValues: List<Long>): List<Long> {
            val opcodes = input[0].split(",").map { it.toLong() }.toMutableList()

            var inputI = 0L
            val output = mutableListOf<Long>()

            var i = 0
            while (true) {
                val opcode = opcodes[i]
                val op = opcode % 100L
                val modes = opcode / 100L
                when (op) {
                    1L -> {
                        val r1Mode = (modes / 1L) % 10L
                        val r2Mode = (modes / 10L) % 10L
                        assert((modes / 100L) % 10L == 0L)

                        opcodes[opcodes[i + 3].toInt()] =
                            when (r1Mode) {
                                0L -> {
                                    opcodes[opcodes[i + 1].toInt()]
                                }
                                1L -> {
                                    opcodes[(i + 1L).toInt()]
                                }
                                else -> {
                                    throw Exception()
                                }
                            } + when (r2Mode) {
                                0L -> {
                                    opcodes[opcodes[i + 2].toInt()]
                                }
                                1L -> {
                                    opcodes[i + 2]
                                }
                                else -> {
                                    throw Exception()
                                }
                            }

                        i += 4
                    }
                    2L -> {
                        val r1Mode = (modes / 1L) % 10L
                        val r2Mode = (modes / 10L) % 10L
                        assert((modes / 100L) % 10L == 0L)

                        opcodes[opcodes[(i + 3L).toInt()].toInt()] =
                            when (r1Mode) {
                                0L -> {
                                    opcodes[opcodes[i + 1].toInt()]
                                }
                                1L -> {
                                    opcodes[i + 1]
                                }
                                else -> {
                                    throw Exception()
                                }
                            } * when (r2Mode) {
                                0L -> {
                                    opcodes[opcodes[i + 2].toInt()]
                                }
                                1L -> {
                                    opcodes[i + 2]
                                }
                                else -> {
                                    throw Exception()
                                }
                            }

                        i += 4
                    }
                    3L -> {
                        assert((modes / 1L) % 10L == 0L)
                        opcodes[(opcodes[(i + 1L).toInt()]).toInt()] =
                            inputValues[inputI.toInt()]

                        inputI++
                        i += 2
                    }
                    4L -> {
                        val r1Mode = (modes / 1L) % 10L

                        output.add(
                            when (r1Mode) {
                                0L -> {
                                    opcodes[opcodes[i + 1].toInt()]
                                }
                                1L -> {
                                    opcodes[(i + 1L).toInt()]
                                }
                                else -> {
                                    throw Exception()
                                }
                            }
                        )

                        i += 2
                    }
                    5L -> {
                        val r1Mode = (modes / 1L) % 10L
                        val r2Mode = (modes / 10L) % 10L

                        if (
                            when (r1Mode) {
                                0L -> {
                                    opcodes[opcodes[i + 1].toInt()]
                                }
                                1L -> {
                                    opcodes[i + 1]
                                }
                                else -> {
                                    throw Exception()
                                }
                            } != 0L
                        ) {
                            i = when (r2Mode) {
                                0L -> {
                                    opcodes[opcodes[i + 2].toInt()]
                                }
                                1L -> {
                                    opcodes[i + 2]
                                }
                                else -> {
                                    throw Exception()
                                }
                            }.toInt()
                        } else {
                            i += 3
                        }
                    }
                    6L -> {
                        val r1Mode = (modes / 1L) % 10L
                        val r2Mode = (modes / 10L) % 10L

                        if (
                            when (r1Mode) {
                                0L -> {
                                    opcodes[opcodes[(i + 1L).toInt()].toInt()]
                                }
                                1L -> {
                                    opcodes[(i + 1L).toInt()]
                                }
                                else -> {
                                    throw Exception()
                                }
                            } == 0L
                        ) {
                            i = when (r2Mode) {
                                0L -> {
                                    opcodes[opcodes[i + 2].toInt()]
                                }
                                1L -> {
                                    opcodes[i + 2]
                                }
                                else -> {
                                    throw Exception()
                                }
                            }.toInt()
                        } else {
                            i += 3
                        }
                    }
                    7L -> {
                        val r1Mode = (modes / 1) % 10
                        val r2Mode = (modes / 10) % 10
                        assert((modes / 100) % 10 == 0L)

                        opcodes[opcodes[i + 3].toInt()] =
                            if (
                                when (r1Mode) {
                                    0L -> {
                                        opcodes[opcodes[i + 1].toInt()]
                                    }
                                    1L -> {
                                        opcodes[i + 1]
                                    }
                                    else -> {
                                        throw Exception()
                                    }
                                } < when (r2Mode) {
                                    0L -> {
                                        opcodes[opcodes[i + 2].toInt()]
                                    }
                                    1L -> {
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
                    8L -> {
                        val r1Mode = (modes / 1) % 10
                        val r2Mode = (modes / 10) % 10
                        assert((modes / 100L) % 10 == 0L)

                        opcodes[opcodes[i + 3].toInt()] =
                            if (
                                when (r1Mode) {
                                    0L -> {
                                        opcodes[opcodes[i + 1].toInt()]
                                    }
                                    1L -> {
                                        opcodes[i + 1]
                                    }
                                    else -> {
                                        throw Exception()
                                    }
                                } == when (r2Mode) {
                                    0L -> {
                                        opcodes[opcodes[i + 2].toInt()]
                                    }
                                    1L -> {
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
                    99L -> {
                        return output
                    }
                    else -> {
                        println("invalid op $op at $i")
                        throw Exception()
                    }
                }
            }
        }

        return (0L..4L).flatMap { phaseA ->
            val inputB = amplifier(input, listOf(phaseA, 0L)).last()

            (0L..4L).flatMap { phaseB ->
                if (phaseB != phaseA) {
                    val inputC = amplifier(input, listOf(phaseB, inputB)).last()

                    (0L..4L).flatMap { phaseC ->
                        if (phaseC != phaseA && phaseC != phaseB) {
                            val inputD = amplifier(input, listOf(phaseC, inputC)).last()

                            (0L..4L).flatMap { phaseD ->
                                if (phaseD != phaseA && phaseD != phaseB && phaseD != phaseC) {
                                    val inputE = amplifier(input, listOf(phaseD, inputD)).last()

                                    (0L..4L).flatMap { phaseE ->
                                        if (phaseE != phaseA && phaseE != phaseB && phaseE != phaseC && phaseE != phaseD) {
                                            listOf(amplifier(input, listOf(phaseE, inputE)).last())
                                        } else {
                                            emptyList()
                                        }
                                    }
                                } else {
                                    emptyList()
                                }
                            }
                        } else {
                            emptyList()
                        }
                    }
                } else {
                    emptyList()
                }
            }
        }.maxOf { it }
    }

    fun part2(input: List<String>): Long {
        fun amplifier(input: List<String>, first: Long): (inputValues: Long) -> Long? {
            val opcodes = input[0].split(",").map { it.toLong() }.toMutableList()

            var i = 0

            while (true) {
                val opcode = opcodes[i]
                val op = opcode % 100
                val modes = opcode / 100
                when (op) {
                    1L -> {
                        val r1Mode = (modes / 1) % 10
                        val r2Mode = (modes / 10) % 10
                        assert((modes / 100) % 10 == 0L)

                        opcodes[opcodes[i + 3].toInt()] =
                            when (r1Mode) {
                                0L -> {
                                    opcodes[opcodes[i + 1].toInt()]
                                }
                                1L -> {
                                    opcodes[i + 1]
                                }
                                else -> {
                                    throw Exception()
                                }
                            } + when (r2Mode) {
                                0L -> {
                                    opcodes[opcodes[i + 2].toInt()]
                                }
                                1L -> {
                                    opcodes[i + 2]
                                }
                                else -> {
                                    throw Exception()
                                }
                            }

                        i += 4
                    }
                    2L -> {
                        val r1Mode = (modes / 1) % 10
                        val r2Mode = (modes / 10) % 10
                        assert((modes / 100) % 10 == 0L)

                        opcodes[opcodes[i + 3].toInt()] =
                            when (r1Mode) {
                                0L -> {
                                    opcodes[opcodes[i + 1].toInt()]
                                }
                                1L -> {
                                    opcodes[i + 1]
                                }
                                else -> {
                                    throw Exception()
                                }
                            } * when (r2Mode) {
                                0L -> {
                                    opcodes[opcodes[i + 2].toInt()]
                                }
                                1L -> {
                                    opcodes[i + 2]
                                }
                                else -> {
                                    throw Exception()
                                }
                            }

                        i += 4
                    }
                    3L -> {
                        assert((modes / 1) % 10 == 0L)
                        opcodes[opcodes[i + 1].toInt()] =
                            first

                        i += 2
                        break
                    }
                    4L -> {
                        throw Exception()
                    }
                    5L -> {
                        val r1Mode = (modes / 1) % 10
                        val r2Mode = (modes / 10) % 10

                        if (
                            when (r1Mode) {
                                0L -> {
                                    opcodes[opcodes[i + 1].toInt()]
                                }
                                1L -> {
                                    opcodes[i + 1]
                                }
                                else -> {
                                    throw Exception()
                                }
                            } != 0L
                        ) {
                            i = when (r2Mode) {
                                0L -> {
                                    opcodes[opcodes[i + 2].toInt()]
                                }
                                1L -> {
                                    opcodes[i + 2]
                                }
                                else -> {
                                    throw Exception()
                                }
                            }.toInt()
                        } else {
                            i += 3
                        }
                    }
                    6L -> {
                        val r1Mode = (modes / 1) % 10
                        val r2Mode = (modes / 10) % 10

                        if (
                            when (r1Mode) {
                                0L -> {
                                    opcodes[opcodes[i + 1].toInt()]
                                }
                                1L -> {
                                    opcodes[i + 1]
                                }
                                else -> {
                                    throw Exception()
                                }
                            } == 0L
                        ) {
                            i = when (r2Mode) {
                                0L -> {
                                    opcodes[opcodes[i + 2].toInt()]
                                }
                                1L -> {
                                    opcodes[i + 2]
                                }
                                else -> {
                                    throw Exception()
                                }
                            }.toInt()
                        } else {
                            i += 3
                        }
                    }
                    7L -> {
                        val r1Mode = (modes / 1) % 10
                        val r2Mode = (modes / 10) % 10
                        assert((modes / 100) % 10 == 0L)

                        opcodes[opcodes[i + 3].toInt()] =
                            if (
                                when (r1Mode) {
                                    0L -> {
                                        opcodes[opcodes[i + 1].toInt()]
                                    }
                                    1L -> {
                                        opcodes[i + 1]
                                    }
                                    else -> {
                                        throw Exception()
                                    }
                                } < when (r2Mode) {
                                    0L -> {
                                        opcodes[opcodes[i + 2].toInt()]
                                    }
                                    1L -> {
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
                    8L -> {
                        val r1Mode = (modes / 1) % 10
                        val r2Mode = (modes / 10) % 10
                        assert((modes / 100) % 10 == 0L)

                        opcodes[opcodes[i + 3].toInt()] =
                            if (
                                when (r1Mode) {
                                    0L -> {
                                        opcodes[opcodes[i + 1].toInt()]
                                    }
                                    1L -> {
                                        opcodes[i + 1]
                                    }
                                    else -> {
                                        throw Exception()
                                    }
                                } == when (r2Mode) {
                                    0L -> {
                                        opcodes[opcodes[i + 2].toInt()]
                                    }
                                    1L -> {
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
                    99L -> {
                        throw Exception()
                    }
                    else -> {
                        println("invalid op $op at $i")
                        throw Exception()
                    }
                }
            }

            fun foo(inputValue: Long): Long? {
//                var read = false

                while (true) {
                    val opcode = opcodes[i]
                    val op = opcode % 100
                    val modes = opcode / 100
                    when (op) {
                        1L -> {
                            val r1Mode = (modes / 1) % 10
                            val r2Mode = (modes / 10) % 10
                            assert((modes / 100) % 10 == 0L)

                            opcodes[opcodes[i + 3].toInt()] =
                                when (r1Mode) {
                                    0L -> {
                                        opcodes[opcodes[i + 1].toInt()]
                                    }
                                    1L -> {
                                        opcodes[i + 1]
                                    }
                                    else -> {
                                        throw Exception()
                                    }
                                } + when (r2Mode) {
                                    0L -> {
                                        opcodes[opcodes[i + 2].toInt()]
                                    }
                                    1L -> {
                                        opcodes[i + 2]
                                    }
                                    else -> {
                                        throw Exception()
                                    }
                                }

                            i += 4
                        }
                        2L -> {
                            val r1Mode = (modes / 1) % 10
                            val r2Mode = (modes / 10) % 10
                            assert((modes / 100) % 10 == 0L)

                            opcodes[opcodes[i + 3].toInt()] =
                                when (r1Mode) {
                                    0L -> {
                                        opcodes[opcodes[i + 1].toInt()]
                                    }
                                    1L -> {
                                        opcodes[i + 1]
                                    }
                                    else -> {
                                        throw Exception()
                                    }
                                } * when (r2Mode) {
                                    0L -> {
                                        opcodes[opcodes[i + 2].toInt()]
                                    }
                                    1L -> {
                                        opcodes[i + 2]
                                    }
                                    else -> {
                                        throw Exception()
                                    }
                                }

                            i += 4
                        }
                        3L -> {
                            //println("read")
//                            if (read) throw Exception()
//                            read = true

                            assert((modes / 1) % 10 == 0L)
                            opcodes[opcodes[i + 1].toInt()] =
                                inputValue

                            i += 2
                        }
                        4L -> {
//                            println("write")
                            val r1Mode = (modes / 1) % 10
                            val k = i
                            i += 2
                            return when (r1Mode) {
                                0L -> {
                                    opcodes[opcodes[k + 1].toInt()]
                                }
                                1L -> {
                                    opcodes[k + 1]
                                }
                                else -> {
                                    throw Exception()
                                }
                            }
                        }
                        5L -> {
                            val r1Mode = (modes / 1) % 10
                            val r2Mode = (modes / 10) % 10

                            if (
                                when (r1Mode) {
                                    0L -> {
                                        opcodes[opcodes[i + 1].toInt()]
                                    }
                                    1L -> {
                                        opcodes[i + 1]
                                    }
                                    else -> {
                                        throw Exception()
                                    }
                                } != 0L
                            ) {
                                i = when (r2Mode) {
                                    0L -> {
                                        opcodes[opcodes[i + 2].toInt()]
                                    }
                                    1L -> {
                                        opcodes[i + 2]
                                    }
                                    else -> {
                                        throw Exception()
                                    }
                                }.toInt()
                            } else {
                                i += 3
                            }
                        }
                        6L -> {
                            val r1Mode = (modes / 1) % 10
                            val r2Mode = (modes / 10) % 10

                            if (
                                when (r1Mode) {
                                    0L -> {
                                        opcodes[opcodes[i + 1].toInt()]
                                    }
                                    1L -> {
                                        opcodes[i + 1]
                                    }
                                    else -> {
                                        throw Exception()
                                    }
                                } == 0L
                            ) {
                                i = when (r2Mode) {
                                    0L -> {
                                        opcodes[opcodes[i + 2].toInt()]
                                    }
                                    1L -> {
                                        opcodes[i + 2]
                                    }
                                    else -> {
                                        throw Exception()
                                    }
                                }.toInt()
                            } else {
                                i += 3
                            }
                        }
                        7L -> {
                            val r1Mode = (modes / 1) % 10
                            val r2Mode = (modes / 10) % 10
                            assert((modes / 100) % 10 == 0L)

                            opcodes[opcodes[i + 3].toInt()] =
                                if (
                                    when (r1Mode) {
                                        0L -> {
                                            opcodes[opcodes[i + 1].toInt()]
                                        }
                                        1L -> {
                                            opcodes[i + 1]
                                        }
                                        else -> {
                                            throw Exception()
                                        }
                                    } < when (r2Mode) {
                                        0L -> {
                                            opcodes[opcodes[i + 2].toInt()]
                                        }
                                        1L -> {
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
                        8L -> {
                            val r1Mode = (modes / 1) % 10
                            val r2Mode = (modes / 10) % 10
                            assert((modes / 100) % 10 == 0L)

                            opcodes[opcodes[i + 3].toInt()] =
                                if (
                                    when (r1Mode) {
                                        0L -> {
                                            opcodes[opcodes[i + 1].toInt()]
                                        }
                                        1L -> {
                                            opcodes[i + 1]
                                        }
                                        else -> {
                                            throw Exception()
                                        }
                                    } == when (r2Mode) {
                                        0L -> {
                                            opcodes[opcodes[i + 2].toInt()]
                                        }
                                        1L -> {
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
                        99L -> {
                            return null
                        }
                        else -> {
                            println("invalid op $op at $i")
                            throw Exception()
                        }
                    }
                }

                throw Exception()
            }

            return ::foo
        }

        return (5L..9L).flatMap { phaseA ->
            (5L..9L).flatMap { phaseB ->
                if (phaseB != phaseA) {
                    (5L..9L).flatMap { phaseC ->
                        if (phaseC != phaseA && phaseC != phaseB) {
                            (5L..9L).flatMap { phaseD ->
                                if (phaseD != phaseA && phaseD != phaseB && phaseD != phaseC) {
                                    (5L..9L).flatMap { phaseE ->
                                        if (phaseE != phaseA && phaseE != phaseB && phaseE != phaseC && phaseE != phaseD) {
                                            listOf(listOf(phaseA, phaseB, phaseC, phaseD, phaseE))
                                        } else {
                                            emptyList()
                                        }
                                    }
                                } else {
                                    emptyList()
                                }
                            }
                        } else {
                            emptyList()
                        }
                    }
                } else {
                    emptyList()
                }
            }
        }.map { (phaseA, phaseB, phaseC, phaseD, phaseE) ->
            //println("$phaseA $phaseB $phaseC $phaseD $phaseE")
            val ampA = amplifier(input, phaseA)
            val ampB = amplifier(input, phaseB)
            val ampC = amplifier(input, phaseC)
            val ampD = amplifier(input, phaseD)
            val ampE = amplifier(input, phaseE)

            var e = ampE(ampD(ampC(ampB(ampA(0)!!)!!)!!)!!)!!

            while (true) {
                val a = ampA(e) ?: break
                //println("a $a")
                val b = ampB(a) ?: break
                //println("b $b")
                val c = ampC(b) ?: break
                //println("c $c")
                val d = ampD(c) ?: break
                //println("d $d")
                e = ampE(d) ?: break
            }

            e
        }.maxOf { it }
    }

    // test if implementation meets criteria from the description, like:
    //val testInput = readInput("Day07_test")
    check(part1(listOf("3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0")) == 43210L)

    check(part2(listOf("3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5")) == 139629729L)
    check(
        part2(
            listOf(
                "3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10"
            )
        ) == 18216L
    )

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
