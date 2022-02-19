package com.blackknight.mealbook.ui.landing

import com.blackknight.mealbook.data.entities.Category
import com.blackknight.mealbook.data.entities.Meal
import com.blackknight.mealbook.data.repo.CategoryRepo
import com.blackknight.mealbook.data.repo.MealRepo
import com.blackknight.mealbook.fake.FakeSchedulerProvider
import com.blackknight.mealbook.ui.landing.adapter.CategoryItem
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class LandingViewModelTest {

    @Mock
    private lateinit var categoryRepo: CategoryRepo

    @Mock
    private lateinit var mealRepo: MealRepo

    private val schedulerProvider = FakeSchedulerProvider()

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
        viewModel = LandingViewModel(categoryRepo, mealRepo, schedulerProvider)
        whenever(categoryRepo.getCategories()).thenReturn(Single.just(categories))
        whenever(mealRepo.getMealList(categories[0])).thenReturn(Single.just(meals.subList(0, 1)))
        whenever(mealRepo.getMealList(categories[1])).thenReturn(Single.just(meals.subList(1, 1)))
    }

    @Test
    fun `when observeViewState invoke should return the categories and recipe names`() {
        val observer = viewModel.observeViewState()
            .test()

        viewModel.onClickCategoryItem(CategoryItem(categories[1], false))

        val firstTimeCategories = categories.mapIndexed { index, category ->
            CategoryItem(category, index == 0)
        }

        val secondTimeCategories = categories.mapIndexed { index, category ->
            CategoryItem(category, index == 1)
        }
        observer.assertValues(
            LandingViewState(LoadingState.FullLoading),
            LandingViewState(LoadingState.RecipeOnlyLoading, firstTimeCategories),
            LandingViewState(LoadingState.Hide, firstTimeCategories, meals.subList(0, 1)),
            LandingViewState(LoadingState.RecipeOnlyLoading, secondTimeCategories),
            LandingViewState(LoadingState.Hide, secondTimeCategories, meals.subList(1, 1)),
        )
            .assertNoErrors()
            .assertNotComplete()
            .dispose()
    }

    @Test
    fun `when observeViewState invoke and repo return error should set isError in the state`() {
        whenever(categoryRepo.getCategories()).thenReturn(Single.error(Exception("Error")))
        val observer = viewModel.observeViewState()
            .test()

        observer.assertValues(
            LandingViewState(LoadingState.FullLoading),
            LandingViewState(LoadingState.Hide, isError = true),
        )
            .assertNoErrors()
            .assertComplete()
            .dispose()
    }
}