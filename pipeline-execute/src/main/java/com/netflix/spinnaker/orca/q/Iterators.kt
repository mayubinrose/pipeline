package com.netflix.spinnaker.orca.q

/**
 * Iterate with information about whether the current element is first or last
 * and what index it is.
 */
fun <T> ListIterator<T>.forEachWithMetadata(block: (IteratorElement<T>) -> Unit) {
  while (hasNext()) {
    val first = !hasPrevious()
    val index = nextIndex()
    val value = next()
    val last = !hasNext()
    block.invoke(IteratorElement(value, index, first, last))
  }
}

data class IteratorElement<out T>(
  val value: T,
  val index: Int,
  val isFirst: Boolean,
  val isLast: Boolean
)

/**
 * Groovy-style sublist using range. For example:
 *
 *     assert(listOf(1, 2, 3)[1..2] == listOf(2, 3))
 */
operator fun <E> List<E>.get(indices: IntRange): List<E> =
  subList(indices.start, indices.endInclusive + 1)
