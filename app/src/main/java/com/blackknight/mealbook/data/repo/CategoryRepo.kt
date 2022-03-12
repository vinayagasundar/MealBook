package com.blackknight.mealbook.data.repo

import com.blackknight.mealbook.data.daos.CategoryDao
import com.blackknight.mealbook.data.entities.Category
import com.blackknight.mealbook.data.mapper.Mapper
import com.blackknight.mealbook.network.MealDBService
import com.blackknight.mealbook.network.response.CategoryResponse
import java.lang.Exception
import javax.inject.Inject

interface CategoryRepo {
    suspend fun getCategories(): List<Category>
}

class CategoryRepoImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val mealDBService: MealDBService,
    private val mapper: Mapper<CategoryResponse, Category>
) : CategoryRepo {
    override suspend fun getCategories(): List<Category> {
        val categories = getAndSaveCategories()
        return categories.ifEmpty { categoryDao.getCategories() }
    }

    private suspend fun getAndSaveCategories(): List<Category> {
        return try {
            val categoriesResponse = mealDBService.getCategories()
            categoriesResponse.list.map { mapper.map(it) }
                .also { list ->
                    categoryDao.insertOrUpdate(list)
                }
        } catch (e: Exception) {
            emptyList()
        }

    }
}