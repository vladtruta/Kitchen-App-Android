package com.vladtruta.kitchenapp.repository

import com.vladtruta.kitchenapp.model.local.KitchenOrder
import com.vladtruta.kitchenapp.webservice.getNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object RestaurantRepository {

    private val kitchenNetwork = getNetwork()

    //region API

    suspend fun refreshOrders(): List<KitchenOrder> {
        return withContext(Dispatchers.Default) {
            try {
                val kitchenResponse = kitchenNetwork.refreshOrders()
                kitchenResponse.data.mapNotNull { it.toKitchenOrder() }
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