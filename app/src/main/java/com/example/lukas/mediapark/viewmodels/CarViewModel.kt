package com.example.lukas.mediapark.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lukas.mediapark.models.Car
import com.example.lukas.mediapark.webservices.CarWebService
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CarViewModel(application: Application) : AndroidViewModel(application) {

    private var myApplication = application
    var cars: MutableLiveData<List<Car>>? = null
    lateinit var carWebService: CarWebService

    private val BASE_URL = "https://development.espark.lt/api/mobile/public/availablecars/"

    init {
        val gson = GsonBuilder()
                .setLenient()
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        carWebService = retrofit.create(CarWebService::class.java)
    }

    fun getCars(): LiveData<List<Car>>? {
        if (cars == null) {
            cars = MutableLiveData<List<Car>>()
            loadCars()
        }
        return cars
    }

    fun loadCars() {
        carWebService.getCars().enqueue(object : Callback<List<Car>> {
            override fun onFailure(call: Call<List<Car>>, t: Throwable) {
                Toast.makeText(myApplication, "failure", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Car>>, response: Response<List<Car>>) {
                if (response.isSuccessful()){
                    cars?.value = response.body()
                }
            }
        })
    }
}