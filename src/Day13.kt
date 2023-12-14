fun main() {
    val day = 13

    fun part1(input: List<String>): Int {
        return input
            .splitBy("")
            .sumOf { field ->
                // notes -> hashCode was not necessary, but also does not impede
                val verticalSymmetry = field.transposeAndHash()
                    .getLineOfSymmetry()
                    ?.let { it }
                    ?: 0
                val horizontalSymmetry = field.map { it.hashCode() }
                    .getLineOfSymmetry()
                    ?.let { (it) * 100 }
                    ?: 0
                verticalSymmetry + horizontalSymmetry
            }

        // after part 2 i tried part1 again with the new methods:
        //return input
        //    .splitBy("")
        //    .sumOf { field ->
        //        val verticalSymmetry = field.transpose()
        //            .getAllLinesOfSymmetry()
        //            .singleOrNull()
        //            ?.let { it }
        //            ?: 0
        //        val horizontalSymmetry = field
        //            .getAllLinesOfSymmetry()
        //            .singleOrNull()
        //            ?.let { (it) * 100 }
        //            ?: 0
        //        println(verticalSymmetry to horizontalSymmetry)
        //        verticalSymmetry + horizontalSymmetry
        //    }
    }

    fun part2(input: List<String>): Int {
        return input
            .splitBy("")
            .sumOf { field ->
                val verticalSymmetry = field.transpose()
                    .getAllLinesOfSymmetry(1, true)
                    .singleOrNull()
                    ?.let { it }
                    ?: 0
                val horizontalSymmetry = field
                    .getAllLinesOfSymmetry(1, true)
                    .singleOrNull()
                    ?.let { (it) * 100 }
                    ?: 0
                println(verticalSymmetry to horizontalSymmetry)
                verticalSymmetry + horizontalSymmetry
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${"%02d".format(day)}_test")
    val input = readInput("Day${"%02d".format(day)}")

    val testPart1 = part1(testInput)
    check(testPart1 == 405) { "Part 1 failed with $testPart1" }
    measureTime("Part1 ") { part1(input).println() }

    val testPart2 = part2(testInput)
    check(testPart2 == 400) { "Part 2 failed with $testPart2" }
    measureTime("Part2 ") { part2(input).println() }
}

fun List<String>.transposeAndHash(): List<Int> =
    this.first().indices.map { index ->
        this.map { it.elementAt(index) }.hashCode()
    }

fun <T> List<T>.getLineOfSymmetry(): Int? =
    this.findAllDirectRepetitions()
        .filter { this.verifyLineOfSymmetry(it) }
        .singleOrNull()

fun <T> List<T>.verifyLineOfSymmetry(startOfSymmetry: Int): Boolean =
    (0..startOfSymmetry - 1).all {
        val down = this.getOrNull(startOfSymmetry - 1 - it)
        val up = this.getOrNull(startOfSymmetry + it)
        down == null || up == null || down == up
    }

fun <T> Iterable<T>.findAllDirectRepetitions() =
    this.zipWithNext()
        .mapIndexedNotNull { index, pair -> if (pair.equals()) index + 1 else null }

fun List<String>.getAllLinesOfSymmetry(
    sumOfHammingDistancesThreshold: Int = 0,
    hammingDistanceMustBeExact: Boolean = true
): List<Int> {
    return (1..<this.size)
        .mapNotNull { startOfSymmetry ->
            val sumOfHammingDistances = (0..<startOfSymmetry).mapNotNull {
                val down = this.getOrNull(startOfSymmetry - 1 - it)
                val up = this.getOrNull(startOfSymmetry + it)
                if (up == null || down == null) null
                else up.hammingDistance(down)
            }.sum()
            if (sumOfHammingDistances > sumOfHammingDistancesThreshold) null
            else if (hammingDistanceMustBeExact && sumOfHammingDistances != sumOfHammingDistancesThreshold) null
            else startOfSymmetry
        }
}

