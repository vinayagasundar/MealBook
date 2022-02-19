package com.blackknight.mealbook.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.blackknight.mealbook.data.MealDatabase
import com.blackknight.mealbook.data.entities.Category
import com.blackknight.mealbook.data.entities.IngredientConverter
import com.blackknight.mealbook.data.entities.Meal
import com.blackknight.mealbook.data.entities.Recipe

@Database(
    entities = [
        Category::class,
        Meal::class,
        Recipe::class
    ],
    version = MealDatabase.VERSION
)
@TypeConverters(
    IngredientConverter::class
)
abstract class RoomMealDatabase : RoomDatabase(), MealDatabase