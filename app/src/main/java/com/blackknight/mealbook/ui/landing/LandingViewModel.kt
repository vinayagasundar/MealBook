package com.blackknight.mealbook.ui.landing

import androidx.lifecycle.ViewModel
import com.blackknight.mealbook.data.entities.Category
import com.blackknight.mealbook.data.entities.Meal
import com.blackknight.mealbook.data.repo.CategoryRepo
import com.blackknight.mealbook.data.repo.MealRepo
import com.blackknight.mealbook.ui.landing.adapter.CategoryItem
import com.blackknight.mealbook.util.Optional
import com.blackknight.mealbook.util.SchedulerProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

sealed class LoadingState {
    object FullLoading : LoadingState()
    object RecipeOnlyLoading : LoadingState()
    object Hide : LoadingState()
}

data class LandingViewState(
    val loadingState: LoadingState,
    val categories: List<CategoryItem> = emptyList(),
    val meals: List<Meal> = emptyList()
)

@HiltViewModel
internal class LandingViewModel @Inject constructor(
    private val categoryRepo: CategoryRepo,
    private val mealRepo: MealRepo,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    private val publishSelectCategory = PublishSubject.create<Optional<Category>>()

    fun onClickCategoryItem(item: CategoryItem) {
        publishSelectCategory.onNext(Optional.Present(item.category))
    }

    fun observeViewState(): Observable<LandingViewState> {
        return observeCategories().observeOn(schedulerProvider.io())
            .switchMap { categories -> observableMealList(categories) }
            .startWithItem(LandingViewState(LoadingState.FullLoading))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
    }

    private fun observableMealList(categories: List<CategoryItem>): @NonNull Observable<LandingViewState> {
        val selectedCategory = categories.firstOrNull { it.isSelected }?.category
        return if (selectedCategory != null) {
            mealRepo.getMealList(selectedCategory)
                .map {
                    LandingViewState(LoadingState.Hide, categories, it)
                }.toObservable()
        } else {
            Observable.just(LandingViewState(LoadingState.RecipeOnlyLoading, categories))
        }.startWithItem(LandingViewState(LoadingState.RecipeOnlyLoading, categories))
    }

    private fun observeCategories(): Observable<List<CategoryItem>> {
        return Observable.combineLatest(
            categoryRepo.getCategories().toObservable(),
            publishSelectCategory.startWithItem(Optional.Absent)
        ) { categories, selectedCategory ->
            val categoryId = (selectedCategory.getOrNull() ?: categories.getOrNull(0))?.id
            categories.map { category -> CategoryItem(category, categoryId == category.id) }
        }
    }
}