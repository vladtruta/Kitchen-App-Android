package com.vladtruta.kitchenapp.data.model.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MenuCourse(
    val category: Category,
    val name: String,
    val description: String,
    val photoUrl: String,
    val portionSize: String,
    val price: Int,
    val id: Int
) : Parcelable