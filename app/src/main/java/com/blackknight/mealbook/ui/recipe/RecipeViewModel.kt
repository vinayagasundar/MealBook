package com.blackknight.mealbook.ui.recipe

import androidx.lifecycle.ViewModel
import com.blackknight.mealbook.data.entities.Recipe
import com.blackknight.mealbook.data.repo.RecipeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class RecipeViewModel @Inject constructor(
    private val recipeRepo: RecipeRepo
) : ViewModel() {

    suspend fun getRecipe(recipeId: String): Result<Recipe> {
        return try {
            recipeRepo.getRecipe(recipeId)
                .let { Result.success(it) }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}