package com.blackknight.mealbook.data.repo

import com.blackknight.mealbook.data.daos.RecipeDao
import com.blackknight.mealbook.data.entities.Recipe
import com.blackknight.mealbook.data.mapper.Mapper
import com.blackknight.mealbook.network.MealDBService
import com.blackknight.mealbook.network.response.RecipeResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface RecipeRepo {
    fun getRecipe(recipeId: String): Single<Recipe>
}

class RecipeRepoImpl @Inject constructor(
    private val mealDBService: MealDBService,
    private val recipeDao: RecipeDao,
    private val mapper: Mapper<RecipeResponse, Recipe>
) : RecipeRepo {
    override fun getRecipe(recipeId: String): Single<Recipe> {
        return getAndSaveRecipe(recipeId)
            .onErrorResumeNext {
                recipeDao.getRecipe(recipeId)
            }
    }

    private fun getAndSaveRecipe(recipeId: String): Single<Recipe> {
        return mealDBService.getRecipe(recipeId)
            .map { response ->
                response.list.firstOrNull()?.let { mapper.map(it) } ?: Recipe.EMPTY
            }
            .flatMap { recipe ->
                recipeDao.insertOrUpdate(listOf(recipe))
                    .andThen(Single.just(recipe))
            }
    }
}