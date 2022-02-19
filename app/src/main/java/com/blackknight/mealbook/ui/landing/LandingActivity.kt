package com.blackknight.mealbook.ui.landing

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blackknight.mealbook.R
import com.blackknight.mealbook.ui.landing.adapter.CategoryAdapter
import com.blackknight.mealbook.ui.landing.adapter.MealAdapter
import com.blackknight.mealbook.ui.recipe.RecipeDetailActivity
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

    private val loadingCategory by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<View>(R.id.loading_category)
    }

    private val loadingRecipe by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<View>(R.id.loading_recipe)
    }

    private val tvTitleCategory by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<View>(R.id.tv_title_category)
    }

    private val rvCategories by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<RecyclerView>(R.id.rv_category)
    }

    private val tvRecipeTitle by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<View>(R.id.tv_title_recipe)
    }

    private val rvRecipe by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<RecyclerView>(R.id.rv_recipe)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        rvCategories.apply {
            adapter = categoryAdapter
            (layoutManager as? LinearLayoutManager)?.orientation = LinearLayoutManager.HORIZONTAL
            itemAnimator = null
        }

        rvRecipe.apply {
            itemAnimator = null
            adapter = mealAdapter
        }

        categoryAdapter.setOnClickHandler { categoryItem ->
            viewModel.onClickCategoryItem(categoryItem)
        }

        mealAdapter.setOnClickHandler { meal ->
            startActivity(RecipeDetailActivity.getIntent(this, meal))
        }

        disposable.add(
            viewModel.observeViewState()
                .subscribeBy(defaultErrorHandler) { state ->
                    when (state.loadingState) {
                        is LoadingState.FullLoading -> fullLoading()
                        is LoadingState.RecipeOnlyLoading -> recipeOnlyLoading()
                        is LoadingState.Hide -> hide()
                    }

                    if (state.categories.isNotEmpty()) {
                        categoryAdapter.submitList(state.categories)
                    }

                    if (state.meals.isNotEmpty()) {
                        mealAdapter.submitList(state.meals)
                    }
                }
        )
    }

    private fun hide() {
        loadingCategory.visibility = View.GONE
        loadingRecipe.visibility = View.GONE

        tvTitleCategory.visibility = View.VISIBLE
        rvCategories.visibility = View.VISIBLE

        tvRecipeTitle.visibility = View.VISIBLE
        rvRecipe.visibility = View.VISIBLE
    }

    private fun recipeOnlyLoading() {
        loadingCategory.visibility = View.GONE
        loadingRecipe.visibility = View.VISIBLE

        tvTitleCategory.visibility = View.VISIBLE
        rvCategories.visibility = View.VISIBLE

        tvRecipeTitle.visibility = View.GONE
        rvRecipe.visibility = View.GONE
    }

    private fun fullLoading() {
        loadingCategory.visibility = View.VISIBLE
        loadingRecipe.visibility = View.VISIBLE

        tvTitleCategory.visibility = View.GONE
        rvCategories.visibility = View.GONE

        tvRecipeTitle.visibility = View.GONE
        rvRecipe.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}