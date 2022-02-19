package com.blackknight.mealbook.ui.recipe

import androidx.lifecycle.ViewModel
import com.blackknight.mealbook.data.entities.Recipe
import com.blackknight.mealbook.data.repo.RecipeRepo
import com.blackknight.mealbook.util.SchedulerProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@HiltViewModel
internal class RecipeViewModel @Inject constructor(
    private val recipeRepo: RecipeRepo,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    fun getRecipe(recipeId: String): Single<Recipe> {
        return recipeRepo.getRecipe(recipeId)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
    }
}