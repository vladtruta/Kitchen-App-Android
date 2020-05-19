package com.vladtruta.kitchenapp.webservice

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private lateinit var instance: KitchenApi

fun getNetwork(): KitchenApi {
    if (!::instance.isInitialized) {
        val okHttpClient = OkHttpClient.Builder()
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        instance = retrofit.create(KitchenApi::class.java)
    }

    return instance
}