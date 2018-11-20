package com.example.lukas.mediapark.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lukas.mediapark.R
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
    lateinit var userSharedPref: SharedPreferences

    private val BASE_URL = "https://development.espark.lt/"

    init {
        userSharedPref = myApplication?.getSharedPreferences(myApplication
            .getString(R.string.settings_pref), Context.MODE_PRIVATE)

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
        var userPreferedPlate = userSharedPref?.getString(myApplication.getString(R.string.plate_key), "")
        var userPreferedBattery = userSharedPref?.getString(myApplication.getString(R.string.battery_key), "")
        carWebService.getCars().enqueue(object : Callback<List<Car>> {
            override fun onFailure(call: Call<List<Car>>, t: Throwable) {
                Toast.makeText(myApplication, "failure", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Car>>, response: Response<List<Car>>) {
                if (response.isSuccessful()){
                    if (userPreferedBattery == "") {
                        if (userPreferedPlate == "") {
                            cars?.value = response.body()
                        } else{
                            var carsList = response.body()
                            var car = carsList?.find { it.plateNumber == userPreferedPlate }
                            if (car != null) {
                                var newList: MutableList<Car> = mutableListOf<Car>()
                                newList.add(car)
                                cars?.value = newList.toList()
                            }
                        }
                    } else{
                        var newList: MutableList<Car> = mutableListOf<Car>()
                        var carsList = response.body()
                        var newCarList = carsList?.filter { it.batteryPercentage!! >= userPreferedBattery?.toInt() ?: 0 }
                        if (userPreferedPlate == ""){
                            cars?.value = newCarList
                        } else{
                            var selectedCar = newCarList?.find { it.plateNumber == userPreferedPlate }
                            if (selectedCar != null) {
                                newList.add(selectedCar)
                                cars?.value = newList
                            }
                        }
                    }
                }

            }
        })
    }
}