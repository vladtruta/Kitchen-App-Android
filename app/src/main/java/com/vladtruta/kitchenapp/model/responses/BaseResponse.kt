package com.vladtruta.kitchenapp.model.responses

import com.google.gson.annotations.SerializedName

data class BaseResponse<out T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: T
)