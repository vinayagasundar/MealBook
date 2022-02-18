package com.blackknight.mealbook.data.entities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object IngredientConverter {
    private val gson by lazy { Gson() }
    private val typeToken by lazy {
        object : TypeToken<List<Ingredient>>() {}.type
    }

    @TypeConverter
    fun fromIngredients(list: List<Ingredient>): String? {
        return try {
            gson.toJson(list, typeToken)
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun toIngredients(value: String?): List<Ingredient> {
        return try {
            gson.fromJson(value, typeToken)
        } catch (e: Exception) {
            emptyList()
        }
    }


}