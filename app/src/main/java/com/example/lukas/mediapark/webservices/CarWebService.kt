package com.example.lukas.mediapark.webservices

import com.example.lukas.mediapark.models.Car
import retrofit2.Call
import retrofit2.http.GET

interface CarWebService {

    @GET("/")
    fun getCars(): Call<List<Car>>
}