package com.blackknight.mealbook.data.daos

import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDao<E> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(list: List<E>)
}