package com.blackknight.mealbook.data.daos

import androidx.room.Dao
import com.blackknight.mealbook.data.entities.Recipe

@Dao
interface RecipeDao : BaseDao<Recipe>