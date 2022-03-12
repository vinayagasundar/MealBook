package com.blackknight.mealbook.data.repo

import com.blackknight.mealbook.data.daos.RecipeDao
import com.blackknight.mealbook.data.entities.Recipe
import com.blackknight.mealbook.data.mapper.Mapper
import com.blackknight.mealbook.network.MealDBService
import com.blackknight.mealbook.network.response.RecipeResponse
import java.lang.Exception
import javax.inject.Inject

interface RecipeRepo {
    suspend fun getRecipe(recipeId: String): Recipe
}

class RecipeRepoImpl @Inject constructor(
    private val mealDBService: MealDBService,
    private val recipeDao: RecipeDao,
    private val mapper: Mapper<RecipeResponse, Recipe>
) : RecipeRepo {
    override suspend fun getRecipe(recipeId: String): Recipe {
        val recipe = getAndSaveRecipe(recipeId)
        return recipe ?: recipeDao.getRecipe(recipeId)
    }

    private suspend fun getAndSaveRecipe(recipeId: String): Recipe? {
        return try {
            val response = mealDBService.getRecipe(recipeId)
            response.list.firstOrNull()?.let { mapper.map(it) }
                ?.also { recipe -> recipeDao.insertOrUpdate(listOf(recipe)) }
        } catch (e: Exception) {
            null
        }
    }
}