package com.example.alimentate.same

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alimentate.data.FoodItem
import com.example.alimentate.databinding.ItemFoodBinding

class FoodAdapter(private val items: List<FoodItem>) :
    RecyclerView.Adapter<FoodAdapter.VH>() {

    class VH(val bind: ItemFoodBinding): RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemFoodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        val b = holder.bind

        b.txtName.text = item.name
        b.txtTemp.text = "Temp: ${item.tempC} °C"
        b.txtHumi.text = "Humedad: ${item.humi} %"
        b.txtGas.text  = "Gas: ${item.gas}"
        b.txtHours.text = "Vida útil: ~${item.hoursLeft} h"
        b.txtClass.text = item.freshnessClass

        val color = when(item.freshnessClass){
            "Fresco" -> Color.parseColor("#00B388")
            "Riesgo" -> Color.parseColor("#FFC107")
            else     -> Color.parseColor("#D32F2F")
        }
        b.statusBar.setBackgroundColor(color)
    }

    override fun getItemCount(): Int = items.size
}
