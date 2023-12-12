fun main() {
    val day = 7

    fun part1(input: List<String>): Int {
        val hands = input.associate { line ->
            val (handString, scoreString) = line.split(" ")
            val score = scoreString.toInt()
            handString to score
        }

        val sortedHands = hands.keys.sortedWith(handComparatorLexical).reversed()
        val bids = sortedHands
            .filterNot { hands.get(it) == 0 } // exclude testdata
            .mapIndexed { rank, hand -> (rank + 1) * hands.get(hand)!! }
//        println(sortedHands)

        return bids.sum()
    }

    fun part2(input: List<String>): Int {
        val hands = input.associate { line ->
            val (handString, scoreString) = line.split(" ")
            val score = scoreString.toInt()
            handString to score
        }

        val sortedHands = hands.keys.sortedWith(handComparatorWithJokers).reversed()
        val bids = sortedHands
            .filterNot { hands.get(it) == 0 } // exclude testdata
            .mapIndexed { rank, hand -> (rank + 1) * hands.get(hand)!! }
        println(sortedHands)

        return bids.sum()    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${"%02d".format(day)}_test")
    val input = readInput("Day${"%02d".format(day)}")

    val testPart1 = part1(testInput)
    check(testPart1 == 6440) { "Part 1 failed with $testPart1" }
    measureTime("Part1 ") { part1(input).println() }

    val testPart2 = part2(testInput)
    check(testPart2 == 5905) { "Part 2 failed with $testPart2" }
    measureTime("Part2 ") { part2(input).println() }
}

private const val joker = 'J'
private val allCards = listOf('A', 'K', 'Q', joker, 'T', '9', '8', '7', '6', '5', '4', '3', '2')
private val allCardsWithJokers = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', joker)

enum class HandType(val matcher: (String) -> Boolean) : Comparable<HandType> {
    FIVE_OF_A_KIND({ hand -> hand.distribution().containsValue(5) }),
    FOUR_OF_A_KIND({ hand -> hand.distribution().containsValue(4) }),
    FULL_HOUSE({ hand -> hand.distribution().containsAllValues(3, 2) }),
    THREE_OF_A_KIND({ hand -> hand.distribution().containsAllValues(3, 1, 1) }),
    TWO_PAIR({ hand -> hand.distribution().containsAllValues(2, 2, 1) }),
    ONE_PAIR({ hand -> hand.distribution().containsAllValues(2, 1, 1, 1) }),
    HIGH_CARD({ hand -> hand.distribution().values.all { it == 1 } });

    companion object {
        fun matchHand(hand: String): HandType = entries.first { it.matcher(hand) }

        fun matchHandWithJokers(hand: String): HandType = entries.first { it.matcherWithJokers(hand) }
    }

    private fun matcherWithJokers(hand: String): Boolean {
        return if (hand.contains(joker) && hand != "JJJJJ") {
            val distribution = hand.distribution()
            val mostCommonCards = distribution
                .filterKeys { it != joker }
                .entries.groupBy { it.value }.maxBy { it.key }.value.map { it.key }
            val mostValuableCommonCard = mostCommonCards.minBy { allCardsWithJokers.indexOf(it) }
            matcher(hand.replace(joker, mostValuableCommonCard))
        } else matcher(hand)
    }
}

fun String.matchHand() = HandType.matchHand(this)

fun String.matchHandWithJokers() = HandType.matchHandWithJokers(this)

val cardOrderComparator: Comparator<String> = Comparator { one, other ->
    one.zip(other)
        .map { (charOne, charOther) -> allCards.indexOf(charOne) to allCards.indexOf(charOther) }
        .find { it.first != it.second }
        ?.let { it.first.compareTo(it.second) }
        ?: 0
}

val handComparatorLexical: Comparator<String> = compareBy<String> { it.matchHand() }
    .thenComparing(cardOrderComparator)

val cardOrderComparatorWithJokers: Comparator<String> = Comparator { one, other ->
    one.zip(other)
        .map { (charOne, charOther) -> allCardsWithJokers.indexOf(charOne) to allCardsWithJokers.indexOf(charOther) }
        .find { it.first != it.second }
        ?.let { it.first.compareTo(it.second) }
        ?: 0
}

val handComparatorWithJokers: Comparator<String> = compareBy<String> { it.matchHandWithJokers() }
    .thenComparing(cardOrderComparatorWithJokers)

val handComparator: Comparator<String> = Comparator { one, other ->
    val handTypeComparison = one.matchHand().compareTo(other.matchHand())
    if (handTypeComparison != 0) handTypeComparison
    else compareHandDistributions(one.distribution(), other.distribution())
}

fun compareHandDistributions(hand1: Map<Char, Int>, hand2: Map<Char, Int>): Int {
    val sortedMap1 = hand1.entries.sortedWith(
        compareByDescending(Map.Entry<Char, Int>::value)
            .thenComparing { (key, _) -> allCards.indexOf(key) })
    val sortedMap2 = hand2.entries.sortedWith(
        compareByDescending(Map.Entry<Char, Int>::value)
            .thenComparing { (key, _) -> allCards.indexOf(key) })

    return sortedMap1.zip(sortedMap2).fold(0) { acc, (entry1, entry2) ->
        if (acc != 0) return acc
        compareByDescending<Map.Entry<Char, Int>>(Map.Entry<Char, Int>::value)
            .thenComparing { (key, _) -> allCards.indexOf(key) }
            .compare(entry1, entry2)
    }
}

fun <K, V> Map<K, V>.containsAllValues(vararg values: V): Boolean {
    val valueCounts = values.groupingBy { it }.eachCount()
    return valueCounts.all { (value, count) ->
        this.values.count { it == value } >= count
    }
}

fun String.distribution() = this.groupingBy { it }.eachCount()