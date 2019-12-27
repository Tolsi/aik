package ru.tolsi.aik.geom

fun <T : Comparable<T>> ClosedRange<T>.bounds(): Pair<T, T> {
    return start to endInclusive
}