package com.blackknight.mealbook.data.repo

import com.blackknight.mealbook.data.daos.RecipeDao
import com.blackknight.mealbook.data.entities.Recipe
import com.blackknight.mealbook.data.mapper.Mapper
import com.blackknight.mealbook.network.MealDBService
import com.blackknight.mealbook.network.response.MealRecipeResponse
import com.blackknight.mealbook.network.response.MealsResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class RecipeRepoImplTest {

    @Mock
    private lateinit var recipeDao: RecipeDao

    @Mock
    private lateinit var mealDBService: MealDBService

    @Mock
    private lateinit var mapper: Mapper<MealRecipeResponse, Recipe>

    private lateinit var recipeRepo: RecipeRepoImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        recipeRepo = RecipeRepoImpl(mealDBService, recipeDao, mapper)
    }

    @Test
    fun `when getRecipe invoked and dao return exception and call api service `() {
        val mealResponse = MealRecipeResponse(
            id = "id",
            name = "name",
            thumbnail = "thumbnail",
            categories = "category"
        )
        val response = MealsResponse(listOf(mealResponse))
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

        whenever(recipeDao.getRecipe("recipeId")).thenReturn(Single.error(Exception("No Data Found")))
        whenever(mealDBService.getMeal("recipeId")).thenReturn(Single.just(response))
        whenever(mapper.map(mealResponse)).thenReturn(recipe)
        whenever(recipeDao.insertOrUpdate(recipeList)).thenReturn(Completable.complete())

        recipeRepo.getRecipe("recipeId")
            .test()
            .assertValue(recipe)
            .assertNoErrors()
            .assertComplete()
            .dispose()
    }

    @Test
    fun `when getRecipe invoked and dao return Recipe and should not api service`() {
        val recipe = Recipe(
            id = "id",
            name = "name",
            thumbnail = "thumbnail",
            category = "category",
            instruction = "",
            ingredient = emptyList(),
            youtube = ""
        )

        whenever(recipeDao.getRecipe("recipeId")).thenReturn(Single.just(recipe))

        recipeRepo.getRecipe("recipeId")
            .test()
            .assertValue(recipe)
            .assertNoErrors()
            .assertComplete()
            .dispose()
    }
}