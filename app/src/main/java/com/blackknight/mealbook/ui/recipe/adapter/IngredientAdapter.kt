package com.blackknight.mealbook.ui.recipe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blackknight.mealbook.R
import com.blackknight.mealbook.data.entities.Ingredient
import javax.inject.Inject

private val DIFF = object : DiffUtil.ItemCallback<Ingredient>() {
    override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem == newItem
    }

}

class IngredientAdapter @Inject constructor() :
    ListAdapter<Ingredient, IngredientAdapter.IngredientViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class IngredientViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val tvName by lazy(LazyThreadSafetyMode.NONE) {
            view.findViewById<TextView>(R.id.tv_ingredient_name)
        }
        private val tvMeasure by lazy(LazyThreadSafetyMode.NONE) {
            view.findViewById<TextView>(R.id.tv_ingredient_measure)
        }

        fun bind(ingredient: Ingredient) {
            ingredient.apply {
                tvName.text = name
                tvMeasure.text = measurement
            }
        }
    }
}