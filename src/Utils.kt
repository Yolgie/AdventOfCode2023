@file:Suppress("unused")

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun <T> Iterable<T>.printEach(label: String = ""): Iterable<T> {
    this.forEach { item -> if (label.isNotBlank()) println("$label: $item") else println(item) }
    return this
}

fun <T> Iterable<T>.printEachIndexed(label: String = ""): Iterable<T> {
    this.forEachIndexed { index, item -> println("$label$index: $item") }
    return this
}

fun <T> Iterable<T>.printIterableLine(label: String = ""): Iterable<T> {
    if (label.isNotBlank()) println("$label: $this") else println(this)
    return this
}

fun <T> Sequence<T>.printSequence(label: String = ""): Sequence<T> {
    val materializedList = this.toList()
    if (label.isNotBlank()) println("$label: $materializedList") else println(materializedList)
    return materializedList.asSequence()
}

fun String.toIntList(vararg delimiters: Char = charArrayOf(' ')): List<Int> =
    this.split(*delimiters)
        .mapNotNull(String::toIntOrNull)

fun String.toIntList(regex: Regex): List<Int> =
    this.split(regex)
        .mapNotNull(String::toIntOrNull)

fun String.toLongList(vararg delimiters: Char = charArrayOf(' ')): List<Long> =
    this.split(*delimiters)
        .mapNotNull(String::toLongOrNull)

fun String.toBigIntegerSeqence(vararg delimiters: Char = charArrayOf(' ')): Sequence<BigInteger> =
    this.splitToSequence(*delimiters)
        .mapNotNull(String::toBigIntegerOrNull)

fun <T : Any> List<T>.splitBy(deliminator: T): List<List<T>> {
    return this.fold(mutableListOf(mutableListOf<T>())) { lists, current ->
        if (current == deliminator) {
            lists.add(mutableListOf())
        } else {
            lists.last().add(current)
        }
        lists
    }
}

fun String.parseDigitsToLong(): Long? = this.filter(Char::isDigit).toLongOrNull()

fun Collection<Int>.multiply() = this.reduce { acc, i -> acc * i }

fun Collection<Long>.multiply() = this.reduce { acc, i -> acc * i }

fun Sequence<BigInteger>.sum(): BigInteger = this.fold(BigInteger.ZERO) { sum, next -> sum + next }

fun Iterable<BigInteger>.sum(): BigInteger = this.fold(BigInteger.ZERO) { sum, next -> sum + next }

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = this.first + other.first to this.second + other.second

fun List<String>.getOrNull(coordinate: Pair<Int, Int>): Char? = this.getOrNull(coordinate.second)?.getOrNull(coordinate.first)

fun List<String>.get(coordinate: Pair<Int, Int>): Char = this.get(coordinate.second).get(coordinate.first)

fun Pair<Int, Int>.manhattanDistance(other: Pair<Int, Int>): Int {
    return kotlin.math.abs(this.first - other.first) + kotlin.math.abs(this.second - other.second)
}

fun getManhattanPathSequence(start: Pair<Int, Int>, end: Pair<Int, Int>): Sequence<Pair<Int, Int>> {
    val horizontalPath = if (start.first <= end.first) {
        (start.first..end.first)
    } else {
        (start.first downTo end.first)
    }.asSequence().map { x -> Pair(x, start.second) }

    val verticalPath = if (start.second <= end.second) {
        ((start.second + 1)..end.second) // +1 to avoid duplication at the corner
    } else {
        ((start.second - 1) downTo end.second) // -1 to avoid duplication at the corner
    }.asSequence().map { y -> Pair(end.first, y) }

    return horizontalPath + verticalPath
}

fun <T> Iterable<T>.allEqual(): Boolean {
    return this.all { it == this.first() }
}