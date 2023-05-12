package com.example.projectforcource

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectforcource.adapters.RecyclerViewForPlace
import com.example.projectforcource.adapters.RecyclerViewForPostersAdapter
import com.example.projectforcource.databinding.ActivityChooseTicketsBinding
import com.example.projectforcource.databinding.ActivityMainBinding
import com.example.projectforcource.dbFirebase.*
import com.example.projectforcource.ui.LoginActivity
import com.example.projectforcource.utilits.MyIntentConstance
import com.squareup.picasso.Picasso
import java.net.IDN

class ChooseTicketsActivity : AppCompatActivity() {

    private var price=0
    private var place =""
    lateinit var countPlace:Array<Int>
    lateinit var myAdapter : RecyclerViewForPlace
    lateinit var binding: ActivityChooseTicketsBinding

    lateinit var hall_id: String
    lateinit var film_id:String
    lateinit var poster_id:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseTicketsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBasket()
        getMyIntent()







        myAdapter = RecyclerViewForPlace(countPlace,this)
        //setPriceInList()
        initRecyclerView()


        binding.button3.setOnClickListener {



            val dateMap = mutableMapOf<String,Any>()
            dateMap[CHILD_BASKET_HALL_ID] = hall_id.toString()
            dateMap[CHILD_BASKET_POSTER_ID] = poster_id.toString()
            dateMap[CHILD_BASKET_FILM]=binding.tVNameFilm.text
            dateMap[CHILD_BASKET_PRICE]=binding.tvPrice.text
            dateMap[CHILD_BASKET_PLACE]= BASKET.place.toString()


            REF_DATABASE_ROOT.child(NODE_BASKET).child(CURRENT_UID).setValue(dateMap)


        }
    }


    fun getMyIntent(){
        val i = intent
        if(i!=null){
            Picasso.get().load(i.getStringExtra(MyIntentConstance.FILM_PHOTO)).into(binding.imageFilm)
            binding.tVNameFilm.text = i.getStringExtra(MyIntentConstance.FILM_NAME)
            binding.tVGenreFilm.text = i.getStringExtra(MyIntentConstance.FILM_GENRE)
            binding.tVReleaseAndDuration.text = "${i.getStringExtra(MyIntentConstance.FILM_RELEASE)}, " +
                    "${i.getStringExtra(MyIntentConstance.FILM_DURATION)}"
            getCountPlaceForRecyclerView(i.getStringExtra(MyIntentConstance.HALL_ID)!!)
            binding.tVDate.text = i.getStringExtra(MyIntentConstance.POSTER_DATE)
            hall_id=i.getStringExtra(MyIntentConstance.HALL_ID)!!
            film_id=i.getStringExtra(MyIntentConstance.FILM_ID)!!
            poster_id=i.getStringExtra(MyIntentConstance.POSTER_ID)!!
        }
    }




    fun outputPrice(price:String){
        binding.tvPrice.text = "Стоимость: $price"
    }





    fun getCountPlaceForRecyclerView(hall_id:String){
        listHall.forEach {
            if(it.id==hall_id){
                val price = it.price.toInt()
                countPlace = Array(it.count.toInt()) { price }
                Log.d("MyLog","${it.price}")
                outputPrice(it.price)
            }
        }
    }

    fun initRecyclerView(){

        binding.rcView.layoutManager= GridLayoutManager(this,3)
        binding.rcView.adapter = myAdapter
    }


}
