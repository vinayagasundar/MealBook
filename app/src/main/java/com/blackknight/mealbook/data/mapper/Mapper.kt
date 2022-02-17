package com.blackknight.mealbook.data.mapper

interface Mapper<F, T> {
    fun map(from: F): T
}