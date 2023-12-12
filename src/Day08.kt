fun main() {
    val day = 8

    fun part1(input: List<String>): Int {
        val instructions = input.first()
        val instructionSequence = generateSequence(instructions::iterator).flatMap(CharIterator::asSequence)
        val nodes = input.drop(2).associate { line ->
            line.substring(0..2) to (line.substring(7..9) to line.substring(12..14))
        }

        return instructionSequence.runningFold("AAA") { key, choice ->
            val node = nodes.getValue(key)
            when (choice) {
                'L' -> node.first
                'R' -> node.second
                else -> throw Exception("Choice not in (L,R): $choice")
            }
        }
            .takeWhile { current -> current != "ZZZ" }
//            .printSequence()
            .count()
    }

    fun part2(input: List<String>): Long {
        val instructions = input.first()
        val instructionSequence = generateSequence(instructions::iterator).flatMap(CharIterator::asSequence)
        val nodes = input.drop(2).associate { line ->
            line.substring(0..2) to (line.substring(7..9) to line.substring(12..14))
        }
        val startingNodes = nodes.keys.filter { it.endsWith('A') }
        val endNodes = nodes.keys.filter { it.endsWith('Z') }.toSet()

//        println("#of parallel paths: ${startingNodes.count()}")
//        println("#of possible ends: ${endNodes.size}")

        val runningSequence = instructionSequence.runningFold(startingNodes) { currentNodes, choice ->
            when (choice) {
                'L' -> currentNodes.map(nodes::getValue).map(Pair<String, String>::first)
                'R' -> currentNodes.map(nodes::getValue).map(Pair<String, String>::second)
                else -> throw Exception("Choice not in (L,R): $choice")
            }
        }
        val skip = if (startingNodes.count() > 3) 3000000000L else 0L

        return runningSequence.dropLong(skip)
            .takeWhile { currentNodes -> !currentNodes.all { it in endNodes } }
            .countAsLong() + skip
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${"%02d".format(day)}_test")
    val input = readInput("Day${"%02d".format(day)}")

    val testPart1 = part1(testInput)
    check(testPart1 == 2) { "Part 1 failed with $testPart1" }
    measureTime { part1(input).println() }

    val testInputPart2 = readInput("Day${"%02d".format(day)}_part2_test")
    val testPart2 = part2(testInputPart2)
    check(testPart2 == 6L ) { "Part 2 failed with $testPart2" }
    measureTime { part2(input).println() }
}
