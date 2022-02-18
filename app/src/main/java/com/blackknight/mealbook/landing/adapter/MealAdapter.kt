package com.blackknight.mealbook.landing.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.blackknight.mealbook.R
import com.blackknight.mealbook.data.entities.Meal
import com.blackknight.mealbook.util.defaultErrorHandler
import javax.inject.Inject

private val DIFF = object : DiffUtil.ItemCallback<Meal>() {
    override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
        return oldItem == newItem
    }
}

class MealAdapter @Inject constructor() : ListAdapter<Meal, MealAdapter.MealViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MealViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val name by lazy(LazyThreadSafetyMode.NONE) {
            view.findViewById<TextView>(R.id.tv_meal_name)
        }
        private val image by lazy(LazyThreadSafetyMode.NONE) {
            view.findViewById<ImageView>(R.id.iv_meal)
        }
        private val imageLoader by lazy { view.context.imageLoader }
        private var disposable: Disposable? = null

        fun bind(meal: Meal) {
            name.text = meal.name
            disposable?.dispose()
            val request = ImageRequest.Builder(view.context)
                .data(meal.thumbnail)
                .placeholder(R.drawable.ic_restaurent)
                .error(R.drawable.ic_restaurent)
                .transformations(CircleCropTransformation())
                .listener(onError = { _, e ->
                    defaultErrorHandler(e)
                })
                .target(image)
                .build()
            disposable = imageLoader.enqueue(request)
        }
    }
}