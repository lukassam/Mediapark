package com.example.lukas.mediapark.webservices

import com.example.lukas.mediapark.models.Car
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface CarWebService {

    @Headers("Accept: application/json")
    @GET("api/mobile/public/availablecars/")
    fun getCars(): Call<List<Car>>
}