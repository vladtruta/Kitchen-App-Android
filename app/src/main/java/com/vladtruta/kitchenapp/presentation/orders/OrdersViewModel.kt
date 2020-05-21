package com.vladtruta.kitchenapp.presentation.orders

import androidx.lifecycle.*
import com.vladtruta.kitchenapp.data.model.local.CartItem
import com.vladtruta.kitchenapp.data.model.local.KitchenOrder
import com.vladtruta.kitchenapp.repository.RestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OrdersViewModel : ViewModel() {
    companion object {
        private const val REFRESH_RETRY_DELAY_MS = 10_000L
    }

    private val _refreshErrorMessage = MutableLiveData<String>()
    val refreshErrorMessage: LiveData<String> = _refreshErrorMessage

    private val _finishErrorMessage = MutableLiveData<String>()
    val finishErrorMessage: LiveData<String> = _finishErrorMessage

    val kitchenOrders = liveData {
        while (true) {
            emit(fetchOrders())
            delay(REFRESH_RETRY_DELAY_MS)
        }
    }

    private val _kitchenOrdersForceRefresh = MutableLiveData<List<KitchenOrder>>()
    val kitchenOrdersForceRefresh: LiveData<List<KitchenOrder>> = _kitchenOrdersForceRefresh

    private val _totalCourses = MutableLiveData<List<CartItem>>()
    val totalCourses: LiveData<List<CartItem>> = _totalCourses

    private val _forceRefreshLoading = MutableLiveData(false)
    val forceRefreshLoading: LiveData<Boolean> = _forceRefreshLoading

    private val _finishButtonEnabled = MutableLiveData(true)
    val finishButtonEnabled: LiveData<Boolean> = _finishButtonEnabled

    fun triggerRefresh() {
        viewModelScope.launch {
            _forceRefreshLoading.value = true
            _kitchenOrdersForceRefresh.value = fetchOrders()
            _forceRefreshLoading.value = false
        }
    }

    fun updateTotalCourses() {
        viewModelScope.launch(Dispatchers.Default) {
            val totalCourses = (kitchenOrders.value?.flatMap { it.cartItems } ?: emptyList())
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
                triggerRefresh()
            } catch (error: Exception) {
                _finishErrorMessage.value = error.message
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