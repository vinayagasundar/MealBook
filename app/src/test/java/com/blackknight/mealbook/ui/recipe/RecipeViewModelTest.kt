package com.blackknight.mealbook.ui.recipe

import com.blackknight.mealbook.data.entities.Ingredient
import com.blackknight.mealbook.data.entities.Recipe
import com.blackknight.mealbook.data.repo.RecipeRepo
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.whenever

class RecipeViewModelTest {

    @Mock
    private lateinit var recipeRepo: RecipeRepo


    private lateinit var viewModel: RecipeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = RecipeViewModel(recipeRepo)
    }

    @Test
    fun getRecipe() {
        runBlocking {
            val recipe = Recipe(
                id = "id",
                name = "name",
                category = "category",
                instruction = "instruction",
                thumbnail = "thumbnail",
                youtube = "youtube",
                ingredient = listOf(Ingredient("name1", "measurement1"))
            )
            whenever(recipeRepo.getRecipe("recipeId")).thenReturn(recipe)

            val result = viewModel.getRecipe("recipeId")

            Assert.assertEquals(Result.success(recipe), result)
        }
    }

    @Test
    fun getRecipe_failure() {
        runBlocking {
            val exception = Exception("Error")
            whenever(recipeRepo.getRecipe("recipeId")).doSuspendableAnswer {
                throw exception
            }

            val result = viewModel.getRecipe("recipeId")

            Assert.assertEquals(Result.failure<Recipe>(exception), result)
        }
    }
}