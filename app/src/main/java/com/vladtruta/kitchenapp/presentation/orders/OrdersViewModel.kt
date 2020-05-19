package com.vladtruta.kitchenapp.presentation.orders

import androidx.lifecycle.*
import com.vladtruta.kitchenapp.model.local.CartItem
import com.vladtruta.kitchenapp.repository.RestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrdersViewModel : ViewModel() {

    val orders = liveData { emit(RestaurantRepository.refreshOrders()) }

    private val _totalCourses = MutableLiveData<List<CartItem>>()
    val totalCourses: LiveData<List<CartItem>> = _totalCourses

    fun updateTotalCourses() {
        viewModelScope.launch(Dispatchers.Default) {
            val totalCourses = (orders.value?.flatMap { it.cartItems } ?: emptyList())
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
        }
    }
}