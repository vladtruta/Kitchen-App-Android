package com.vladtruta.kitchenapp.repository

import com.vladtruta.kitchenapp.data.model.local.KitchenOrder
import com.vladtruta.kitchenapp.data.model.webservice.getNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object RestaurantRepository {

    private val kitchenNetwork = getNetwork()

    //region API

    suspend fun refreshOrders(): List<KitchenOrder> {
        return withContext(Dispatchers.Default) {
            try {
                val response = kitchenNetwork.refreshOrders()
                if (response.success) {
                    response.data.mapNotNull { it.toKitchenOrder() }
                } else {
                    throw Exception()
                }
            } catch (error: Exception) {
                throw Exception("Failed to refresh orders", error)
            }
        }
    }

    suspend fun deleteOrder(kitchenOrder: KitchenOrder) {
        try {
            kitchenNetwork.deleteOrderById(kitchenOrder.id)
        } catch (error: Exception) {
            throw Exception("Failed to delete order having id ${kitchenOrder.id}", error)
        }
    }

    //endregion
}