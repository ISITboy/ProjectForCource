package com.example.projectforcource.dbFirebase

import android.util.Log
import com.example.projectforcource.Models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

lateinit var AUTH: FirebaseAuth
lateinit var CURRENT_UID:String
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var USER:User
lateinit var FILM:Films
lateinit var HALL:Hall
lateinit var BASKET:Basket
lateinit var POSTERS_DATA:PostersData



const val NODE_USERS="users"
const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "userName"
const val CHILD_STATUS = "status"
const val CHILD_EMAIL = "email"
const val CHILD_PHOTO_ID = "photo_id"

const val NODE_BASKET="basket"
const val CHILD_BASKET_POSTER_ID="poster_id"
const val CHILD_BASKET_FILM="film"
const val CHILD_BASKET_HALL_ID="hall_id"
const val CHILD_BASKET_PRICE="price"
const val CHILD_BASKET_PLACE="place"



const val NODE_FILMS="films"
const val NODE_POSTERS="posters"
const val NODE_HALL="hall"

var listBasket:MutableList<Basket> = mutableListOf()


var listHall:MutableList<Hall> = mutableListOf()
var listFilms:MutableList<Films> = mutableListOf()
var listPosters_data:MutableList<PostersData> = mutableListOf()


fun initPOSTERS_DATA(){
    POSTERS_DATA= PostersData()
}

fun initHall(){
    HALL= Hall()
}
fun initBasket(){
    BASKET= Basket()
}

fun initUSER(){
    USER = User()
}
fun initFilms(){
    FILM= Films()
}

fun initFirebase(){
    AUTH=FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    CURRENT_UID=AUTH.currentUser?.uid.toString()
}




fun setDataPostersInList() {

    var data :MutableList<PostersData> = mutableListOf()
    REF_DATABASE_ROOT.child(NODE_POSTERS)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot!!.children
                // This returns the correct child count...
                Log.d("MyLog","${snapshot.children.count().toString()}")
                children.forEach {
                    Log.d("MyLog","forEach${it.toString()}")
                    POSTERS_DATA = it.getValue(PostersData::class.java) ?: PostersData()
                    data.add(POSTERS_DATA)
                }
                listPosters_data= data.toMutableList()
            }
            override fun onCancelled(error: DatabaseError) {
            println(error!!.message)
        }
    })
}





fun setDataFilmsInList(){
    var data :MutableList<Films> = mutableListOf()
    REF_DATABASE_ROOT.child(NODE_FILMS)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot!!.children
                // This returns the correct child count...
                Log.d("MyLog","${snapshot.children.count().toString()}")
                children.forEach {
                    Log.d("MyLog","forEach${it.toString()}")
                    FILM = it.getValue(Films::class.java) ?: Films()
                    data.add(FILM)
                }
                listFilms= data.toMutableList()
            }
            override fun onCancelled(error: DatabaseError) {
                println(error!!.message)

            }
        })
}


fun setDataHallInList(){
    var data :MutableList<Hall> = mutableListOf()
    REF_DATABASE_ROOT.child(NODE_HALL)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot!!.children
                // This returns the correct child count...
                Log.d("MyLog","${snapshot.children.count().toString()}")
                children.forEach {
                    Log.d("MyLog","forEach${it.toString()}")
                    HALL = it.getValue(Hall::class.java) ?: Hall()
                    data.add(HALL)
                }
                listHall= data.toMutableList()
            }
            override fun onCancelled(error: DatabaseError) {
                println(error!!.message)

            }
        })
}


fun setDataBasketInList(){
    var data :MutableList<Basket> = mutableListOf()
    REF_DATABASE_ROOT.child(NODE_BASKET)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot!!.children
                // This returns the correct child count...
                Log.d("MyLog","${snapshot.children.count().toString()}")
                children.forEach {
                    Log.d("MyLog","forEach${it.toString()}")
                    BASKET = it.getValue(Basket::class.java) ?: Basket()
                    data.add(Basket())
                }
                listBasket= data.toMutableList()
            }
            override fun onCancelled(error: DatabaseError) {
                println(error!!.message)

            }
        })
}






