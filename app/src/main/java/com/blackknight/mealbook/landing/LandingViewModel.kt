package com.blackknight.mealbook.landing

import androidx.lifecycle.ViewModel
import com.blackknight.mealbook.data.entities.Category
import com.blackknight.mealbook.data.repo.CategoryRepo
import com.blackknight.mealbook.landing.adapter.CategoryItem
import com.blackknight.mealbook.util.Optional
import com.blackknight.mealbook.util.SchedulerProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

data class LandingViewState(
    val isLoading: Boolean,
    val categories: List<CategoryItem> = emptyList()
)

@HiltViewModel
internal class LandingViewModel @Inject constructor(
    private val categoryRepo: CategoryRepo,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    private val publishSelectCategory = PublishSubject.create<Optional<Category>>()

    fun onClickCategoryItem(item: CategoryItem) {
        publishSelectCategory.onNext(Optional.Present(item.category))
    }

    fun observeViewState(): Observable<LandingViewState> {
        return observeCategories().map {
            LandingViewState(false, it)
        }.startWithItem(LandingViewState(true))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
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