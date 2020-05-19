package com.vladtruta.kitchenapp.model.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KitchenOrder(
    val cartItems: List<CartItem>,
    val tableName: String,
    val id: Int
) : Parcelable