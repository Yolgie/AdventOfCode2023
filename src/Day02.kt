fun main() {
    val day = 2

    fun part1(input: List<String>): Int {
        val maxCubes = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14
        )

        return input.sumOf { line ->
            val parts = line.split(':', ';')
            val gameNumber = parts[0].toIntList().single()
            val hands = parts.drop(1)
                .map { unparsedHand ->
                    val hand = unparsedHand.split(',')
                        .associate {
                            val (number, color) = it.trim().split(' ')
                            color.trim() to number.toInt()
                        }
                    // println(hand)
                    // validate hand
                    hand.keys.all { color -> hand.get(color)!! <= maxCubes.get(color)!! }
                }
            if (hands.all { it }) gameNumber else 0
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val parts = line.split(':', ';')
            val hands = parts.drop(1)
                .map { unparsedHand ->
                    val hand = unparsedHand.split(',')
                        .associate {
                            val (number, color) = it.trim().split(' ')
                            color.trim() to number.toInt()
                        }
                    hand
                }
            // println(hands)

            val minCubes = hands.fold(mutableMapOf<String, Int>()) { minimum, hand ->
                hand.forEach { (color, count) -> minimum[color] = Math.max(minimum.getOrDefault(color, 0), count) }
                minimum
            }
            // println(minCubes)

            val power = minCubes.values.reduce { acc, count -> acc * count }
            power
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${"%02d".format(day)}_test")
    check(part1(testInput) == 8) { "Part 1 failed with ${part1(testInput)}" }
    check(part2(testInput) == 2286) { "Part 2 failed with ${part2(testInput)}" }

    val input = readInput("Day${"%02d".format(day)}")
    part1(input).println()
    part2(input).println()
}
