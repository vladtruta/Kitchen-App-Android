package com.vladtruta.kitchenapp.presentation.orders

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.vladtruta.kitchenapp.R
import com.vladtruta.kitchenapp.data.model.local.KitchenOrder
import com.vladtruta.kitchenapp.databinding.ActivityOrdersBinding
import com.vladtruta.kitchenapp.presentation.orders.adapter.OrderDetailsAdapter
import com.vladtruta.kitchenapp.presentation.orders.adapter.OrdersListAdapter
import com.vladtruta.kitchenapp.presentation.orders.adapter.TotalCoursesAdapter

class OrdersActivity : AppCompatActivity(), OrdersListAdapter.OrdersListListener {

    private lateinit var binding: ActivityOrdersBinding
    private val viewModel by viewModels<OrdersViewModel>()

    private lateinit var ordersListAdapter: OrdersListAdapter
    private lateinit var orderDetailsAdapter: OrderDetailsAdapter
    private lateinit var totalCoursesAdapter: TotalCoursesAdapter

    private var errorSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initObservers()
        initActions()
    }

    private fun initViews() {
        ordersListAdapter = OrdersListAdapter(this)
        binding.ordersListRv.adapter = ordersListAdapter

        orderDetailsAdapter = OrderDetailsAdapter()
        binding.ordersDetailsRv.adapter = orderDetailsAdapter

        totalCoursesAdapter = TotalCoursesAdapter()
        binding.coursesRv.adapter = totalCoursesAdapter

        binding.finishEfab.hide()
    }

    private fun initObservers() {
        viewModel.kitchenOrders.observe(this, Observer {
            updateOrders(it)
        })

        viewModel.kitchenOrdersForceRefresh.observe(this, Observer {
            updateOrders(it)
        })

        viewModel.totalCourses.observe(this, Observer {
            totalCoursesAdapter.submitList(it)
        })

        viewModel.refreshErrorMessage.observe(this, Observer {
            errorSnackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.refresh) { viewModel.triggerRefresh() }
            errorSnackbar?.show()
        })

        viewModel.finishErrorMessage.observe(this, Observer {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        })

        viewModel.finishOrderSuccessful.observe(this, Observer {
            if (it == true) {
                ordersListAdapter.checkedPosition =
                    if (!viewModel.kitchenOrdersForceRefresh.value.isNullOrEmpty()) {
                        0
                    } else {
                        RecyclerView.NO_POSITION
                    }
            }
        })

        viewModel.forceRefreshLoading.observe(this, Observer {
            binding.loadingSrl.isRefreshing = it
        })

        viewModel.finishButtonEnabled.observe(this, Observer {
            binding.finishEfab.isEnabled = it
        })
    }

    private fun updateOrders(orders: List<KitchenOrder>?) {
        // Orders are null when there's a network connection issue
        if (orders == null) {
            return
        } else {
            errorSnackbar?.dismiss()
            errorSnackbar = null
        }

        if (orders.isEmpty()) {
            binding.noOrdersTv.visibility = View.VISIBLE
            binding.ordersListRv.visibility = View.GONE
            binding.ordersDetailsRv.visibility = View.GONE
            binding.coursesRv.visibility = View.GONE
            binding.finishEfab.hide()
        } else {
            binding.noOrdersTv.visibility = View.GONE
            binding.ordersListRv.visibility = View.VISIBLE
            binding.ordersDetailsRv.visibility = View.VISIBLE
            binding.coursesRv.visibility = View.VISIBLE
            binding.finishEfab.show()
        }

        ordersListAdapter.submitList(orders) {
            if (ordersListAdapter.itemCount == 0) {
                return@submitList
            }

            if (ordersListAdapter.checkedPosition == RecyclerView.NO_POSITION) {
                return@submitList
            }

            ordersListAdapter.notifyItemChanged(ordersListAdapter.checkedPosition)
            val clickedOrder =
                ordersListAdapter.currentList[ordersListAdapter.checkedPosition]
            onOrderListItemClicked(clickedOrder)
        }
        viewModel.updateTotalCourses()
    }

    private fun initActions() {
        binding.loadingSrl.setOnRefreshListener {
            viewModel.triggerRefresh()
        }

        binding.ordersMtb.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_refresh -> {
                    viewModel.triggerRefresh()
                    true
                }
                else -> {
                    false
                }
            }
        }

        binding.finishEfab.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.finish_order)
                .setMessage(R.string.finish_order_message).setPositiveButton(R.string.ok) { _, _ ->
                    val clickedOrder =
                        ordersListAdapter.currentList[ordersListAdapter.checkedPosition]
                    viewModel.finishOrder(clickedOrder)
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .show()
        }
    }

    override fun onOrderListItemClicked(kitchenOrder: KitchenOrder) {
        binding.finishEfab.show()
        orderDetailsAdapter.submitList(kitchenOrder.cartItems)
    }
}
