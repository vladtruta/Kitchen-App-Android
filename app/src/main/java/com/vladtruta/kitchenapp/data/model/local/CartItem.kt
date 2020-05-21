package com.vladtruta.kitchenapp.data.model.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItem(
    val menuCourse: MenuCourse,
    val quantity: Int,
    val id: Int
): Parcelable