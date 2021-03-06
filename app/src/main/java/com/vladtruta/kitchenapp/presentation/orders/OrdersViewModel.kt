package com.vladtruta.kitchenapp.presentation.orders

import androidx.lifecycle.*
import com.vladtruta.kitchenapp.data.model.local.CartItem
import com.vladtruta.kitchenapp.data.model.local.KitchenOrder
import com.vladtruta.kitchenapp.repository.RestaurantRepository
import kotlinx.coroutines.*

class OrdersViewModel : ViewModel() {
    companion object {
        private const val REFRESH_RETRY_DELAY_MS = 10_000L
    }

    private val _refreshErrorMessage = MutableLiveData<String>()
    val refreshErrorMessage: LiveData<String> = _refreshErrorMessage

    private val _finishOrderErrorMessage = MutableLiveData<String>()
    val finishOrderErrorMessage: LiveData<String> = _finishOrderErrorMessage

    private val _finishOrderSuccessful = MutableLiveData<Boolean>()
    val finishOrderSuccessful: LiveData<Boolean> = _finishOrderSuccessful

    private val _kitchenOrders = MutableLiveData<List<KitchenOrder>>()
    val kitchenOrders: LiveData<List<KitchenOrder>> = _kitchenOrders

    private val _totalCourses = MutableLiveData<List<CartItem>>()
    val totalCourses: LiveData<List<CartItem>> = _totalCourses

    private val _refreshLoading = MutableLiveData(false)
    val refreshLoading: LiveData<Boolean> = _refreshLoading

    private val _finishButtonEnabled = MutableLiveData(true)
    val finishButtonEnabled: LiveData<Boolean> = _finishButtonEnabled

    fun startContinuousRefresh() {
        viewModelScope.launch {
            while (true) {
                triggerRefresh().join()
                delay(REFRESH_RETRY_DELAY_MS)
            }
        }
    }

    fun triggerRefresh(): Job {
        return viewModelScope.launch {
            _refreshLoading.value = true
            val orders = fetchOrders()?.also { updateTotalCourses(it).join() }
            _kitchenOrders.value = orders
            _refreshLoading.value = false
        }
    }

    private fun updateTotalCourses(orders: List<KitchenOrder>): Job {
        return viewModelScope.launch(Dispatchers.Default) {
            val totalCourses = (orders.flatMap { it.cartItems })
                .groupBy { it.menuCourse.id }
                .values
                .map {
                    it.reduce { acc, cartItem ->
                        CartItem(
                            cartItem.menuCourse,
                            acc.quantity + cartItem.quantity,
                            cartItem.id
                        )
                    }
                }
                .sortedByDescending { it.quantity }
            _totalCourses.postValue(totalCourses)
        }
    }

    fun finishOrder(kitchenOrder: KitchenOrder) {
        _finishButtonEnabled.value = false
        viewModelScope.launch {
            try {
                RestaurantRepository.deleteOrder(kitchenOrder)
                triggerRefresh().join()
                _finishOrderSuccessful.value = true
            } catch (error: Exception) {
                _finishOrderSuccessful.value = false
                _finishOrderErrorMessage.value = error.message
            } finally {
                _finishButtonEnabled.value = true
            }
        }
    }

    private suspend fun fetchOrders(): List<KitchenOrder>? {
        return try {
            RestaurantRepository.refreshOrders()
        } catch (error: Exception) {
            _refreshErrorMessage.value = error.message
            null
        }
    }
}