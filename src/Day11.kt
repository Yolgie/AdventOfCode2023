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

    fun part2(input: List<String>): Int {
        val expandedInput = input.expandUniverse(1000000)
        val galaxies = expandedInput.getGalaxies()
        val distances = galaxies.distances()

//        expandedInput.forEach { println(it) }
//        println(galaxies)

        return distances.flatten().sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${"%02d".format(day)}_test")
    check(part1(testInput) == 374) { "Part 1 failed with ${part1(testInput)}" }
//    check(part2(testInput) == 8410) { "Part 2 failed with ${part2(testInput)}" }

    val input = readInput("Day${"%02d".format(day)}")
    part1(input).println()
    part2(input).println()
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

private fun Pair<Int, Int>.manhattanDistance(other: Pair<Int, Int>): Int {
    return kotlin.math.abs(this.first - other.first) + kotlin.math.abs(this.second - other.second)
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
