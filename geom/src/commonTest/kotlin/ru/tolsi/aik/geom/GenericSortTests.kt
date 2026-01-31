package ru.tolsi.aik.geom

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GenericSortTests {
    @Test
    fun testGenericSort_EmptyList() {
        val list = mutableListOf<Int>()
        val result = list.genericSort()

        assertEquals(0, result.size)
    }

    @Test
    fun testGenericSort_SingleElement() {
        val list = mutableListOf(42)
        val result = list.genericSort()

        assertEquals(1, result.size)
        assertEquals(42, result[0])
    }

    @Test
    fun testGenericSort_TwoElements_AlreadySorted() {
        val list = mutableListOf(1, 2)
        val result = list.genericSort()

        assertEquals(listOf(1, 2), result)
    }

    @Test
    fun testGenericSort_TwoElements_Reversed() {
        val list = mutableListOf(2, 1)
        val result = list.genericSort()

        assertEquals(listOf(1, 2), result)
    }

    @Test
    fun testGenericSort_AlreadySorted() {
        val list = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val result = list.genericSort()

        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), result)
    }

    @Test
    fun testGenericSort_ReverseSorted() {
        val list = mutableListOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
        val result = list.genericSort()

        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), result)
    }

    @Test
    fun testGenericSort_RandomOrder() {
        val list = mutableListOf(5, 2, 8, 1, 9, 3, 7, 4, 6, 10)
        val result = list.genericSort()

        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), result)
    }

    @Test
    fun testGenericSort_WithDuplicates() {
        val list = mutableListOf(5, 2, 8, 2, 9, 5, 7, 4, 8, 10)
        val result = list.genericSort()

        assertEquals(listOf(2, 2, 4, 5, 5, 7, 8, 8, 9, 10), result)
    }

    @Test
    fun testGenericSort_AllSameElements() {
        val list = mutableListOf(5, 5, 5, 5, 5)
        val result = list.genericSort()

        assertEquals(listOf(5, 5, 5, 5, 5), result)
    }

    @Test
    fun testGenericSort_NegativeNumbers() {
        val list = mutableListOf(-5, 3, -1, 7, -3, 0, 2)
        val result = list.genericSort()

        assertEquals(listOf(-5, -3, -1, 0, 2, 3, 7), result)
    }

    @Test
    fun testGenericSort_LargeList() {
        // Test with list larger than RUN size (32) to trigger timSort's merge phase
        val list = (1..100).map { it }.reversed().toMutableList()
        val result = list.genericSort()

        assertEquals((1..100).toList(), result)
    }

    @Test
    fun testGenericSort_PartialRange() {
        val list = mutableListOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
        val result = list.genericSort(left = 2, right = 6)

        // Only elements from index 2 to 6 should be sorted
        assertEquals(listOf(10, 9, 4, 5, 6, 7, 8, 3, 2, 1), result)
    }

    @Test
    fun testGenericSort_WithStrings() {
        val list = mutableListOf("dog", "apple", "cat", "banana")
        val result = list.genericSort()

        assertEquals(listOf("apple", "banana", "cat", "dog"), result)
    }

    @Test
    fun testGenericSort_WithDoubles() {
        val list = mutableListOf(3.14, 1.41, 2.71, 0.5, 4.2)
        val result = list.genericSort()

        assertEquals(listOf(0.5, 1.41, 2.71, 3.14, 4.2), result)
    }

    @Test
    fun testGenericSorted_CreatesNewList() {
        val original = listOf(5, 2, 8, 1, 9)
        val sorted = original.genericSorted()

        assertEquals(listOf(1, 2, 5, 8, 9), sorted)
        assertEquals(listOf(5, 2, 8, 1, 9), original, "Original list should be unchanged")
    }

    @Test
    fun testGenericSorted_PartialRange() {
        val original = listOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
        val sorted = original.genericSorted(left = 2, right = 5)

        // Should extract sublist [8, 7, 6, 5] from indices 2-5 and sort it
        assertEquals(listOf(5, 6, 7, 8), sorted)
        assertEquals(listOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1), original, "Original should be unchanged")
    }

    @Test
    fun testSortOpsComparable_Compare() {
        val list: MutableList<Comparable<Any>> = mutableListOf<Comparable<Any>>(5 as Comparable<Any>, 2 as Comparable<Any>, 8 as Comparable<Any>)

        assertTrue(SortOpsComparable.compare(list, 0, 1) > 0, "5 > 2")
        assertTrue(SortOpsComparable.compare(list, 1, 2) < 0, "2 < 8")
        assertEquals(0, SortOpsComparable.compare(list, 0, 0), "5 == 5")
    }

    @Test
    fun testSortOpsComparable_Swap() {
        val list: MutableList<Comparable<Any>> = mutableListOf<Comparable<Any>>(5 as Comparable<Any>, 2 as Comparable<Any>, 8 as Comparable<Any>)

        SortOpsComparable.swap(list, 0, 2)

        assertEquals(listOf<Comparable<Any>>(8 as Comparable<Any>, 2 as Comparable<Any>, 5 as Comparable<Any>), list)
    }

    @Test
    fun testGenericSort_VeryLargeList() {
        // Test with list much larger than RUN size to ensure timSort works correctly
        val size = 1000
        val list = (1..size).map { it }.shuffled().toMutableList()
        val result = list.genericSort()

        assertEquals((1..size).toList(), result)
    }

    @Test
    fun testGenericSort_ExtremelySmallRange() {
        val list = mutableListOf(5, 3, 1, 4, 2)
        // Sort only index 1 to 1 (single element range)
        val result = list.genericSort(left = 1, right = 1)

        // Should remain unchanged (single element is already sorted)
        assertEquals(listOf(5, 3, 1, 4, 2), result)
    }

    @Test
    fun testGenericSort_NearlySorted() {
        // List is mostly sorted with a few out-of-place elements
        val list = mutableListOf(1, 2, 3, 5, 4, 6, 7, 8, 10, 9)
        val result = list.genericSort()

        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), result)
    }

    @Test
    fun testGenericSort_AlternatingPattern() {
        val list = mutableListOf(1, 10, 2, 9, 3, 8, 4, 7, 5, 6)
        val result = list.genericSort()

        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), result)
    }

    @Test
    fun testGenericSort_ManyDuplicates() {
        val list = mutableListOf(1, 1, 2, 2, 3, 3, 1, 1, 2, 2)
        val result = list.genericSort()

        assertEquals(listOf(1, 1, 1, 1, 2, 2, 2, 2, 3, 3), result)
    }

    @Test
    fun testGenericSort_ExactlyRunSize() {
        // Test with exactly 32 elements (RUN size)
        val list = (1..32).map { it }.reversed().toMutableList()
        val result = list.genericSort()

        assertEquals((1..32).toList(), result)
    }

    @Test
    fun testGenericSort_JustOverRunSize() {
        // Test with 33 elements (RUN size + 1)
        val list = (1..33).map { it }.reversed().toMutableList()
        val result = list.genericSort()

        assertEquals((1..33).toList(), result)
    }

    @Test
    fun testGenericSort_TwoRuns() {
        // Test with 64 elements (2 * RUN size)
        val list = (1..64).map { it }.reversed().toMutableList()
        val result = list.genericSort()

        assertEquals((1..64).toList(), result)
    }

    @Test
    fun testGenericSort_MultipleRunsOddSize() {
        // Test with 100 elements (not a multiple of RUN size)
        val list = (1..100).map { it }.shuffled().toMutableList()
        val result = list.genericSort()

        assertTrue(result == (1..100).toList())
    }
}
