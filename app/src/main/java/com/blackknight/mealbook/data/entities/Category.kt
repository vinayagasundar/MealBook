package com.blackknight.mealbook.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey
    val id: String,
    val name: String,
    val thumbnail: String,
    val description: String
)
