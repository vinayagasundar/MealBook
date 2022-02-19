package com.blackknight.mealbook.data.daos

import androidx.room.Dao
import androidx.room.Query
import com.blackknight.mealbook.data.entities.Recipe
import io.reactivex.rxjava3.core.Single

@Dao
interface RecipeDao : BaseDao<Recipe> {
    @Query("SELECT * FROM recipe where id = :recipeId")
    fun getRecipe(recipeId: String): Single<Recipe>
}