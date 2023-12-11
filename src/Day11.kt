import java.math.BigInteger

val galaxyChar = '#'
val spaceChar = '.'
val expansionChar = 'x'

fun main() {
    val day = 11

    fun part1(input: List<String>): Int {
        val expandedInput = input.expandUniverse()
        val galaxies = expandedInput.getGalaxies()
        val distances = galaxies.distances()

//        expandedInput.forEach { println(it) }
//        println(galaxies)

        return distances.flatten().sum()
    }

    fun convertToWeightedPathSegments(
        manhattanPath: Sequence<Pair<Int, Int>>,
        expandedInput: List<String>,
        factor: Int,
        coordinate: Pair<Int, Int>
    ) = manhattanPath.map(expandedInput::get)
        .map { symbol ->
            when (symbol) {
                galaxyChar, spaceChar -> 1
                expansionChar -> factor
                else -> throw Exception("Invalid Character in map: $symbol at $coordinate")
            }
        }

    fun part2(input: List<String>, factor: Int): BigInteger {
        val expandedInput = input.expandUniverseWithPlaceholder()
        val galaxies = expandedInput.getGalaxies()
        val distances = galaxies.mapIndexed { index, coordinate ->
            val otherGalaxies = galaxies.drop(index + 1)
            val distances =
                otherGalaxies.map { other -> getManhattanPathSequence(coordinate, other) }
                    .map { manhattanPath -> convertToWeightedPathSegments(manhattanPath, expandedInput, factor, coordinate)}
                    .map { it.map { it.toBigInteger() } }
                    .map(Sequence<BigInteger>::sum)
                    .map { it - BigInteger.ONE }
//            println("$coordinate -> $otherGalaxies")
//            println("$coordinate -> $distances")
            distances
        }

//        expandedInput.forEach { println(it) }
//        println(galaxies)

        return distances.flatten().sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${"%02d".format(day)}_test")
    check(part1(testInput) == 374) { "Part 1 failed with ${part1(testInput)}" }
    check(part2(testInput, 2) == (374).toBigInteger()) { "Part 2 failed with ${part2(testInput, 2)}" }
    check(part2(testInput, 10) == (1030).toBigInteger()) { "Part 2 failed with ${part2(testInput, 10)}" }
    check(part2(testInput, 100) == (8410).toBigInteger()) { "Part 2 failed with ${part2(testInput, 100)}" }

    val input = readInput("Day${"%02d".format(day)}")
    part1(input).println()
    part2(input, 1000000).println()
}

private fun List<Pair<Int, Int>>.distances(): List<List<Int>> {
    return this.mapIndexed { index, coordinate ->
        val otherGalaxies = this.drop(index + 1)
        val distances = otherGalaxies.map { other -> coordinate.manhattanDistance(other) }
//        println("$coordinate -> $otherGalaxies")
//        println("$coordinate -> $distances")
        distances
    }
}

private fun List<String>.getGalaxies(): List<Pair<Int, Int>> {
    val galaxies = mutableListOf<Pair<Int, Int>>()
    this.forEachIndexed { y, line ->
        line.forEachIndexed { x, symbol ->
            if (symbol == '#')
                galaxies.add(x to y)
        }
    }
    return galaxies
}

private fun List<String>.expandUniverse(factor: Int = 2): List<String> {
    val expandedRowsInput = this.flatMap { line ->
        if (line.all { it == '.' }) List(factor) { line }
        else listOf(line)
    }
    val expandedInput = expandedRowsInput.map { mutableListOf<Char>() }
    expandedRowsInput.first().forEachIndexed { x, _ ->
        if (expandedRowsInput.map { it[x] }.all { it == '.' }) {
            expandedRowsInput.forEachIndexed { y, line ->
                repeat(factor) { expandedInput[y].add('.') }
            }
        } else {
            expandedRowsInput.forEachIndexed { y, line ->
                expandedInput[y].add(expandedRowsInput[y][x])
            }
        }
    }
    return expandedInput.map { it.joinToString("") }
}

private fun List<String>.expandUniverseWithPlaceholder(): List<String> {
    val expansionLine = expansionChar.toString().repeat(this.first().length)
    val expandedRowsInput = this.map { line ->
        if (line.all { it == spaceChar || it == expansionChar }) expansionLine
        else line
    }
    val expandedInput = expandedRowsInput.map { mutableListOf<Char>() }
    expandedRowsInput.first().forEachIndexed { x, _ ->
        if (expandedRowsInput.map { it[x] }.all { it == spaceChar || it == expansionChar }) {
            expandedRowsInput.forEachIndexed { y, line -> expandedInput[y].add(expansionChar) }
        } else {
            expandedRowsInput.forEachIndexed { y, line -> expandedInput[y].add(expandedRowsInput[y][x]) }
        }
    }
    return expandedInput.map { it.joinToString("") }
}
