package com.blackknight.mealbook.ui.landing

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blackknight.mealbook.R
import com.blackknight.mealbook.ui.landing.adapter.CategoryAdapter
import com.blackknight.mealbook.ui.landing.adapter.MealAdapter
import com.blackknight.mealbook.util.defaultErrorHandler
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

@AndroidEntryPoint
class LandingActivity : AppCompatActivity() {

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    @Inject
    lateinit var mealAdapter: MealAdapter

    private val disposable = CompositeDisposable()

    private val viewModel by viewModels<LandingViewModel>()

    private val categoryRecyclerView by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<RecyclerView>(R.id.rv_category)
    }

    private val mealRecyclerView by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<RecyclerView>(R.id.rv_meal)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        categoryRecyclerView.apply {
            adapter = categoryAdapter
            (layoutManager as? LinearLayoutManager)?.orientation = LinearLayoutManager.HORIZONTAL
            itemAnimator = null
        }

        mealRecyclerView.apply {
            itemAnimator = null
            adapter = mealAdapter
        }

        categoryAdapter.setOnClickHandler {
            viewModel.onClickCategoryItem(it)
        }

        disposable.add(
            viewModel.observeViewState()
                .subscribeBy(defaultErrorHandler) { state ->
                    if (state.categories.isNotEmpty()) {
                        categoryAdapter.submitList(state.categories)
                    }

                    if (state.meals.isNotEmpty()) {
                        mealAdapter.submitList(state.meals)
                    }
                }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}