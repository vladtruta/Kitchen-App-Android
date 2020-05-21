package com.vladtruta.kitchenapp.presentation.orders

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.vladtruta.kitchenapp.R
import com.vladtruta.kitchenapp.databinding.ActivityOrdersBinding
import com.vladtruta.kitchenapp.data.model.local.KitchenOrder
import com.vladtruta.kitchenapp.presentation.orders.adapter.OrderDetailsAdapter
import com.vladtruta.kitchenapp.presentation.orders.adapter.OrdersListAdapter
import com.vladtruta.kitchenapp.presentation.orders.adapter.TotalCoursesAdapter

class OrdersActivity : AppCompatActivity(), OrdersListAdapter.OrdersListListener {

    private lateinit var binding: ActivityOrdersBinding
    private val viewModel by viewModels<OrdersViewModel>()

    private lateinit var ordersListAdapter: OrdersListAdapter
    private lateinit var orderDetailsAdapter: OrderDetailsAdapter
    private lateinit var totalCoursesAdapter: TotalCoursesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_orders)

        initViews()
        initObservers()
        initActions()
    }

    private fun initViews() {
        ordersListAdapter = OrdersListAdapter(this)
        orderDetailsAdapter = OrderDetailsAdapter()
        totalCoursesAdapter = TotalCoursesAdapter()
    }

    private fun initObservers() {
        viewModel.kitchenOrders.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                binding.noOrdersTv.visibility = View.VISIBLE
            } else {
                binding.noOrdersTv.visibility = View.GONE
                ordersListAdapter.submitList(it) {
                    if (ordersListAdapter.itemCount == 0) {
                        return@submitList
                    }

                    val clickedOrder =
                        ordersListAdapter.currentList[ordersListAdapter.checkedPosition]
                    onOrderListItemClicked(clickedOrder)
                }
                viewModel.updateTotalCourses()
            }
        })

        viewModel.totalCourses.observe(this, Observer {
            totalCoursesAdapter.submitList(it)
        })

        viewModel._errorMessage.observe(this, Observer {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_INDEFINITE).show()
        })
    }

    private fun initActions() {

    }

    override fun onOrderListItemClicked(kitchenOrder: KitchenOrder) {
        orderDetailsAdapter.submitList(kitchenOrder.cartItems)
    }
}
