package com.example.lukas.mediapark.fragments


import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.lukas.mediapark.R
import com.example.lukas.mediapark.adapters.CarListAdapter
import com.example.lukas.mediapark.models.Car
import com.example.lukas.mediapark.viewmodels.CarViewModel
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_car_list.*

class CarListFragment : Fragment() {

    lateinit var carViewModel: CarViewModel
    lateinit var disposable: Disposable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_car_list, container, false)
        carViewModel = ViewModelProviders.of(activity!!).get(CarViewModel::class.java)
        rootView.post {
            if (ContextCompat.checkSelfPermission(context!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                var rxLocation = RxLocation(context!!);

                var locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(500000)

                disposable = rxLocation.location().updates(locationRequest).subscribe { location: Location? ->
                    car_list_recycler_view.layoutManager = LinearLayoutManager(context)
                    carViewModel.getCars()?.observe(this, Observer {
                        var carList = it as MutableList<Car>
                        carList.forEach { eachCar ->
                            eachCar.distance = distanceBetweenTwo(location?.latitude!!, location?.longitude!!,
                                eachCar.location?.latitude?.toDouble()!!, eachCar.location?.longitude?.toDouble()!!)
                        }
                        var newCarList = carList.sortedBy { selector(it) }
                        car_list_recycler_view.adapter = CarListAdapter(newCarList.toList(), context!!)
                    })
                }
            }
        }
        return rootView
    }

    private fun selector(car: Car): Float? {
        return car.distance
    }

    private fun distanceBetweenTwo(lat1: Double, lng1: Double, lat2: Double, lng2: Double) : Float? {
        var locationA = Location("point A");

        locationA.latitude = lat1
        locationA.longitude = lng1

        var locationB = Location("point B");

        locationB.latitude = lat2
        locationB.longitude = lng2

        return locationA.distanceTo(locationB)/1000;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.dispose();
    }
}
