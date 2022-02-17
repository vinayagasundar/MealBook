package com.blackknight.mealbook.data

import com.blackknight.mealbook.data.daos.CategoryDao
import com.blackknight.mealbook.data.daos.MealDao
import com.blackknight.mealbook.data.daos.RecipeDao

interface MealDatabase {
    fun getCategoryDao(): CategoryDao
    fun getMealDao(): MealDao
    fun getRecipe(): RecipeDao
}