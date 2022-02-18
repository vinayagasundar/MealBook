package com.blackknight.mealbook.util

import android.util.Log

val defaultErrorHandler: (error: Throwable) -> Unit
    get() = {
        Log.e("Meal Book", it.toString())
    }