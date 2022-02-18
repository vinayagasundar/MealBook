package com.blackknight.mealbook.landing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blackknight.mealbook.R
import com.blackknight.mealbook.data.daos.CategoryDao
import com.blackknight.mealbook.data.entities.Category
import com.blackknight.mealbook.data.mapper.Mapper
import com.blackknight.mealbook.network.MealDBService
import com.blackknight.mealbook.network.response.CategoryResponse
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class LandingActivity : AppCompatActivity() {

    @Inject
    lateinit var service: MealDBService

    @Inject
    lateinit var categoryDao: CategoryDao

    @Inject
    lateinit var categoryMapper: Mapper<CategoryResponse, Category>

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

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
        }

        service.getCategories()
            .map { response ->
                response.list.map { from ->
                    categoryMapper.map(from)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { value ->
                categoryAdapter.submitList(value)
            }
    }
}