package com.blackknight.mealbook.ui.landing

import com.blackknight.mealbook.data.entities.Category
import com.blackknight.mealbook.data.entities.Meal
import com.blackknight.mealbook.data.repo.CategoryRepo
import com.blackknight.mealbook.data.repo.MealRepo
import com.blackknight.mealbook.ui.landing.adapter.CategoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class LandingViewModelTest {

    @Mock
    private lateinit var categoryRepo: CategoryRepo

    @Mock
    private lateinit var mealRepo: MealRepo

    private lateinit var viewModel: LandingViewModel

    private val categories = listOf(
        Category("1", "name1", "thumbnail1", "description1"),
        Category("2", "name2", "thumbnail2", "description2")
    )

    private val meals = listOf(
        Meal("1", "name1", "thumbnail1", "1"),
        Meal("2", "name2", "thumbnail2", "2")
    )

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = LandingViewModel(categoryRepo, mealRepo)
        runBlocking {
            whenever(categoryRepo.getCategories()).thenReturn(categories)
            whenever(mealRepo.getMealList(categories[0])).thenReturn(meals.subList(0, 1))
            whenever(mealRepo.getMealList(categories[1])).thenReturn(meals.subList(1, 1))
        }
    }

    @Test
    @Ignore
    fun `when observeViewState invoke should return the categories and recipe names`() {
        runBlocking {
            val result = withTimeout(5000) {
                withContext(Dispatchers.Default) {
                    viewModel.observeViewState()
                        .take(5)
                        .toList()
                }
            }

            viewModel.onClickCategoryItem(CategoryItem(categories[1], false))

            val firstTimeCategories = categories.mapIndexed { index, category ->
                CategoryItem(category, index == 0)
            }

            val secondTimeCategories = categories.mapIndexed { index, category ->
                CategoryItem(category, index == 1)
            }

            val expected = listOf(
                LandingViewState(LoadingState.FullLoading),
                LandingViewState(LoadingState.RecipeOnlyLoading, firstTimeCategories),
                LandingViewState(LoadingState.Hide, firstTimeCategories, meals.subList(0, 1)),
                LandingViewState(LoadingState.RecipeOnlyLoading, secondTimeCategories),
                LandingViewState(LoadingState.Hide, secondTimeCategories, meals.subList(1, 1))
            )
            Assert.assertEquals(expected, result)
        }
    }

    @Test
    fun `when observeViewState invoke and repo return error should set isError in the state`() {
        runBlocking {
            whenever(categoryRepo.getCategories()).doSuspendableAnswer {
                throw Exception("Error")
            }
            val result = viewModel.observeViewState()
                .toList()

            Assert.assertEquals(
                listOf(
                    LandingViewState(LoadingState.FullLoading),
                    LandingViewState(LoadingState.Hide, isError = true)
                ), result
            )
        }
    }
}