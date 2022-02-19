package com.blackknight.mealbook.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
    @PrimaryKey
    val id: String,
    val name: String,
    val category: String,
    val instruction: String,
    val thumbnail: String,
    val youtube: String,
    val ingredient: List<Ingredient>
) {
    companion object {
        val EMPTY = Recipe("", "", "", "", "", "", emptyList())
    }
}
