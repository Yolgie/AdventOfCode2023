fun main() {
    fun part1(input: List<String>): Int {
        return input.map { line ->
            val digits = line.toList().mapNotNull(Char::digitToIntOrNull)
            digits.first() * 10 + digits.last()
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val digitMap = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9
        )
        return input.map { line ->
            println(line)
            val firstRealDigit = line.toList().mapNotNull(Char::digitToIntOrNull).firstOrNull()
            val firstRealDigitPosition = line.indexOfFirst { it == firstRealDigit?.digitToChar() }
            println("firstReal $firstRealDigit @ $firstRealDigitPosition")
            val firstDigit = digitMap.keys.associate { digitAsString ->
                line.indexOf(digitAsString) to digitMap.get(digitAsString).toString()
            }
                .plus(firstRealDigitPosition to firstRealDigit)
                .filterKeys { it >= 0 }
                .minBy { it.key }
                .value
            println(firstDigit)

            val lastRealDigit = line.toList().mapNotNull(Char::digitToIntOrNull).lastOrNull()
            val lastRealDigitPosition = line.indexOfLast { it == lastRealDigit?.digitToChar() }
            println("last $lastRealDigit @ $lastRealDigitPosition")
            val lastDigit = digitMap.keys.associate { digitAsString ->
                line.lastIndexOf(digitAsString) to digitMap.get(digitAsString).toString()
            }
                .plus(lastRealDigitPosition to lastRealDigit)
                .filterKeys { it >= 0 }
                .maxBy { it.key }
                .value
            println(lastDigit)

            "${firstDigit}${lastDigit}".toInt()
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142) { "Part 1 failed with ${part1(testInput)}" }
    val testInputPart2 = readInput("Day01_part2_test")
    check(part2(testInputPart2) == 281) { "Part 2 failed with ${part2(testInputPart2)}" }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}