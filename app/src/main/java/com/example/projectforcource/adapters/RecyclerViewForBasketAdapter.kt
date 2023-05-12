package com.example.projectforcource.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectforcource.Models.Basket
import com.example.projectforcource.R
import com.example.projectforcource.databinding.BasketItemBinding
import com.example.projectforcource.dbFirebase.listBasket

class RecyclerViewForBasketAdapter(): RecyclerView.Adapter<RecyclerViewForBasketAdapter.MyHolder>(){
    private var listData = listBasket
    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = BasketItemBinding.bind(itemView)
        fun setData(basket: Basket){

            binding.tvName.text = basket.film
            binding.tvPriceRes.text = basket.price
            binding.tvPlace.text= basket.place
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflate = LayoutInflater.from(parent.context)
        return MyHolder(inflate.inflate(R.layout.basket_item,parent,false))
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listData[position])
    }
    fun updateAdapter(){
        listData.clear()
        listData.addAll(listBasket)
        notifyDataSetChanged()
    }
}