package com.vladtruta.kitchenapp.data.model.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    val name: String,
    val id: Int
) : Parcelable
