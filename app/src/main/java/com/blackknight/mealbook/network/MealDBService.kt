package com.blackknight.mealbook.network

import com.blackknight.mealbook.network.response.CategoryListResponse
import com.blackknight.mealbook.network.response.MealListResponse
import com.blackknight.mealbook.network.response.RecipeListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealDBService {
    @GET("categories.php")
    suspend fun getCategories(): CategoryListResponse

    @GET("filter.php")
    suspend fun getMealByCategory(@Query("c") categoryId: String): MealListResponse

    @GET("lookup.php")
    suspend fun getRecipe(@Query("i") id: String): RecipeListResponse
}