package com.vladtruta.kitchenapp.webservice

import com.vladtruta.kitchenapp.model.responses.BaseResponse
import com.vladtruta.kitchenapp.model.responses.KitchenResponse
import retrofit2.http.*

interface KitchenApi {

    @GET("orders")
    suspend fun refreshOrders(): BaseResponse<List<KitchenResponse>>

    @DELETE("order")
    suspend fun deleteOrderById(@Field("id") id: Int)

}