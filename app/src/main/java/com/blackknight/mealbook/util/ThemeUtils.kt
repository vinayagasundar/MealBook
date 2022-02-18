package com.blackknight.mealbook.util

import android.content.Context
import android.util.TypedValue
import android.view.View


fun View.getThemeColor(colorAttrResId: Int): Int {
    return context.getThemeColor(colorAttrResId)
}

fun Context.getThemeColor(colorAttrResId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(colorAttrResId, typedValue, true)
    return typedValue.data
}