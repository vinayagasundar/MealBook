package com.blackknight.mealbook.ui.landing.adapter

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
import com.blackknight.mealbook.util.defaultErrorLogger
import com.blackknight.mealbook.util.getThemeColor
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

private val DIFF = object : DiffUtil.ItemCallback<CategoryItem>() {
    override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
        return oldItem.category.id == newItem.category.id
    }

    override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
        return oldItem == newItem
    }
}

@ActivityScoped
class CategoryAdapter @Inject constructor() :
    ListAdapter<CategoryItem, CategoryAdapter.CategoryViewHolder>(DIFF) {

    private var handler: CategoryOnClickHandler? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view, handler)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnClickHandler(clickHandler: CategoryOnClickHandler) {
        handler = clickHandler
    }

    class CategoryViewHolder(
        private val view: View,
        private val handler: CategoryOnClickHandler?
    ) : RecyclerView.ViewHolder(view) {

        private val name by lazy(LazyThreadSafetyMode.NONE) {
            view.findViewById<TextView>(R.id.tv_category_name)
        }
        private val image by lazy(LazyThreadSafetyMode.NONE) {
            view.findViewById<ImageView>(R.id.iv_category_image)
        }
        private val imageLoader by lazy { view.context.imageLoader }
        private var disposable: Disposable? = null

        fun bind(item: CategoryItem) {
            val category = item.category
            name.text = category.name
            if (item.isSelected) {
                name.setTextColor(
                    view.getThemeColor(com.google.android.material.R.attr.colorPrimary)
                )
            } else {
                name.setTextColor(
                    view.getThemeColor(com.google.android.material.R.attr.colorOnBackground)
                )
            }

            disposable?.dispose()
            val request = ImageRequest.Builder(view.context)
                .placeholder(R.drawable.ic_restaurent)
                .error(R.drawable.ic_restaurent)
                .data(category.thumbnail)
                .transformations(CircleCropTransformation())
                .listener(onError = { _, e ->
                    defaultErrorLogger(e)
                })
                .target(image)
                .build()
            disposable = imageLoader.enqueue(request)

            view.setOnClickListener {
                handler?.onClick(item)
            }
        }
    }

    fun interface CategoryOnClickHandler {
        fun onClick(item: CategoryItem)
    }
}