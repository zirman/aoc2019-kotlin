fun main() {
    fun part1(input: List<String>): Int {
        val map = input.associate { line ->
            val groupValues = Regex("""(\w+)\)(\w+)""").matchEntire(line)!!.groupValues
            Pair(groupValues[2], groupValues[1])
        }

        tailrec fun depth(s: String, acc: Int): Int {
            val q = map[s]
            return if (q != null) depth(q, acc + 1) else acc + 0
        }

        return map.keys.sumOf { depth(it, 0) }
    }

    fun part2(input: List<String>): Int {
        val map = input.associate { line ->
            val groupValues = Regex("""(\w+)\)(\w+)""").matchEntire(line)!!.groupValues
            Pair(groupValues[2], groupValues[1])
        }

        tailrec fun orbiting(s: String, acc: Set<String>): Set<String> {
            val q = map[s]
            return if (q != null) orbiting(q, acc.plus(q)) else acc
        }

        fun foo(s: String, acc: Set<String>): Int =
            if (acc.contains(s)) 0 else foo(map[s]!!, acc) + 1

        return foo(map["YOU"]!!, orbiting("SAN", emptySet())) + foo(map["SAN"]!!, orbiting("YOU", emptySet()))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 42)
    check(part2(readInput("Day06_test2")) == 4)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
