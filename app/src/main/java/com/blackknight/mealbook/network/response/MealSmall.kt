package com.blackknight.mealbook.network.response

import com.google.gson.annotations.SerializedName


data class MealSmall(
    @SerializedName("idMeal")
    val id: String,
    @SerializedName("strMeal")
    val name: String,
    @SerializedName("strMealThumb")
    val thumbnail: String
)