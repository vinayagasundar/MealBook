package com.blackknight.mealbook.data.daos

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import io.reactivex.rxjava3.core.Completable

interface BaseDao<E> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(list: List<E>): Completable
}