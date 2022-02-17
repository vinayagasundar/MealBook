package com.blackknight.mealbook.landing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blackknight.mealbook.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
    }
}