package com.blackknight.mealbook.network.response

import com.google.gson.annotations.SerializedName

data class RecipeListResponse(
    @SerializedName("meals")
    val list: List<RecipeResponse>
)
