package com.example.lukas.mediapark.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lukas.mediapark.R
import com.example.lukas.mediapark.models.Car
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.car_recycler_view_item.view.*

class CarListAdapter(carList: List<Car>, context: Context): RecyclerView.Adapter<CarListAdapter.ViewHolder>() {

    var carList = carList
    var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        var view: View = layoutInflater.inflate(R.layout.car_recycler_view_item, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return carList.size
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val carName = view.car_name_text_holder
        val carImage = view.car_image_holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.carName.text = carList[position].model.title
        Picasso.get().load(carList[position].model.photoUrl).into(holder.carImage);
    }
}