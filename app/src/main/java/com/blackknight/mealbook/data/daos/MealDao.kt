package com.blackknight.mealbook.data.daos

import androidx.room.Dao
import androidx.room.Query
import com.blackknight.mealbook.data.entities.Meal
import io.reactivex.rxjava3.core.Single

@Dao
interface MealDao : BaseDao<Meal> {
    @Query("SELECT * FROM meal WHERE categoryId = :categoryId")
    fun getMealList(categoryId: String): Single<List<Meal>>
}