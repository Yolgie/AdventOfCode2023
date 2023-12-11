import kotlin.math.ceil

fun main() {
    val day = 10

    fun getDirections(input: List<String>, currentPosition: Pair<Int, Int>): Set<Direction> =
        input.getOrNull(currentPosition)?.toPipe()?.directions ?: emptySet()

    fun getNextStep(
        input: List<String>,
        currentPosition: Pair<Int, Int>,
        direction: Direction
    ): Pair<Pair<Int, Int>, Direction?> {
        val nextPosition = currentPosition.get(direction)
        val nextDirection = getDirections(input, nextPosition).filterNot { it == direction.opposite }.singleOrNull()
        return nextPosition to nextDirection
    }

    fun getStartDirections(input: List<String>, start: Pair<Int, Int>): Set<Direction> {
        return Direction.entries.filter { direction ->
            val directionsFromNeighbor = getDirections(input, start.get(direction))
                .filterNot { it == direction.opposite }
            directionsFromNeighbor.size == 1
        }.toSet()
    }

    fun part1(input: List<String>): Int {
        val start = input.mapIndexedNotNull { y, line ->
            if (line.contains('S')) line.indexOf('S') to y
            else null
        }.single()

        val startDirections = getStartDirections(input, start)
        val path =
            generateSequence<Pair<Pair<Int, Int>, Direction?>>(start to startDirections.first()) { (currentPosition, direction) ->
                println("$currentPosition ($direction)")
                val nextStep = getNextStep(input, currentPosition, requireNotNull(direction))
                if (nextStep.second == null) null
                else nextStep
            }
        val result = path.toList()

        return ceil(result.size / 2.0).toInt()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${"%02d".format(day)}_test")
    check(part1(testInput) == 4) { "Part 1 failed with ${part1(testInput)}" }
    val testbInput = readInput("Day${"%02d".format(day)}_testb")
    check(part1(testbInput) == 8) { "Part 1 failed with ${part1(testbInput)}" }

    val input = readInput("Day${"%02d".format(day)}")
    part1(input).println()

    check(part2(testInput) == 30) { "Part 2 failed with ${part2(testInput)}" }
    part2(input).println()
}

enum class Direction(val coordinate: Pair<Int, Int>) {
    UP(0 to -1),
    DOWN(0 to 1),
    LEFT(-1 to 0),
    RIGHT(1 to 0);

    val opposite: Direction
        get() = when (this) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
}

fun Pair<Int, Int>.get(direction: Direction): Pair<Int, Int> = this + direction.coordinate

enum class Pipe(val symbol: Char, val directions: Set<Direction>) {
    VERTICAL_PIPE('|', setOf(Direction.UP, Direction.DOWN)),
    HORIZONTAL_PIPE('-', setOf(Direction.LEFT, Direction.RIGHT)),
    BEND_L('L', setOf(Direction.UP, Direction.RIGHT)),
    BEND_J('J', setOf(Direction.UP, Direction.LEFT)),
    BEND_7('7', setOf(Direction.LEFT, Direction.DOWN)),
    BEND_F('F', setOf(Direction.RIGHT, Direction.DOWN)),
    GROUND('.', emptySet()),
    START('S', emptySet());

    companion object {
        private val map = entries.associateBy(Pipe::symbol)

        fun fromSymbol(symbol: Char): Pipe {
            return requireNotNull(map[symbol])
        }
    }
}

fun Char.toPipe(): Pipe = Pipe.fromSymbol(this)