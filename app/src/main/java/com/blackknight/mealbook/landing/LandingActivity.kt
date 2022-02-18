package com.blackknight.mealbook.landing

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blackknight.mealbook.R
import com.blackknight.mealbook.landing.adapter.CategoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

@AndroidEntryPoint
class LandingActivity : AppCompatActivity() {

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    private val disposable = CompositeDisposable()

    private val viewModel by viewModels<LandingViewModel>()

    private val categoryRecyclerView by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<RecyclerView>(
            R.id.rv_category
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        categoryRecyclerView.apply {
            adapter = categoryAdapter
            (layoutManager as? LinearLayoutManager)?.orientation = LinearLayoutManager.HORIZONTAL
            itemAnimator = null
        }

        categoryAdapter.setOnClickHandler {
            viewModel.onClickCategoryItem(it)
        }

        disposable.add(
            viewModel.observeViewState()
                .subscribeBy { state ->
                    if (state.categories.isNotEmpty()) {
                        categoryAdapter.submitList(state.categories)
                    }
                }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}