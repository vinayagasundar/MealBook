package com.blackknight.mealbook.ui.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackknight.mealbook.data.entities.Category
import com.blackknight.mealbook.data.entities.Meal
import com.blackknight.mealbook.data.repo.CategoryRepo
import com.blackknight.mealbook.data.repo.MealRepo
import com.blackknight.mealbook.ui.landing.adapter.CategoryItem
import com.blackknight.mealbook.util.Optional
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoadingState {
    object FullLoading : LoadingState()
    object RecipeOnlyLoading : LoadingState()
    object Hide : LoadingState()
}

data class LandingViewState(
    val loadingState: LoadingState,
    val categories: List<CategoryItem> = emptyList(),
    val meals: List<Meal> = emptyList(),
    val isError: Boolean = false
) {

    fun isErrorState(): Boolean {
        return isError || (loadingState == LoadingState.Hide
                && (categories.isEmpty() || meals.isEmpty()))
    }
}

@HiltViewModel
internal class LandingViewModel @Inject constructor(
    private val categoryRepo: CategoryRepo,
    private val mealRepo: MealRepo
) : ViewModel() {

    private val publishSelectCategory = Channel<Optional<Category>>()

    fun onClickCategoryItem(item: CategoryItem) {
        publishSelectCategory.trySend(Optional.Present(item.category))
    }

    @ExperimentalCoroutinesApi
    fun observeViewState(): Flow<LandingViewState> {
        return flow {
            val categories = categoryRepo.getCategories()
            publishSelectCategory.receiveAsFlow()
                .onStart { emit(Optional.Absent) }
                .flatMapLatest { selectedCategoryOpt ->
                    flow {
                        val selectedCategory =
                            (selectedCategoryOpt.getOrNull() ?: categories.firstOrNull())
                        val categoriesItem = categories.map { category ->
                            CategoryItem(
                                category,
                                category.id == selectedCategory?.id
                            )
                        }

                        emit(LandingViewState(LoadingState.RecipeOnlyLoading, categoriesItem))

                        selectedCategory?.let { category ->
                            mealRepo.getMealList(category)
                        }?.also { meals ->
                            emit(LandingViewState(LoadingState.Hide, categoriesItem, meals, false))
                        }
                    }
                }.collect {
                    emit(it)
                }
        }.onStart { emit(LandingViewState(LoadingState.FullLoading)) }
            .catch { emit(LandingViewState(LoadingState.Hide, isError = true)) }
            .flowOn(Dispatchers.Main)
    }
}