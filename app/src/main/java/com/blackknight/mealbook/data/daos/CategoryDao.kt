package com.blackknight.mealbook.data.daos

import androidx.room.Dao
import androidx.room.Query
import com.blackknight.mealbook.data.entities.Category

@Dao
interface CategoryDao : BaseDao<Category> {
    @Query("SELECT * FROM category")
    suspend fun getCategories(): List<Category>
}