package com.vladtruta.kitchenapp.data.model.webservice

import com.vladtruta.kitchenapp.data.model.responses.BaseResponse
import com.vladtruta.kitchenapp.data.model.responses.KitchenResponse
import retrofit2.http.*

interface KitchenApi {

    @GET("kitchen")
    suspend fun refreshOrders(): BaseResponse<List<KitchenResponse>>

    @DELETE("order")
    suspend fun deleteOrderById(@Field("id") id: Int)

}