package com.blackknight.mealbook.database.converter

import androidx.room.TypeConverter
import com.blackknight.mealbook.data.entities.Ingredient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataTypeConverter {
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