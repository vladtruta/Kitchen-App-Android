package com.vladtruta.kitchenapp.presentation.orders.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vladtruta.kitchenapp.R
import com.vladtruta.kitchenapp.databinding.ListItemOrderBinding
import com.vladtruta.kitchenapp.data.model.local.KitchenOrder
import com.vladtruta.kitchenapp.utils.UIUtils
import com.vladtruta.kitchenapp.utils.setRipple

class OrdersListAdapter(private val listener: OrdersListListener) :
    ListAdapter<KitchenOrder, OrdersListAdapter.ViewHolder>(OrdersListDiffCallback()) {

    var checkedPosition = 0
        set(value) {
            notifyItemChanged(field)
            field = value
            notifyItemChanged(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ListItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            initActions()
        }

        private fun initActions() {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }

                checkedPosition = position
                listener.onOrderListItemClicked(getItem(position))
            }
        }

        fun bind(kitchenOrder: KitchenOrder) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (position == checkedPosition) {
                    binding.root.setBackgroundColor(UIUtils.getColor(R.color.colorPrimaryVariant))
                } else {
                    binding.root.setRipple()
                }
            }
            binding.orderNumberTv.text =
                UIUtils.getString(R.string.order_number_placeholder, kitchenOrder.id)
            binding.tableNameTv.text = kitchenOrder.tableName
        }
    }

    interface OrdersListListener {
        fun onOrderListItemClicked(kitchenOrder: KitchenOrder)
    }
}

private class OrdersListDiffCallback : DiffUtil.ItemCallback<KitchenOrder>() {
    override fun areItemsTheSame(oldItem: KitchenOrder, newItem: KitchenOrder): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: KitchenOrder, newItem: KitchenOrder): Boolean {
        return oldItem == newItem
    }
}