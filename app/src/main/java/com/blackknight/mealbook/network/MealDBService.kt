package com.blackknight.mealbook.network

import com.blackknight.mealbook.network.response.CategoriesResponse
import com.blackknight.mealbook.network.response.MealListResponse
import com.blackknight.mealbook.network.response.MealsResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MealDBService {
    @GET("categories.php")
    fun getCategories(): Single<CategoriesResponse>

    @GET("filter.php")
    fun getFoodListByCategory(@Query("c") categoryId: String): Single<MealListResponse>

    @GET("lookup.php")
    fun getMeal(@Query("i") id: String): Single<MealsResponse>
}