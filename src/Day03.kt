fun main() {
    val day = 3

    fun part1(input: List<String>): Int {
        return input.flatMapIndexed { y, line ->
            val numberPositions = line.getIndicesOfFirstDigitsOfNumbersInString()
            val numbers = numberPositions.map { x -> input.getNumberStartingAtCoordinate(x, y)!! }
            val borders = numberPositions.map { x -> input.getBorderOfNumberStartingAtCoordinate(x, y) }
            borders.mapIndexed { index, border -> if (border.any { it != '.' }) numbers[index] else 0 }
        }.sum()

        // parse in map
        // go through map line by line
        // - first extract all numbers
        // - for each number get a set of all surrounding symbols with line-1,line+1 and indexrange for the length of the number
        // -- plus the symbol immediately after and before
        // - check this set for non . and then add to the sum, else 0
    }

    fun part2(input: List<String>): Int {
        val possibleGears = input.findAllCoordinatesFor('*')
        println(possibleGears)
        val borders = possibleGears.map { (x, y) -> input.getBorderOfCoordinate(x, y) }
        println(borders)
        val gears =
            borders.map { border -> border.joinToString("").toIntList("\\D".toRegex()).size }
                .mapIndexedNotNull { index, neighborcount -> if (neighborcount == 2) possibleGears[index] else null }
        println(gears)
        val borderNumbers = gears.map { (x, y) -> input.getBorderTouchingNumbers(x, y) }
        println(borderNumbers)
        check(borderNumbers.all { it.size == 2 }) { "No 2 distinct numbers detected, todo duplicate number"}
        return borderNumbers.sumOf { it.multiply() }

//        return input.flatMapIndexed { y, line ->
//
//
//            val numberPositions = line.getIndicesOfFirstDigitsOfNumbersInString()
//            println(numberPositions)
//            val numbers = numberPositions.map { x -> input.getNumberStartingAtCoordinate(x, y)!! }
//            println(numbers)
//            val borders = numberPositions.map { x -> input.getBorderOfNumberStartingAtCoordinate(x, y) }
//            println(borders)
//            borders.mapIndexed { index, border -> if (border.any { it != '.' }) numbers[index] else 0 }
//        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${"%02d".format(day)}_test")
    check(part1(testInput) == 4361) { "Part 1 failed with ${part1(testInput)}" }
    check(part2(testInput) == 467835) { "Part 2 failed with ${part2(testInput)}" }

    val input = readInput("Day${"%02d".format(day)}")
    part1(input).println()
    part2(input).println()
}

fun List<String>.getBorderTouchingNumbers(x: Int, y: Int): Set<Int> {
    val borderCoordinates = listOf(
        x - 1 to y - 1,
        x to y - 1,
        x + 1 to y - 1,
        x + 1 to y,
        x + 1 to y + 1,
        x to y + 1,
        x - 1 to y + 1,
        x - 1 to y
    )
    return borderCoordinates.mapNotNull { (x, y) -> this.getNumberTouchingCoordinate(x, y) }.toSet()
}

fun List<String>.getBorderOfCoordinate(x: Int, y: Int): List<Char> {
    val result = mutableListOf<Char>()
    this.getOrNull(y - 1)?.getOrNull(x - 1)?.let { result.add(it) }
    this.getOrNull(y - 1)?.getOrNull(x)?.let { result.add(it) }
    this.getOrNull(y - 1)?.getOrNull(x + 1)?.let { result.add(it) }
    this.getOrNull(y)?.getOrNull(x + 1)?.let { result.add(it) }
    this.getOrNull(y + 1)?.getOrNull(x + 1)?.let { result.add(it) }
    this.getOrNull(y + 1)?.getOrNull(x)?.let { result.add(it) }
    this.getOrNull(y + 1)?.getOrNull(x - 1)?.let { result.add(it) }
    this.getOrNull(y)?.getOrNull(x - 1)?.let { result.add(it) }
    return result
}

fun List<String>.findAllCoordinatesFor(target: Char): List<Pair<Int, Int>> {
    return this.flatMapIndexed { y, line ->
        line.foldIndexed(emptyList()) { x, coordinates, currentChar ->
            if (currentChar == target) coordinates + (x to y)
            else coordinates
        }
    }
}

fun List<String>.getNumberStartingAtCoordinate(x: Int, y: Int): Int? =
    this[y].drop(x)
        .takeWhile { it.isDigit() }
        .toIntOrNull()

fun List<String>.getNumberTouchingCoordinate(x: Int, y: Int): Int? =
    this.getStartOfNumberAtCoordinate(x, y)
        ?.let { startingX -> this.getNumberStartingAtCoordinate(startingX, y) }

fun List<String>.getStartOfNumberAtCoordinate(x: Int, y: Int): Int? =
    generateSequence(x) { it - 1 }
        .takeWhile { this.getOrNull(y)?.getOrNull(it)?.isDigit() ?: false }
        .lastOrNull()

fun List<String>.getBorderOfNumberStartingAtCoordinate(x: Int, y: Int): Set<Char> {
    val xEnd = x + this.getNumberStartingAtCoordinate(x, y).toString().length - 1
    val result = mutableSetOf<Char>()
    this.getOrNull(y - 1)?.getOrNull(x - 1)?.let { result.add(it) }
    this.getOrNull(y - 1)?.getOrNull(xEnd + 1)?.let { result.add(it) }
    this.getOrNull(y)?.getOrNull(x - 1)?.let { result.add(it) }
    this.getOrNull(y)?.getOrNull(xEnd + 1)?.let { result.add(it) }
    this.getOrNull(y + 1)?.getOrNull(x - 1)?.let { result.add(it) }
    this.getOrNull(y + 1)?.getOrNull(xEnd + 1)?.let { result.add(it) }
    this.getOrNull(y - 1)?.subSequence(x, xEnd + 1)?.toSet()?.let { result.addAll(it) }
    this.getOrNull(y + 1)?.subSequence(x, xEnd + 1)?.toSet()?.let { result.addAll(it) }
    return result
}

fun String.getIndicesOfFirstDigitsOfNumbersInString(): List<Int> {
    return this.foldIndexed(emptyList<Int>() to false) { index, (indecesList, numberDetected), currentChar ->
        when {
            currentChar.isDigit() && !numberDetected -> (indecesList + index to true) // add new first digit
            currentChar.isDigit() -> (indecesList to true) // currently in a number
            else -> (indecesList to false) // outside of a number
        }
    }.first
}