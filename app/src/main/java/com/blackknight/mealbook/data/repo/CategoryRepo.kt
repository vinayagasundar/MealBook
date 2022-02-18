package com.blackknight.mealbook.data.repo

import com.blackknight.mealbook.data.daos.CategoryDao
import com.blackknight.mealbook.data.entities.Category
import com.blackknight.mealbook.data.mapper.Mapper
import com.blackknight.mealbook.network.MealDBService
import com.blackknight.mealbook.network.response.CategoryResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface CategoryRepo {
    fun getCategories(fromNetwork: Boolean = false): Single<List<Category>>
}

class CategoryRepoImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val mealDBService: MealDBService,
    private val mapper: Mapper<CategoryResponse, Category>
) : CategoryRepo {
    override fun getCategories(fromNetwork: Boolean): Single<List<Category>> {
        return if (fromNetwork) {
            getAndSaveCategories()
        } else {
            categoryDao.getCategories()
                .flatMap { categories ->
                    if (categories.isEmpty()) {
                        getAndSaveCategories()
                    } else {
                        Single.just(categories)
                    }
                }
        }
    }

    private fun getAndSaveCategories(): Single<List<Category>> {
        return mealDBService.getCategories()
            .map { response ->
                response.list.map { mapper.map(it) }
            }
            .flatMap {
                categoryDao.insertOrUpdate(it)
                    .andThen(Single.just(it))
            }
    }
}