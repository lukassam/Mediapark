package com.example.lukas.mediapark.models

data class Car(val id: Long?, val plateNumber: String?,
               val location: CarLocation?, val model: CarModel?, val batteryPercentage: Int?,
               val batteryEstimatedDistance: Double?, val isCharging: Boolean?, var distance: Float?)