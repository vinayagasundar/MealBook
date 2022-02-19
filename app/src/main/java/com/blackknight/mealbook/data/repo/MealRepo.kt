package com.blackknight.mealbook.data.repo

import com.blackknight.mealbook.data.daos.MealDao
import com.blackknight.mealbook.data.entities.Category
import com.blackknight.mealbook.data.entities.Meal
import com.blackknight.mealbook.data.mapper.Mapper
import com.blackknight.mealbook.network.MealDBService
import com.blackknight.mealbook.network.response.MealResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface MealRepo {
    fun getMealList(category: Category): Single<List<Meal>>
}


class MealRepoImpl @Inject constructor(
    private val mealDao: MealDao,
    private val mealDBService: MealDBService,
    private val mapper: Mapper<MealResponse, Meal>
) : MealRepo {
    override fun getMealList(category: Category): Single<List<Meal>> {
        return mealDao.getMealList(category.id)
            .flatMap { meals ->
                if (meals.isEmpty()) {
                    getAndSaveMeals(category)
                } else {
                    Single.just(meals)
                }
            }

    }

    private fun getAndSaveMeals(category: Category): Single<List<Meal>> {
        return mealDBService.getMealByCategory(category.name)
            .map { response ->
                response.list.map {
                    mapper.map(it).copy(categoryId = category.id)
                }
            }
            .flatMap {
                mealDao.insertOrUpdate(it)
                    .andThen(Single.just(it))
            }
    }
}

