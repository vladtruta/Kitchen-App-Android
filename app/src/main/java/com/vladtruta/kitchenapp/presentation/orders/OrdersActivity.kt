package com.vladtruta.kitchenapp.presentation.orders

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.vladtruta.kitchenapp.R
import com.vladtruta.kitchenapp.databinding.ActivityOrdersBinding
import com.vladtruta.kitchenapp.model.local.KitchenOrder
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
        viewModel.orders.observe(this, Observer {
            ordersListAdapter.submitList(it)
            viewModel.updateTotalCourses()
        })

        viewModel.totalCourses.observe(this, Observer {
            totalCoursesAdapter.submitList(it)
        })
    }

    private fun initActions() {

    }

    override fun onOrderListItemClicked(kitchenOrder: KitchenOrder) {
        orderDetailsAdapter.submitList(kitchenOrder.cartItems)
    }
}
