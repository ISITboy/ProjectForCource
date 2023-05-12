package com.example.projectforcource.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.projectforcource.ChooseTicketsActivity
import com.example.projectforcource.Models.DataForJoinPosterJoinHall
import com.example.projectforcource.Models.Films
import com.example.projectforcource.Models.PostersData
import com.example.projectforcource.R
import com.example.projectforcource.databinding.PosterItemBinding
import com.example.projectforcource.dbFirebase.AUTH
import com.example.projectforcource.dbFirebase.listFilms
import com.example.projectforcource.dbFirebase.listHall
import com.example.projectforcource.dbFirebase.listPosters_data
import com.example.projectforcource.utilits.MyIntentConstance
import com.squareup.picasso.Picasso


class RecyclerViewForPostersAdapter(contextF:Context) : RecyclerView.Adapter<RecyclerViewForPostersAdapter.MyHolder>() {
    private var listData = listPosters_data
    val context = contextF

    class MyHolder(itemView: View,contextV:Context) : RecyclerView.ViewHolder(itemView) {
        val context =contextV
        val binding = PosterItemBinding.bind(itemView)
        fun setData(poster:PostersData){
            var join = DataForJoinPosterJoinHall()
            listFilms.forEachIndexed {index,it->
                if(it.id==poster.id){
                    join.poster_id=poster.id
                    join.film_id=it.id
                    join.hall_id= listHall[index].id



                    binding.tVNameFilm.text = it.name
                    join.nameFilm = it.name
                    binding.tVGenreFilm.text = it.genre
                    join.genreFilm = it.genre
                    binding.tVReleaseAndDuration.text= "${it.year_of_release}, ${it.duration}"
                    join.releaseFilm = it.year_of_release
                    join.durationFilm = it.duration
                    binding.tVRating.text = it.rating
                    Picasso.get().load(it.photo_id).into(binding.imageFilm)
                    join.photo_id = it.photo_id
                    join.dataPoster=poster.date
                }
            }
            binding.tvDate.text=poster.date

            //Log.d("MyLog","holder ${poster.name}")

            itemView.setOnClickListener {
                if(AUTH.currentUser!=null) {
                    val i = Intent(context, ChooseTicketsActivity::class.java).apply {
                        putExtra(MyIntentConstance.FILM_ID, join.film_id)
                        putExtra(MyIntentConstance.FILM_NAME, join.nameFilm)
                        putExtra(MyIntentConstance.FILM_GENRE, join.genreFilm)
                        putExtra(MyIntentConstance.FILM_DURATION, join.durationFilm)
                        putExtra(MyIntentConstance.FILM_RELEASE, join.releaseFilm)
                        putExtra(MyIntentConstance.FILM_PHOTO, join.photo_id)
                        putExtra(MyIntentConstance.HALL_ID, join.hall_id)
                        putExtra(MyIntentConstance.POSTER_ID, join.poster_id)
                        putExtra(MyIntentConstance.POSTER_DATE, join.dataPoster)

                    }
                    context.startActivity(i)
                }else Toast.makeText(context,"Войдите в аккаунт",
                    Toast.LENGTH_SHORT).show ()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflate = LayoutInflater.from(parent.context)
        return MyHolder(inflate.inflate(R.layout.poster_item,parent,false),context)
    }

    override fun getItemCount(): Int {
        return listPosters_data.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
       holder.setData(listPosters_data[position])
    }
    fun updateAdapter(){
        listData.clear()
        listData.addAll(listPosters_data)
        notifyDataSetChanged()
    }

}