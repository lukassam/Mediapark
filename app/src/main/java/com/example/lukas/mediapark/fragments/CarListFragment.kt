package com.example.lukas.mediapark.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.lukas.mediapark.R
import com.example.lukas.mediapark.adapters.CarListAdapter
import com.example.lukas.mediapark.viewmodels.CarViewModel
import kotlinx.android.synthetic.main.fragment_car_list.*

class CarListFragment : Fragment() {

    lateinit var carViewModel: CarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_car_list, container, false)
        carViewModel = ViewModelProviders.of(activity!!).get(CarViewModel::class.java)
        rootView.post {
            car_list_recycler_view.layoutManager = LinearLayoutManager(context)
            carViewModel.getCars()?.observe(this, Observer {
                car_list_recycler_view.adapter = CarListAdapter(it, context!!)
            })
        }
        return rootView
    }


}
