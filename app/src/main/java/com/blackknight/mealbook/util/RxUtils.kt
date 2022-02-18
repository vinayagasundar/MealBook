package com.blackknight.mealbook.util

import android.util.Log

fun defaultErrorHandler(error: Throwable) {
    Log.e("Meal Book", error.toString())
}