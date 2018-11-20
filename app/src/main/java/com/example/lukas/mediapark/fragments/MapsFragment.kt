package com.example.lukas.mediapark.fragments


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.lukas.mediapark.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.MapsInitializer
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.lukas.mediapark.viewmodels.CarViewModel
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.*
import com.patloew.rxlocation.RxLocation
import com.squareup.picasso.Picasso
import io.reactivex.internal.operators.flowable.FlowableBlockingSubscribe.subscribe
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.android.synthetic.main.fragment_maps.view.*
import java.lang.Exception


class MapsFragment : Fragment() {

    var mapView: MapView? = null
    var map: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var carViewModel: CarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_maps, container, false)
        carViewModel = ViewModelProviders.of(activity!!).get(CarViewModel::class.java)
        val transformation = RoundedCornersTransformation(25, 5);
        rootView.post {

            try {
                MapsInitializer.initialize(activity!!)
            } catch (e: GooglePlayServicesNotAvailableException) {
                Log.e("Address Map", "Could not initialize google play", e)
            }
            mapView = car_map
            mapView?.onCreate(savedInstanceState)
            mapView?.onResume()

            if (ContextCompat.checkSelfPermission(context!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                var rxLocation = RxLocation(context!!);

                var locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setNumUpdates(1)
                    .setInterval(5000)

                rxLocation.location().updates(locationRequest).subscribe { location: Location? ->
                        if (mapView != null) {
                            mapView?.getMapAsync {
                                it.isMyLocationEnabled = true
                                it?.uiSettings?.isMyLocationButtonEnabled = true
                                it.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    LatLng(location!!.latitude,
                                        location!!.longitude), 16.toFloat()))
                                carViewModel.getCars()?.observe(this, Observer {carList ->
                                    carList.forEach {car ->
                                        it.addMarker(MarkerOptions()
                                            .position(LatLng(car.location?.latitude!!.toDouble(),
                                                car.location.longitude!!.toDouble()))
                                            .title(car.model?.title)
                                            .snippet(car.plateNumber))
                                    }
                                })
                            }
                        }
                    }
            }
        }
        return rootView
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView = car_map
        mapView?.onCreate(savedInstanceState)
    }

    override fun onResume() {
        mapView?.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }



}
