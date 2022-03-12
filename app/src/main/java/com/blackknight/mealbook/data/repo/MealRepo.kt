package com.blackknight.mealbook.data.repo

import com.blackknight.mealbook.data.daos.MealDao
import com.blackknight.mealbook.data.entities.Category
import com.blackknight.mealbook.data.entities.Meal
import com.blackknight.mealbook.data.mapper.Mapper
import com.blackknight.mealbook.network.MealDBService
import com.blackknight.mealbook.network.response.MealResponse
import java.lang.Exception
import javax.inject.Inject

interface MealRepo {
    suspend fun getMealList(category: Category): List<Meal>
}


class MealRepoImpl @Inject constructor(
    private val mealDao: MealDao,
    private val mealDBService: MealDBService,
    private val mapper: Mapper<MealResponse, Meal>
) : MealRepo {
    override suspend fun getMealList(category: Category): List<Meal> {
        val meals = getAndSaveMeals(category)
        return meals.ifEmpty { mealDao.getMealList(category.id) }
    }

    private suspend fun getAndSaveMeals(category: Category): List<Meal> {
        return try {
            val response = mealDBService.getMealByCategory(category.name)
            response.list.map {
                mapper.map(it).copy(categoryId = category.id)
            }.also {
                mealDao.insertOrUpdate(it)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

