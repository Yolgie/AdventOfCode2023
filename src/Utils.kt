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

fun String.toIntList(vararg delimiters: Char = charArrayOf(' ')): List<Int> =
    this.split(*delimiters)
        .mapNotNull(String::toIntOrNull)

fun String.toIntList(regex: Regex): List<Int> =
    this.split(regex)
        .mapNotNull(String::toIntOrNull)

fun String.toLongList(vararg delimiters: Char = charArrayOf(' ')): List<Long> =
    this.split(*delimiters)
        .mapNotNull(String::toLongOrNull)

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