package com.blackknight.mealbook.data.mapper

import com.blackknight.mealbook.data.entities.Ingredient
import com.blackknight.mealbook.data.entities.Recipe
import com.blackknight.mealbook.network.response.RecipeResponse
import javax.inject.Inject

class RecipeMapper @Inject constructor() : Mapper<RecipeResponse, Recipe> {

    override fun map(from: RecipeResponse): Recipe {
        return Recipe(
            id = from.id,
            name = from.name,
            category = from.categories,
            instruction = from.instructions.orEmpty(),
            thumbnail = from.thumbnail.orEmpty(),
            youtube = from.youtube.orEmpty(),
            ingredient = toIngredients(from)
        )
    }

    fun toIngredients(from: RecipeResponse): List<Ingredient> {
        val list = mutableListOf<Ingredient>()
        getIngredient(from.strIngredient1, from.strMeasure1)?.let { list.add(it) }
        getIngredient(from.strIngredient2, from.strMeasure2)?.let { list.add(it) }
        getIngredient(from.strIngredient3, from.strMeasure3)?.let { list.add(it) }
        getIngredient(from.strIngredient4, from.strMeasure4)?.let { list.add(it) }
        getIngredient(from.strIngredient5, from.strMeasure5)?.let { list.add(it) }
        getIngredient(from.strIngredient6, from.strMeasure6)?.let { list.add(it) }
        getIngredient(from.strIngredient7, from.strMeasure7)?.let { list.add(it) }
        getIngredient(from.strIngredient8, from.strMeasure8)?.let { list.add(it) }
        getIngredient(from.strIngredient9, from.strMeasure9)?.let { list.add(it) }
        getIngredient(from.strIngredient10, from.strMeasure10)?.let { list.add(it) }
        getIngredient(from.strIngredient11, from.strMeasure11)?.let { list.add(it) }
        getIngredient(from.strIngredient12, from.strMeasure12)?.let { list.add(it) }
        getIngredient(from.strIngredient13, from.strMeasure13)?.let { list.add(it) }
        getIngredient(from.strIngredient14, from.strMeasure14)?.let { list.add(it) }
        getIngredient(from.strIngredient15, from.strMeasure15)?.let { list.add(it) }
        getIngredient(from.strIngredient16, from.strMeasure16)?.let { list.add(it) }
        getIngredient(from.strIngredient17, from.strMeasure17)?.let { list.add(it) }
        getIngredient(from.strIngredient18, from.strMeasure18)?.let { list.add(it) }
        getIngredient(from.strIngredient19, from.strMeasure19)?.let { list.add(it) }
        getIngredient(from.strIngredient20, from.strMeasure20)?.let { list.add(it) }
        return list
    }

    fun getIngredient(ingredient: String?, measure: String?): Ingredient? {
        if (ingredient.isNullOrBlank() || measure.isNullOrEmpty()) return null
        return Ingredient(ingredient, measure)
    }
}