fun main() {
    val day = 6

    fun part1(input: List<String>): Int {
        val times = input[0].toIntList()
        val distances = input[1].toIntList()

        fun distanceTravelled(chargeTime: Int, travelTime: Int): Int {
            return chargeTime * travelTime
        }

        return times.zip(distances)
            .map { (time, distanceToBeat) ->
                val timeRange = 1..time
                val possibilities = timeRange.map { chargeTime -> chargeTime to time - chargeTime }
                    .map { (chargeTime, travelTime) -> distanceTravelled(chargeTime, travelTime) }
                    .filter { it > distanceToBeat }
                possibilities.size
            }.reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Long {
        val time = requireNotNull(input[0].parseDigitsToLong())
        val distance = requireNotNull(input[1].parseDigitsToLong())

        fun distanceTravelled(chargeTime: Long, travelTime: Long): Long {
            return chargeTime * travelTime
        }


        val timeRange = 1..time
        val possibilities = timeRange
            .asSequence()
            .map { chargeTime -> chargeTime to time - chargeTime }
            .map { (chargeTime, travelTime) -> distanceTravelled(chargeTime, travelTime) }
            .filter { it > distance }
            .fold(0L) { acc: Long, _: Long -> acc + 1 }
        return possibilities
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${"%02d".format(day)}_test")
    check(part1(testInput) == 288) { "Part 1 failed with ${part1(testInput)}" }
    check(part2(testInput) == 71503L) { "Part 2 failed with ${part2(testInput)}" }

    val input = readInput("Day${"%02d".format(day)}")
    part1(input).println()
    part2(input).println()
}

