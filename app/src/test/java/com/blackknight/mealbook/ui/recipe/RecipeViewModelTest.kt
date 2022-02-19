package com.blackknight.mealbook.ui.recipe

import com.blackknight.mealbook.data.entities.Ingredient
import com.blackknight.mealbook.data.entities.Recipe
import com.blackknight.mealbook.data.repo.RecipeRepo
import com.blackknight.mealbook.fake.FakeSchedulerProvider
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class RecipeViewModelTest {

    @Mock
    private lateinit var recipeRepo: RecipeRepo

    private val schedulerProvider = FakeSchedulerProvider()

    private lateinit var viewModel: RecipeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = RecipeViewModel(recipeRepo, schedulerProvider)
    }

    @Test
    fun getRecipe() {
        val recipe = Recipe(
            id = "id",
            name = "name",
            category = "category",
            instruction = "instruction",
            thumbnail = "thumbnail",
            youtube = "youtube",
            ingredient = listOf(Ingredient("name1", "measurement1"))
        )
        whenever(recipeRepo.getRecipe("recipeId")).thenReturn(Single.just(recipe))

        recipeRepo.getRecipe("recipeId")
            .test()
            .assertValue(recipe)
            .assertNoErrors()
            .assertComplete()
            .dispose()
    }
}