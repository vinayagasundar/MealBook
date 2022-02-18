package com.blackknight.mealbook.data.daos

import androidx.room.Dao
import androidx.room.Query
import com.blackknight.mealbook.data.entities.Category
import io.reactivex.rxjava3.core.Single

@Dao
interface CategoryDao : BaseDao<Category> {
    @Query("SELECT * FROM category")
    fun getCategories(): Single<List<Category>>
}