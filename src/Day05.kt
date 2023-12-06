fun main() {
    val day = 5

    fun part1(input: List<String>): Long {
        val seeds = input.first().toLongList()
//        println(seeds)
        val inputParts = input.drop(2).splitBy("")
//        println(inputParts)
        val seedToSoilMap = inputParts[0].parseToMap()
        val soilToFertilizerMap = inputParts[1].parseToMap()
        val fertilizerToWaterMap = inputParts[2].parseToMap()
        val waterToLightMap = inputParts[3].parseToMap()
        val lightToTemperatureMap = inputParts[4].parseToMap()
        val temperatureToHumidityMap = inputParts[5].parseToMap()
        val humidityToLocationMap = inputParts[6].parseToMap()

        return seeds.map { seed ->
            val soil = seed + seedToSoilMap.getValueForContainedInKeyRange(seed)
            val fertilizer = soil + soilToFertilizerMap.getValueForContainedInKeyRange(soil)
            val water = fertilizer + fertilizerToWaterMap.getValueForContainedInKeyRange(fertilizer)
            val light = water + waterToLightMap.getValueForContainedInKeyRange(water)
            val temperature = light + lightToTemperatureMap.getValueForContainedInKeyRange(light)
            val humidity = temperature + temperatureToHumidityMap.getValueForContainedInKeyRange(temperature)
            val location = humidity + humidityToLocationMap.getValueForContainedInKeyRange(humidity)
            location
        }.min()
    }

    fun part2(input: List<String>): Long {
        val seedsInputs = input.first().toLongList()
        val seedRanges = seedsInputs.zipWithNext()
            .filterIndexed { index, _ -> index % 2 == 0 }
            .map { (start, count) -> start..(start + count - 1) }
//        println(seeds)
        val inputParts = input.drop(2).splitBy("")
//        println(inputParts)
        val seedToSoilMap = inputParts[0].parseToMap()
        val soilToFertilizerMap = inputParts[1].parseToMap()
        val fertilizerToWaterMap = inputParts[2].parseToMap()
        val waterToLightMap = inputParts[3].parseToMap()
        val lightToTemperatureMap = inputParts[4].parseToMap()
        val temperatureToHumidityMap = inputParts[5].parseToMap()
        val humidityToLocationMap = inputParts[6].parseToMap()

        println("count = ${seedRanges.size}")

        var min = Long.MAX_VALUE

        seedRanges.forEachIndexed { index, seedRange ->
            print(index)
            seedRange.forEach { seed ->
                val soil = seed + seedToSoilMap.getValueForContainedInKeyRange(seed)
                val fertilizer = soil + soilToFertilizerMap.getValueForContainedInKeyRange(soil)
                val water = fertilizer + fertilizerToWaterMap.getValueForContainedInKeyRange(fertilizer)
                val light = water + waterToLightMap.getValueForContainedInKeyRange(water)
                val temperature = light + lightToTemperatureMap.getValueForContainedInKeyRange(light)
                val humidity = temperature + temperatureToHumidityMap.getValueForContainedInKeyRange(temperature)
                val location = humidity + humidityToLocationMap.getValueForContainedInKeyRange(humidity)
                if (location < min) min = location
            }
        }

        return min
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${"%02d".format(day)}_test")
    check(part1(testInput) == 35L) { "Part 1 failed with ${part1(testInput)}" }
    check(part2(testInput) == 46L) { "Part 2 failed with ${part2(testInput)}" }

    val input = readInput("Day${"%02d".format(day)}")
    part1(input).println()
    part2(input).println()
}

fun List<String>.parseToMap(): Map<LongRange, Long> =
    this.drop(1)
        .map { rawRanges -> rawRanges.toLongList() }
        .associate { (destinationRangeStart, sourceRangeStart, rangeLength) ->
            sourceRangeStart..(sourceRangeStart + rangeLength - 1) to destinationRangeStart - sourceRangeStart
        }

fun Map<LongRange, Long>.getValueForContainedInKeyRange(target: Long): Long =
    this.filterKeys { range -> target in range }
        .values
        .firstOrNull() ?: 0