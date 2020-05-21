package com.vladtruta.kitchenapp.presentation.orders.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vladtruta.kitchenapp.R
import com.vladtruta.kitchenapp.databinding.ListItemDetailsBinding
import com.vladtruta.kitchenapp.data.model.local.CartItem
import com.vladtruta.kitchenapp.utils.ImageHelper
import com.vladtruta.kitchenapp.utils.UIUtils

class OrderDetailsAdapter :
    ListAdapter<CartItem, OrderDetailsAdapter.ViewHolder>(OrderDetailsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemDetailsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ListItemDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItem) {
            ImageHelper.loadImage(itemView.context, binding.courseIv, cartItem.menuCourse.photoUrl)
            binding.courseNameTv.text = cartItem.menuCourse.name
            binding.courseQuantityTv.text =
                UIUtils.getString(R.string.quantity_placeholder, cartItem.quantity)
        }
    }
}

private class OrderDetailsDiffCallback : DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem == newItem
    }
}