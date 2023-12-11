fun main() {
    val day = 0

    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${"%02d".format(day)}_test")
    val input = readInput("Day${"%02d".format(day)}")

    val testPart1 = part1(testInput)
    check(testPart1 == 0) { "Part 1 failed with $testPart1" }
    part1(input).println()

    val testPart2 = part2(testInput)
    check(testPart2 == 0) { "Part 2 failed with $testPart2" }
    part2(input).println()
}
