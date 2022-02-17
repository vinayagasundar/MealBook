package com.blackknight.mealbook.network.response

import com.google.gson.annotations.SerializedName

data class MealsResponse(
    @SerializedName("meals")
    val responses: List<MealRecipeResponse>
)
