package com.blackknight.mealbook.data.mapper

import com.blackknight.mealbook.data.entities.Meal
import com.blackknight.mealbook.network.response.MealResponse
import javax.inject.Inject

class MealMapper @Inject constructor() : Mapper<MealResponse, Meal> {
    override fun map(from: MealResponse): Meal {
        return Meal(
            id = from.id,
            name = from.name,
            thumbnail = from.thumbnail,
            categoryId = ""
        )
    }
}