package com.blackknight.mealbook.data.daos

import androidx.room.Dao
import androidx.room.Query
import com.blackknight.mealbook.data.entities.Recipe

@Dao
interface RecipeDao : BaseDao<Recipe> {
    @Query("SELECT * FROM recipe where id = :recipeId")
    suspend fun getRecipe(recipeId: String): Recipe
}