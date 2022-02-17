package com.blackknight.mealbook.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.blackknight.mealbook.data.MealDatabase
import com.blackknight.mealbook.data.entities.Category
import com.blackknight.mealbook.data.entities.Meal
import com.blackknight.mealbook.data.entities.Recipe
import com.blackknight.mealbook.database.converter.DataTypeConverter

@Database(
    entities = [
        Category::class,
        Meal::class,
        Recipe::class
    ],
    version = RoomMealDatabase.VERSION
)
@TypeConverters(
    DataTypeConverter::class
)
abstract class RoomMealDatabase : RoomDatabase(), MealDatabase {
    companion object {
        const val NAME = "mealbook.db"
        const val VERSION = 1
    }
}