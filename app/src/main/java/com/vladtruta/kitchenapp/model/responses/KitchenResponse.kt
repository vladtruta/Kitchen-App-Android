package com.vladtruta.kitchenapp.model.responses

import com.google.gson.annotations.SerializedName
import com.vladtruta.kitchenapp.model.local.CartItem
import com.vladtruta.kitchenapp.model.local.KitchenOrder

data class KitchenResponse(
    @SerializedName("cartItems")
    val cartItems: List<CartItem>? = null,
    @SerializedName("tableName")
    val tableName: String? = null,
    @SerializedName("id")
    val id: Int? = null
) {
    fun toKitchenOrder(): KitchenOrder? {
        cartItems ?: return null
        tableName ?: return null
        id ?: return null

        return KitchenOrder(cartItems, tableName, id)
    }
}