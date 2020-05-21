package com.vladtruta.kitchenapp.presentation.orders

import androidx.lifecycle.*
import com.vladtruta.kitchenapp.data.model.local.CartItem
import com.vladtruta.kitchenapp.repository.RestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OrdersViewModel : ViewModel() {
    companion object {
        private const val REFRESH_RETRY_DELAY_MS = 10_000L
    }

    val _errorMessage = MutableLiveData<String>()
    private val errorMessage: LiveData<String> = _errorMessage

    val kitchenOrders = liveData {
        while (true) {
            try {
                emit(RestaurantRepository.refreshOrders())
            } catch (error: Exception) {
                _errorMessage.postValue(error.message)
            }

            delay(REFRESH_RETRY_DELAY_MS)
        }
    }

    private val _totalCourses = MutableLiveData<List<CartItem>>()
    val totalCourses: LiveData<List<CartItem>> = _totalCourses

    fun updateTotalCourses() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
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
                _totalCourses.postValue(totalCourses)
            } catch (error: Exception) {
                _errorMessage.postValue(error.message)
            }
        }
    }
}