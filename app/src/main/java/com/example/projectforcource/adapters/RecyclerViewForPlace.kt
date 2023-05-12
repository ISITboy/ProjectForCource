package com.example.projectforcource.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.projectforcource.Models.Hall
import com.example.projectforcource.R
import com.example.projectforcource.databinding.PlaceItemBinding
import com.example.projectforcource.dbFirebase.*


class RecyclerViewForPlace(list:Array<Int>,contextF: Context): RecyclerView.Adapter<RecyclerViewForPlace.MyHolder>() {
    val context = contextF

    private var listData = list
    var index=0
    class MyHolder(itemView: View,var index:Int,contextV:Context) : RecyclerView.ViewHolder(itemView) {
        val context =contextV
        val binding = PlaceItemBinding.bind(itemView)
        fun setData(hall: Int){
            binding.place.text = index.toString()
            itemView.setOnClickListener {
                BASKET.place=binding.place.text.toString()
                Toast.makeText(context,"Выбрано место N: ${BASKET.place}",Toast.LENGTH_SHORT).show()

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        index++
        val inflate = LayoutInflater.from(parent.context)
        return MyHolder(inflate.inflate(R.layout.place_item,parent,false),index,context)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listData[position])
    }

}