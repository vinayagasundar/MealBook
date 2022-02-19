package com.blackknight.mealbook.data.mapper

import com.blackknight.mealbook.data.entities.Category
import com.blackknight.mealbook.data.entities.Meal
import com.blackknight.mealbook.data.entities.Recipe
import com.blackknight.mealbook.network.response.CategoryResponse
import com.blackknight.mealbook.network.response.RecipeResponse
import com.blackknight.mealbook.network.response.MealResponse
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MapperModule {
    @Binds
    abstract fun categoryMapper(categoryMapper: CategoryMapper): Mapper<CategoryResponse, Category>

    @Binds
    abstract fun mealMapper(mealMapper: MealMapper): Mapper<MealResponse, Meal>

    @Binds
    abstract fun recipeMapper(recipeMapper: RecipeMapper): Mapper<RecipeResponse, Recipe>
}