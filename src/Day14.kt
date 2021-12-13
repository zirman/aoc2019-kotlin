fun main() {
    fun part1(input: List<String>): Long {
        val regex = Regex("""(\d+) ([A-Z]+)""")
        val rx = mutableMapOf<String, Pair<Long, List<Pair<Long, String>>>>()

        input.map { line ->
            val cols = line.split(" => ")
            val groupValues = regex.matchEntire(cols[1])!!.groupValues

            rx[groupValues[2]] = Pair(
                groupValues[1].toLong(),
                cols[0].split(", ").map { component ->
                    val gv = regex.matchEntire(component)!!.groupValues
                    Pair(
                        gv[1].toLong(),
                        gv[2]
                    )
                }
            )
        }

        fun recur(cmp: String, amt: Long, with: Map<String, Long>): Pair<Long, Map<String, Long>> {
            val availableAmt = with[cmp] ?: 0

            return when {
                // base case is just ore
                cmp == "ORE" -> Pair(amt, with)
                // or, we have some leftovers we can use
                availableAmt >= amt -> Pair(0, with.plus(Pair(cmp, availableAmt - amt)))
                // else we need more precursors
                else -> {
                    val (amtMore, precursors) = rx[cmp]!!
                    val factor = ((amt - availableAmt) + amtMore - 1) / amtMore

                    precursors.fold(
                        recur(
                            cmp,
                            amt,
                            with.plus(Pair(cmp, availableAmt + (amtMore * factor)))
                        )
                    ) { (oreAcc, withAcc), (precursorAmt, precursor) ->
                        val (ore, withAcc2) = recur(precursor, precursorAmt * factor, withAcc)
                        Pair(oreAcc + ore, withAcc2)
                    }
                }
            }
        }

        return recur("FUEL", 1, emptyMap()).first
    }

    fun part2(input: List<String>): Long {
        val regex = Regex("""(\d+) ([A-Z]+)""")
        val rx = mutableMapOf<String, Pair<Long, List<Pair<Long, String>>>>()

        input.map { line ->
            val cols = line.split(" => ")
            val groupValues = regex.matchEntire(cols[1])!!.groupValues

            rx[groupValues[2]] = Pair(
                groupValues[1].toLong(),
                cols[0].split(", ").map { component ->
                    val gv = regex.matchEntire(component)!!.groupValues
                    Pair(
                        gv[1].toLong(),
                        gv[2]
                    )
                }
            )
        }

        fun recur(cmp: String, amt: Long, with: Map<String, Long>): Pair<Long, Map<String, Long>> {
            val availableAmt = with[cmp] ?: 0

            return when {
                // base case is just ore
                cmp == "ORE" -> Pair(amt, with)
                // or, we have some leftovers we can use
                availableAmt >= amt -> Pair(0, with.plus(Pair(cmp, availableAmt - amt)))
                // else we need more precursors
                else -> {
                    val (amtMore, precursors) = rx[cmp]!!
                    val factor = ((amt - availableAmt) + amtMore - 1) / amtMore

                    precursors.fold(
                        recur(
                            cmp,
                            amt,
                            with.plus(Pair(cmp, availableAmt + (amtMore * factor)))
                        )
                    ) { (oreAcc, withAcc), (precursorAmt, precursor) ->
                        val (ore, withAcc2) = recur(precursor, precursorAmt * factor, withAcc)
                        Pair(oreAcc + ore, withAcc2)
                    }
                }
            }
        }

        val stash = 1_000_000_000_000

        // get upper estimate
        var estimate = 1L
        while (recur("FUEL", estimate, emptyMap()).first < stash) {
            estimate *= 2
        }

        tailrec fun binarySearch(min: Long, max: Long): Long {
            val mid = (max + min) / 2
            val oreMin = recur("FUEL", mid, emptyMap()).first
            val oreMax = recur("FUEL", mid + 1, emptyMap()).first

            return if (stash in oreMin until oreMax) {
                mid
            } else if (oreMin < stash) {
                binarySearch(mid, max)
            } else if (oreMin > stash) {
                binarySearch(min, mid)
            } else {
                throw Exception()
            }
        }

        return binarySearch(1, estimate)
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day14_test")) == 31L)
    check(part1(readInput("Day14_test2")) == 165L)
    check(part1(readInput("Day14_test3")) == 13312L)
    check(part1(readInput("Day14_test4")) == 180697L)
    check(part1(readInput("Day14_test5")) == 2210736L)
    check(part2(readInput("Day14_test3")) == 82892753L)
    check(part2(readInput("Day14_test4")) == 5586022L)
    check(part2(readInput("Day14_test5")) == 460664L)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
