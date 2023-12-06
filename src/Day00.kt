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
    check(part1(testInput) == 0) { "Part 1 failed with ${part1(testInput)}" }
//    check(part2(testInput) == 30) { "Part 2 failed with ${part2(testInput)}" }

    val input = readInput("Day${"%02d".format(day)}")
    part1(input).println()
    part2(input).println()
}
