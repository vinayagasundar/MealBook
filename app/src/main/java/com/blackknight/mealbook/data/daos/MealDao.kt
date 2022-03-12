package com.blackknight.mealbook.data.daos

import androidx.room.Dao
import androidx.room.Query
import com.blackknight.mealbook.data.entities.Meal

@Dao
interface MealDao : BaseDao<Meal> {
    @Query("SELECT * FROM meal WHERE categoryId = :categoryId")
    suspend fun getMealList(categoryId: String): List<Meal>
}