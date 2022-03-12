package com.blackknight.mealbook.data.repo

import com.blackknight.mealbook.data.daos.RecipeDao
import com.blackknight.mealbook.data.entities.Recipe
import com.blackknight.mealbook.data.mapper.Mapper
import com.blackknight.mealbook.network.MealDBService
import com.blackknight.mealbook.network.response.RecipeListResponse
import com.blackknight.mealbook.network.response.RecipeResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class RecipeRepoImplTest {

    @Mock
    private lateinit var recipeDao: RecipeDao

    @Mock
    private lateinit var mealDBService: MealDBService

    @Mock
    private lateinit var mapper: Mapper<RecipeResponse, Recipe>

    private lateinit var recipeRepo: RecipeRepoImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        recipeRepo = RecipeRepoImpl(mealDBService, recipeDao, mapper)
    }

    @Test
    fun `when getRecipe invoked if service return list then save it local and return recipe`() {
        runBlocking {
            val mealResponse = RecipeResponse(
                id = "id",
                name = "name",
                thumbnail = "thumbnail",
                categories = "category"
            )
            val response = RecipeListResponse(listOf(mealResponse))
            val recipe = Recipe(
                id = "id",
                name = "name",
                thumbnail = "thumbnail",
                category = "category",
                instruction = "",
                ingredient = emptyList(),
                youtube = ""
            )
            val recipeList = listOf(recipe)

            whenever(mealDBService.getRecipe("recipeId")).thenReturn(response)
            whenever(mapper.map(mealResponse)).thenReturn(recipe)

            val result = recipeRepo.getRecipe("recipeId")

            Assert.assertEquals(recipe, result)

            verify(recipeDao, never()).getRecipe("recipeId")
            verify(recipeDao).insertOrUpdate(recipeList)
        }
    }

    @Test
    fun `when getRecipe invoked and service throws error check the local database for result`() {
        runBlocking {
            val recipe = Recipe(
                id = "id",
                name = "name",
                thumbnail = "thumbnail",
                category = "category",
                instruction = "",
                ingredient = emptyList(),
                youtube = ""
            )

            whenever(mealDBService.getRecipe("recipeId")).doSuspendableAnswer {
                throw Exception("Error")
            }
            whenever(recipeDao.getRecipe("recipeId")).thenReturn(recipe)

            val result = recipeRepo.getRecipe("recipeId")

            Assert.assertEquals(recipe, result)

            verify(mapper, never()).map(any())
        }
    }
}