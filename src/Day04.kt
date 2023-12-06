fun main() {
    fun part1(input: List<String>): Int {
        return input.map { line ->
            val parts = line.split(':', '|')
            val winningNumbers = parts[1].toIntList()
            val myNumbers = parts[2].toIntList()
            val myWinningNumbers = myNumbers.intersect(winningNumbers)
            val points = Math.pow(2.0, (myWinningNumbers.size-1).toDouble()).toInt()
            points
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val multipliers = mutableMapOf<Int, Int>()
        input.forEach { line ->
            val parts = line.split(':', '|')
            val card = parts[0].toIntList().single()
            multipliers.putIfAbsent(card, 1)
            val winningNumbers = parts[1].toIntList()
            val myNumbers = parts[2].toIntList()
            val myWinningNumbers = myNumbers.intersect(winningNumbers)
            val wonCardCount = myWinningNumbers.size
            val wonCards = (card+1)..(card+wonCardCount)

            if (wonCardCount > 0) {
                wonCards.forEach { wonCard ->
                    multipliers[wonCard] = multipliers.getOrDefault(wonCard, 1) + multipliers.get(card)!!
                }
            }
        }
        return multipliers.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13) { "Part 1 failed with ${part1(testInput)}" }
    check(part2(testInput) == 30) { "Part 2 failed with ${part2(testInput)}" }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

