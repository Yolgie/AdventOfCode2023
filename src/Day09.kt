import java.math.BigInteger

fun main() {
    val day = 9

    fun generateNextLevel(values: Sequence<BigInteger>): Sequence<BigInteger>? {
        val nextLevel = values.zipWithNext { a, b -> (b - a) }
        return if (nextLevel.all { it == BigInteger.ZERO }) null
        else nextLevel
    }

    fun part1(input: List<String>): BigInteger {
        val result = input.sumOf { line ->
            val seed = line.toBigIntegerSeqence()

            generateSequence(seed, ::generateNextLevel)
                .sumOf(Sequence<BigInteger>::last)
        }
        return result
    }

    fun part2(input: List<String>): BigInteger {
        val result = input.sumOf { line ->
            val seed = line.toBigIntegerSeqence()

            generateSequence(seed, ::generateNextLevel)
                .map(Sequence<BigInteger>::first)
                .toList()
                .reversed()
                .reduce { acc, current -> current - acc }
        }
        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${"%02d".format(day)}_test")
    val input = readInput("Day${"%02d".format(day)}")

    val testPart1 = part1(testInput)
    check(testPart1 == (114).toBigInteger()) { "Part 1 failed with $testPart1" }
    part1(input).println()

    val testPart2 = part2(testInput)
    check(testPart2 == (2).toBigInteger()) { "Part 2 failed with $testPart2" }
    part2(input).println()
}
