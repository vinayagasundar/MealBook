package com.blackknight.mealbook.data.mapper

import com.blackknight.mealbook.data.entities.Category
import com.blackknight.mealbook.network.response.CategoryResponse
import javax.inject.Inject

class CategoryMapper @Inject constructor() : Mapper<CategoryResponse, Category> {

    override fun map(from: CategoryResponse): Category {
        return Category(
            id = from.id,
            name = from.name,
            thumbnail = from.thumbnail,
            description = from.description
        )
    }
}