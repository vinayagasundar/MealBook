package com.blackknight.mealbook.network.response

import com.google.gson.annotations.SerializedName

class CategoryListResponse(
    @SerializedName("categories")
    val list: List<CategoryResponse>
)