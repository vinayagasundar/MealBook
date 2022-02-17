package com.blackknight.mealbook.database

import android.content.Context
import androidx.room.Room
import com.blackknight.mealbook.data.MealDatabase
import com.blackknight.mealbook.data.daos.CategoryDao
import com.blackknight.mealbook.data.daos.MealDao
import com.blackknight.mealbook.data.daos.RecipeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMealDatabase(@ApplicationContext context: Context): MealDatabase =
        Room.databaseBuilder(context, RoomMealDatabase::class.java, RoomMealDatabase.NAME)
            .build()

    @Provides
    @Singleton
    fun provideCategoryDao(mealDatabase: MealDatabase): CategoryDao = mealDatabase.getCategoryDao()

    @Provides
    @Singleton
    fun provideMealDao(mealDatabase: MealDatabase): MealDao = mealDatabase.getMealDao()

    @Provides
    @Singleton
    fun provideRecipeDao(mealDatabase: MealDatabase): RecipeDao = mealDatabase.getRecipe()
}