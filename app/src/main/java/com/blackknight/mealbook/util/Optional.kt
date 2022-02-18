package com.blackknight.mealbook.util

sealed class Optional<out T> {
    class Present<T>(val value: T) : Optional<T>()
    object Absent : Optional<Nothing>()

    fun getOrNull(): T? {
        return when (this) {
            is Present -> value
            is Absent -> null
        }
    }
}