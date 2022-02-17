package com.blackknight.mealbook.data.daos

import androidx.room.Dao
import com.blackknight.mealbook.data.entities.Meal

@Dao
interface MealDao : BaseDao<Meal>