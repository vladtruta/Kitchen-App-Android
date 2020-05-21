package com.vladtruta.kitchenapp.presentation.orders.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vladtruta.kitchenapp.R
import com.vladtruta.kitchenapp.databinding.ListItemCourseBinding
import com.vladtruta.kitchenapp.data.model.local.CartItem
import com.vladtruta.kitchenapp.utils.UIUtils

class TotalCoursesAdapter :
    ListAdapter<CartItem, TotalCoursesAdapter.ViewHolder>(TotalCoursesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemCourseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ListItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItem) {
            binding.quantityTv.text =
                UIUtils.getString(R.string.quantity_placeholder_reversed, cartItem.quantity)
            binding.nameTv.text = cartItem.menuCourse.name
        }
    }
}

private class TotalCoursesDiffCallback : DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem == newItem
    }
}