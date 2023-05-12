package com.example.projectforcource

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectforcource.Models.DataModel
import com.example.projectforcource.adapters.RecyclerViewForPostersAdapter
import com.example.projectforcource.databinding.ActivityMainBinding
import com.example.projectforcource.dbFirebase.*
import com.example.projectforcource.ui.LoginActivity
import com.example.projectforcource.ui.PersonalAccountFragment
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val dataModel: DataModel by viewModels()

    var myAdapter =RecyclerViewForPostersAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFunc()
        initFilms()
        initHall()
        setImageHeader()
        initPOSTERS_DATA()
        initRecyclerView()



        CoroutineScope(Dispatchers.IO).launch {
            listPosters_data
            setDataFilmsInList()
            listFilms
            setDataHallInList()
            listHall

        }



    }



    private fun openFragment(idHolder:Int,f:Fragment){
        supportFragmentManager.beginTransaction()
            .replace(idHolder,f)
            .commit()

    }



    private fun setNavigationItemSelectedListener(){
        binding.apply {
            navigationDrawerLayout.setNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.signIn-> {
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)

                    }
                    R.id.personalAccount->{
                        if(acceptUID()!="null") {
                            openFragment(R.id.mainPlaceFrgment,PersonalAccountFragment.newInstance())
                            sendUIDToFragmentPersonalAccount()
                        }
                        else{
                           Toast.makeText(this@MainActivity,"Войдите в аккаунт",
                                Toast.LENGTH_SHORT).show ()
                            Drawer.closeDrawer(GravityCompat.START)
                        }

                    }
                }
                true
            }
        }
    }


    private fun acceptUID() = intent.getStringExtra("uid").toString()

    private fun initFunc(){
        initUSER()
        initFirebase()
        setNavigationItemSelectedListener()
    }



    private fun sendUIDToFragmentPersonalAccount(){
        dataModel.uID.value = acceptUID()

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->finish()
            R.id.sync->{
                Toast.makeText(this,"Sync", Toast.LENGTH_LONG).show()
               fillAdapter()
            }
        }
        return true
    }

    fun setImageHeader() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(acceptUID())
            .addListenerForSingleValueEvent(AppValueEventListener {
                val photoUrl = it.child(CHILD_EMAIL).getValue().toString()
                if (photoUrl != "empty") {
                    val navigationView: NavigationView =
                        findViewById(R.id.navigation_drawerLayout)!!
                    val headerView: View = navigationView.getHeaderView(0)
                    val image: ImageView = headerView.findViewById(R.id.imageView4)
                    Picasso.get().load(photoUrl).into(image)
                }
            })
    }


    fun initRecyclerView(){
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = myAdapter
    }

    fun fillAdapter(){
        myAdapter.updateAdapter()
    }

}