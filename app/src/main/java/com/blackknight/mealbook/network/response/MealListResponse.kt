package com.blackknight.mealbook.network.response

import com.google.gson.annotations.SerializedName


data class MealListResponse(
    @SerializedName("meals")
    val list: List<MealResponse>
)