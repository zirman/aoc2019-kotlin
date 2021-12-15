import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.absoluteValue
import kotlin.math.min

sealed class LinkedList<out T> {
    object End : LinkedList<Nothing>()
    data class Link<T>(val head: T, val tail: LinkedList<T>) : LinkedList<T>()
}

tailrec fun <T> LinkedList<T>.contains(v: T): Boolean =
    when (this) {
        is LinkedList.End -> false
        is LinkedList.Link ->
            if (head == v) {
                true
            } else {
                tail.contains(v)
            }
    }

fun <T> LinkedList<T>.cons(v: T): LinkedList<T> =
    LinkedList.Link(v, this)

inline fun <T> LinkedList<T>.forEach(f: (T) -> Unit) {
    var x = this
    while (x is LinkedList.Link) {
        f(x.head)
        x = x.tail
    }
}

fun Boolean.toUnitOrNull(): Unit? =
    if (this) Unit else null

fun main() {
    fun part1(input: List<String>): Int {
        val channelIn = Channel<Long>()
        val channelOut = Channel<Long>()

        return runBlocking(Dispatchers.Default) {
            val intCodeMachineJob = launch {
                suspend fun machine(input: List<String>, read: suspend () -> Long, write: suspend (Long) -> Unit) {
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

                machine(
                    input = input,
                    read = { channelOut.receive() },
                    write = { value -> channelIn.send(value) }
                )
            }


            val tiles = mutableMapOf<Pair<Int, Int>, Int>(Pair(0, 0) to 1)

            var x = 0
            var y = 0

            suspend fun north(): Boolean {
                channelOut.send(1)
                return when (channelIn.receive()) {
                    0L -> {
                        tiles[Pair(x, y - 1)] = 0
                        false
                    }
                    1L -> {
                        y--
                        tiles[Pair(x, y)] = 1
                        true
                    }
                    2L -> {
                        y--
                        tiles[Pair(x, y)] = 2
                        true
                    }
                    else -> {
                        throw Exception()
                    }
                }
            }

            suspend fun south(): Boolean {
                channelOut.send(2)
                return when (channelIn.receive()) {
                    0L -> {
                        tiles[Pair(x, y + 1)] = 0
                        false
                    }
                    1L -> {
                        y++
                        tiles[Pair(x, y)] = 1
                        true
                    }
                    2L -> {
                        y++
                        tiles[Pair(x, y)] = 2
                        true
                    }
                    else -> {
                        throw Exception()
                    }
                }
            }

            suspend fun west(): Boolean {
                channelOut.send(3)
                return when (channelIn.receive()) {
                    0L -> {
                        tiles[Pair(x - 1, y)] = 0
                        false
                    }
                    1L -> {
                        x--
                        tiles[Pair(x, y)] = 1
                        true
                    }
                    2L -> {
                        x--
                        tiles[Pair(x, y)] = 2
                        true
                    }
                    else -> {
                        throw Exception()
                    }
                }
            }

            suspend fun east(): Boolean {
                channelOut.send(4)
                return when (channelIn.receive()) {
                    0L -> {
                        tiles[Pair(x + 1, y)] = 0
                        false
                    }
                    1L -> {
                        x++
                        tiles[Pair(x, y)] = 1
                        true
                    }
                    2L -> {
                        x++
                        tiles[Pair(x, y)] = 2
                        true
                    }
                    else -> {
                        throw Exception()
                    }
                }
            }

            // searches for nearest unknown square using known open tiles.
            fun findUnknownTile(): Pair<Int, Int>? {
                val visitedSet = mutableSetOf<Pair<Int, Int>>()
                val currentSet = mutableSetOf(Pair(x, y))
                val nextSet = mutableSetOf<Pair<Int, Int>>()

                fun checkTile(position: Pair<Int, Int>): Pair<Int, Int>? {
                    if (visitedSet.contains(position).not()) {
                        visitedSet.add(position)

                        when (tiles[position]) {
                            null -> {
                                return position
                            }
                            1, 2 -> {
                                val (x, y) = position
                                nextSet.add(Pair(x - 1, y))
                                nextSet.add(Pair(x + 1, y))
                                nextSet.add(Pair(x, y - 1))
                                nextSet.add(Pair(x, y + 1))
                            }
                        }
                    }

                    return null
                }

                while (true) {
                    currentSet.forEach { checkTile(it)?.let { return it } }
                    if (nextSet.isEmpty()) {
                        return null
                    }
                    currentSet.clear()
                    currentSet.addAll(nextSet)
                    nextSet.clear()
                }
            }

            // moves robot to the tile taking only known open tiles
            suspend fun moveToTile(target: Pair<Int, Int>) {
                fun manhattanDistance(p: Pair<Int, Int>): Int {
                    return (p.first - target.first).absoluteValue +
                            (p.second - target.second).absoluteValue
                }

                val visited = mutableSetOf<Pair<Int, Int>>()

                fun planRoute(src: Pair<Int, Int>): LinkedList<suspend () -> Boolean>? {
                    if (src == target) {
                        return LinkedList.End
                    }

                    if (visited.contains(src)) {
                        return null
                    }

                    visited.add(src)

                    return when (tiles[src]) {
                        0 -> null
                        1, 2 -> {
                            val (x, y) = src

                            listOf(
                                Pair(manhattanDistance(Pair(x, y - 1))) {
                                    planRoute(Pair(x, y - 1))?.cons { north() }
                                },
                                Pair(manhattanDistance(Pair(x, y + 1))) {
                                    planRoute(Pair(x, y + 1))?.cons { south() }
                                },
                                Pair(manhattanDistance(Pair(x - 1, y))) {
                                    planRoute(Pair(x - 1, y))?.cons { west() }
                                },
                                Pair(manhattanDistance(Pair(x + 1, y))) {
                                    planRoute(Pair(x + 1, y))?.cons { east() }
                                }
                            )
                                .asSequence()
                                .sortedBy { (d, _) -> d }
                                .firstNotNullOfOrNull { (_, b) -> b() }
                        }
                        else -> {
                            throw Exception()
                        }
                    }
                }

                planRoute(Pair(x, y))!!.forEach { it() }
            }

            while (true) {
                moveToTile(findUnknownTile() ?: break)
            }

            fun findFoo(): Int {
                val visitedSet = mutableSetOf<Pair<Int, Int>>()
                val currentSet = mutableSetOf(Pair(0, 0))
                val nextSet = mutableSetOf<Pair<Int, Int>>()
                var distance = 0

                fun checkTile(position: Pair<Int, Int>): Pair<Int, Int>? {
                    if (visitedSet.contains(position).not()) {
                        visitedSet.add(position)

                        when (tiles[position]) {
                            null -> {
                                return null
                            }
                            1 -> {
                                val (x, y) = position
                                nextSet.add(Pair(x - 1, y))
                                nextSet.add(Pair(x + 1, y))
                                nextSet.add(Pair(x, y - 1))
                                nextSet.add(Pair(x, y + 1))
                            }
                            2 -> {
                                return position
                            }
                        }
                    }

                    return null
                }

                while (true) {
                    currentSet.forEach {
                        checkTile(it)?.let {
                            return distance
                        }
                    }
                    if (nextSet.isEmpty()) {
                        throw Exception()
                    }
                    currentSet.clear()
                    currentSet.addAll(nextSet)
                    nextSet.clear()
                    distance++
                }
            }

//            val minX = tiles.minByOrNull { (position, _) -> position.first }!!.key.first
//            val maxX = tiles.maxByOrNull { (position, _) -> position.first }!!.key.first
//            val minY = tiles.minByOrNull { (position, _) -> position.second }!!.key.second
//            val maxY = tiles.maxByOrNull { (position, _) -> position.second }!!.key.second
//
//            println(
//                (minY..maxY).joinToString("\n") { k ->
//                    (minX..maxX).joinToString("") { j ->
//                        if (j == x && k == y) {
//                            "@"
//                        } else {
//                            when (tiles[Pair(j, k)]) {
//                                null -> " "
//                                0 -> "#"
//                                1 -> "."
//                                2 -> "X"
//                                else -> {
//                                    throw Exception()
//                                }
//                            }
//                        }
//                    }
//                }
//            )

            intCodeMachineJob.cancel()
            findFoo()
        }
    }

    fun part2(input: List<String>): Int {
        val channelIn = Channel<Long>()
        val channelOut = Channel<Long>()

        return runBlocking(Dispatchers.Default) {
            val intCodeMachineJob = launch {
                suspend fun machine(input: List<String>, read: suspend () -> Long, write: suspend (Long) -> Unit) {
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

                machine(
                    input = input,
                    read = { channelOut.receive() },
                    write = { value -> channelIn.send(value) }
                )
            }


            val tiles = mutableMapOf<Pair<Int, Int>, Int>(Pair(0, 0) to 1)

            var x = 0
            var y = 0

            suspend fun north(): Boolean {
                channelOut.send(1)
                return when (channelIn.receive()) {
                    0L -> {
                        tiles[Pair(x, y - 1)] = 0
                        false
                    }
                    1L -> {
                        y--
                        tiles[Pair(x, y)] = 1
                        true
                    }
                    2L -> {
                        y--
                        tiles[Pair(x, y)] = 2
                        true
                    }
                    else -> {
                        throw Exception()
                    }
                }
            }

            suspend fun south(): Boolean {
                channelOut.send(2)
                return when (channelIn.receive()) {
                    0L -> {
                        tiles[Pair(x, y + 1)] = 0
                        false
                    }
                    1L -> {
                        y++
                        tiles[Pair(x, y)] = 1
                        true
                    }
                    2L -> {
                        y++
                        tiles[Pair(x, y)] = 2
                        true
                    }
                    else -> {
                        throw Exception()
                    }
                }
            }

            suspend fun west(): Boolean {
                channelOut.send(3)
                return when (channelIn.receive()) {
                    0L -> {
                        tiles[Pair(x - 1, y)] = 0
                        false
                    }
                    1L -> {
                        x--
                        tiles[Pair(x, y)] = 1
                        true
                    }
                    2L -> {
                        x--
                        tiles[Pair(x, y)] = 2
                        true
                    }
                    else -> {
                        throw Exception()
                    }
                }
            }

            suspend fun east(): Boolean {
                channelOut.send(4)
                return when (channelIn.receive()) {
                    0L -> {
                        tiles[Pair(x + 1, y)] = 0
                        false
                    }
                    1L -> {
                        x++
                        tiles[Pair(x, y)] = 1
                        true
                    }
                    2L -> {
                        x++
                        tiles[Pair(x, y)] = 2
                        true
                    }
                    else -> {
                        throw Exception()
                    }
                }
            }

            // searches for nearest unknown square using known open tiles.
            fun findUnknownTile(): Pair<Int, Int>? {
                val visitedSet = mutableSetOf<Pair<Int, Int>>()
                val currentSet = mutableSetOf(Pair(x, y))
                val nextSet = mutableSetOf<Pair<Int, Int>>()

                fun checkTile(position: Pair<Int, Int>): Pair<Int, Int>? {
                    if (visitedSet.contains(position).not()) {
                        visitedSet.add(position)

                        when (tiles[position]) {
                            null -> {
                                return position
                            }
                            1, 2 -> {
                                val (x, y) = position
                                nextSet.add(Pair(x - 1, y))
                                nextSet.add(Pair(x + 1, y))
                                nextSet.add(Pair(x, y - 1))
                                nextSet.add(Pair(x, y + 1))
                            }
                        }
                    }

                    return null
                }

                while (true) {
                    currentSet.forEach { checkTile(it)?.let { return it } }
                    if (nextSet.isEmpty()) {
                        return null
                    }
                    currentSet.clear()
                    currentSet.addAll(nextSet)
                    nextSet.clear()
                }
            }

            // moves robot to the tile taking only known open tiles
            suspend fun moveToTile(target: Pair<Int, Int>) {
                fun manhattanDistance(p: Pair<Int, Int>): Int {
                    return (p.first - target.first).absoluteValue +
                            (p.second - target.second).absoluteValue
                }

                val visited = mutableSetOf<Pair<Int, Int>>()

                fun planRoute(src: Pair<Int, Int>): LinkedList<suspend () -> Boolean>? {
                    if (src == target) {
                        return LinkedList.End
                    }

                    if (visited.contains(src)) {
                        return null
                    }

                    visited.add(src)

                    return when (tiles[src]) {
                        0 -> null
                        1, 2 -> {
                            val (x, y) = src

                            listOf(
                                Pair(manhattanDistance(Pair(x, y - 1))) {
                                    planRoute(Pair(x, y - 1))?.cons { north() }
                                },
                                Pair(manhattanDistance(Pair(x, y + 1))) {
                                    planRoute(Pair(x, y + 1))?.cons { south() }
                                },
                                Pair(manhattanDistance(Pair(x - 1, y))) {
                                    planRoute(Pair(x - 1, y))?.cons { west() }
                                },
                                Pair(manhattanDistance(Pair(x + 1, y))) {
                                    planRoute(Pair(x + 1, y))?.cons { east() }
                                }
                            )
                                .asSequence()
                                .sortedBy { (d, _) -> d }
                                .firstNotNullOfOrNull { (_, b) -> b() }
                        }
                        else -> {
                            throw Exception()
                        }
                    }
                }

                planRoute(Pair(x, y))!!.forEach { it() }
            }

            while (true) {
                moveToTile(findUnknownTile() ?: break)
            }

            fun findFoo(): Int {
                val visitedSet = mutableSetOf<Pair<Int, Int>>()
                val currentSet = mutableSetOf(tiles.firstNotNullOf { (k, v) -> if (v == 2) k else null })
                val nextSet = mutableSetOf<Pair<Int, Int>>()
                var distance = 0

                fun checkTile(position: Pair<Int, Int>) {
                    if (visitedSet.contains(position).not()) {
                        visitedSet.add(position)

                        when (tiles[position]) {
                            null -> {}
                            1, 2 -> {
                                val (x, y) = position
                                nextSet.add(Pair(x - 1, y))
                                nextSet.add(Pair(x + 1, y))
                                nextSet.add(Pair(x, y - 1))
                                nextSet.add(Pair(x, y + 1))
                            }
                        }
                    }
                }

                while (true) {
                    currentSet.forEach { checkTile(it) }
                    if (nextSet.isEmpty()) {
                        return distance - 1
                    }
                    currentSet.clear()
                    currentSet.addAll(nextSet)
                    nextSet.clear()
                    distance++
                }
            }

//            val minX = tiles.minByOrNull { (position, _) -> position.first }!!.key.first
//            val maxX = tiles.maxByOrNull { (position, _) -> position.first }!!.key.first
//            val minY = tiles.minByOrNull { (position, _) -> position.second }!!.key.second
//            val maxY = tiles.maxByOrNull { (position, _) -> position.second }!!.key.second
//
//            println(
//                (minY..maxY).joinToString("\n") { k ->
//                    (minX..maxX).joinToString("") { j ->
//                        if (j == x && k == y) {
//                            "@"
//                        } else {
//                            when (tiles[Pair(j, k)]) {
//                                null -> " "
//                                0 -> "#"
//                                1 -> "."
//                                2 -> "X"
//                                else -> {
//                                    throw Exception()
//                                }
//                            }
//                        }
//                    }
//                }
//            )

            intCodeMachineJob.cancel()
            findFoo()
        }
    }

    // test if implementation meets criteria from the description, like:
    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
