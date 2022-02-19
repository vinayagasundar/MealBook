package com.blackknight.mealbook.network

import com.blackknight.mealbook.network.response.CategoryListResponse
import com.blackknight.mealbook.network.response.MealListResponse
import com.blackknight.mealbook.network.response.RecipeListResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MealDBService {
    @GET("categories.php")
    fun getCategories(): Single<CategoryListResponse>

    @GET("filter.php")
    fun getMealByCategory(@Query("c") categoryId: String): Single<MealListResponse>

    @GET("lookup.php")
    fun getRecipe(@Query("i") id: String): Single<RecipeListResponse>
}