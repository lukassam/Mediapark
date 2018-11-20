package com.example.lukas.mediapark.fragments


import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import com.example.lukas.mediapark.R
import com.example.lukas.mediapark.viewmodels.CarViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    lateinit var carViewModel: CarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootview = inflater.inflate(R.layout.fragment_settings, container, false)
        carViewModel = ViewModelProviders.of(activity!!).get(CarViewModel::class.java)
        val userSharedPref = activity?.getSharedPreferences(getString(R.string.settings_pref), Context.MODE_PRIVATE)
        val editor = userSharedPref?.edit()
        rootview.post {
            var plateValue = userSharedPref?.getString(getString(R.string.plate_key), "")
            var batteryValue = userSharedPref?.getString(getString(R.string.battery_key), "")
            plate_number_edit_text.text = SpannableStringBuilder(plateValue)
            remaining_battery_edit_text.text = SpannableStringBuilder(batteryValue)
            settings_fab.setOnClickListener {
                if (plate_number_edit_text.text != null && remaining_battery_edit_text.text != null) {
                    editor?.putString(getString(R.string.plate_key), plate_number_edit_text.text.toString())
                    editor?.putString(getString(R.string.battery_key), remaining_battery_edit_text.text.toString())
                    editor?.commit()
                    carViewModel.loadCars()
                    Snackbar.make(it, "Settings changed", Snackbar.LENGTH_LONG)
                        .show()
                } else{
                    Snackbar.make(it, "Some fields are missing", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
        return rootview
    }


}
