package com.blackknight.mealbook.data.entities

import com.google.gson.annotations.SerializedName

data class Ingredient(
    @SerializedName("name") val name: String,
    @SerializedName("measurement") val measurement: String
)
