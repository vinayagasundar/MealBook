package com.blackknight.mealbook.ui.recipe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blackknight.mealbook.R
import com.blackknight.mealbook.data.entities.Meal
import com.blackknight.mealbook.ui.recipe.adapter.IngredientAdapter
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RecipeDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var ingredientAdapter: IngredientAdapter

    private val viewModel by viewModels<RecipeViewModel>()

    private val materialToolbar by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<MaterialToolbar>(R.id.mtb_title)
    }

    private val tvInstruction by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<TextView>(R.id.tv_instruction)
    }

    private val rvIngredient by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<RecyclerView>(R.id.rv_ingredient)
    }

    private val recipeContainer by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<View>(R.id.ll_recipe_details)
    }

    private val loadingRecipeDetail by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<View>(R.id.loading_recipe_details)
    }

    private val errorLayout by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<View>(R.id.error_layout)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        val id = intent.getStringExtra(KEY_MEAL_ID).orEmpty()
        val title = intent.getStringExtra(KEY_MEAL_TITLE).orEmpty()
        materialToolbar.title = title

        materialToolbar.setNavigationOnClickListener {
            finish()
        }

        rvIngredient.apply {
            itemAnimator = null
            adapter = ingredientAdapter
            (layoutManager as? GridLayoutManager)?.spanCount = 3
        }

        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.RESUMED) {
                val state = viewModel.getRecipe(id)
                when {
                    state.isSuccess -> state.getOrNull()?.apply {
                        recipeContainer.visibility = View.VISIBLE
                        loadingRecipeDetail.visibility = View.GONE
                        materialToolbar.title = name
                        tvInstruction.text = instruction
                        ingredientAdapter.submitList(ingredient)
                    }
                    state.isFailure -> {
                        recipeContainer.visibility = View.GONE
                        loadingRecipeDetail.visibility = View.GONE
                        errorLayout.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    companion object {
        private const val KEY_MEAL_ID = "mealId"
        private const val KEY_MEAL_TITLE = "mealTitle"

        fun getIntent(context: Context, meal: Meal): Intent {
            return Intent(context, RecipeDetailActivity::class.java).apply {
                putExtra(KEY_MEAL_ID, meal.id)
                putExtra(KEY_MEAL_TITLE, meal.name)
            }
        }
    }
}