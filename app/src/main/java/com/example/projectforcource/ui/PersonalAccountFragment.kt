package com.example.projectforcource.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectforcource.MainActivity
import com.example.projectforcource.Models.DataModel
import com.example.projectforcource.Models.User
import com.example.projectforcource.R
import com.example.projectforcource.adapters.RecyclerViewForBasketAdapter
import com.example.projectforcource.databinding.FragmentPersonalAccountBinding
import com.example.projectforcource.dbFirebase.*
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class PersonalAccountFragment : Fragment() {

    var myAdapter = RecyclerViewForBasketAdapter()
    var tempImageUri = "empty"
    val imageRequestCode = 10
    private val dataModel: DataModel by activityViewModels()
    lateinit var binding: FragmentPersonalAccountBinding
    lateinit var _UID:String
    val storageRef = Firebase.storage.reference;
    lateinit var uploadUri:Uri
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPersonalAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dataModel.uID.observe(activity as LifecycleOwner) {
            _UID = it
        }
        initBasket()
        setDataBasketInList()
        initRecyclerView()
        initFirebase()
        initUSER()
        initUser()




        binding.button2.setOnClickListener {

            fillAdapter()
        }

        binding.bSync.setOnClickListener {
            val intent = Intent(view?.context, MainActivity::class.java)
            startActivity(intent)
            AUTH.signOut()
        }



        binding.BChangeData.setOnClickListener {
            changeForBChangeData()
        }
        binding.BSaveData.setOnClickListener {
            val name = binding.ETname.text.toString()
            val surname = binding.ETSurname.text.toString()
            val phone = binding.ETPhone.text.toString()
            if(name.isEmpty() || surname.isEmpty() || phone.isEmpty())
                Toast.makeText(activity, "Поля должны быть заполнены",Toast.LENGTH_SHORT).show()
            else
            {
                REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_PHONE).setValue(phone)
                    .addOnCompleteListener {
                    if(!it.isSuccessful){
                        Toast.makeText(activity, R.string.personal_account_error_change_phone,Toast.LENGTH_SHORT).show()
                    }
                }
                val fullName = "$name $surname"
                USER.userName=fullName
                REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_USERNAME).setValue(fullName)
                    .addOnCompleteListener {
                        if(!it.isSuccessful){
                            Toast.makeText(activity, R.string.personal_account_error_change_full_name,Toast.LENGTH_SHORT).show()
                        }
                    }
                Toast.makeText(activity, "Данные Сохранены",Toast.LENGTH_SHORT).show()

            }
            changeForBSaveData()

        }


        binding.ActionButtonChangeImage.setOnClickListener {
            getImage()
        }

    }

    fun initRecyclerView(){
        binding.rcViewBasket.layoutManager = LinearLayoutManager(view?.context)
        binding.rcViewBasket.adapter = myAdapter
    }

    fun fillAdapter(){
        myAdapter.updateAdapter()
    }

    companion object {
        @JvmStatic
        fun newInstance() = PersonalAccountFragment()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == imageRequestCode){
            binding.imageView.setImageURI(data?.data)
            tempImageUri = data?.data.toString()
            Log.d("MyLog","$tempImageUri")
            uploadImage()
        }

    }


    private fun uploadImage(){

        val bitmap = binding.imageView.drawable.toBitmap()
        val baos  = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val byte = baos.toByteArray()
        val mRef :StorageReference =storageRef.child("${USER.id}")
        val up :UploadTask= mRef.putBytes(byte)
        val urlTask = up?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation mRef.downloadUrl
        })?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                uploadUri = task.getResult()
                USER.photo_id=uploadUri.toString()
                REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_PHOTO_ID).setValue(USER.photo_id)
            } else {
                // Handle failures
            }
        }?.addOnFailureListener{

        }
    }


    private fun changeForBChangeData(){
        binding.apply {
            ETname.isEnabled=true
            ETSurname.isEnabled=true
            ETPhone.isEnabled=true
            BChangeData.visibility=View.GONE
            BSaveData.visibility=View.VISIBLE
        }
    }
    private fun changeForBSaveData(){
        binding.apply {
            ETname.isEnabled=false
            ETSurname.isEnabled=false
            ETPhone.isEnabled=false
            BChangeData.visibility=View.VISIBLE
            BSaveData.visibility=View.GONE }
    }




    private fun initUser(){
        REF_DATABASE_ROOT.child(NODE_USERS).child(_UID).addListenerForSingleValueEvent(AppValueEventListener{
            binding.TVEmail.setText(it.child(CHILD_EMAIL).getValue().toString())
            USER = it.getValue(User::class.java) ?:User()
            USER.photo_id = it.child(CHILD_PHOTO_ID).getValue().toString()
            val fullNameList = USER.userName.split(" ")
            if (fullNameList.size!=1) {
                binding.ETname.setText(fullNameList[0])
                binding.ETSurname.setText(fullNameList[1])
                binding.ETPhone.setText(USER.phone)
                binding.TVName.setText(fullNameList[0])
                binding.TVSurname.setText(fullNameList[1])

            }

            if(USER.photo_id!="empty"){

                Picasso.get().load(USER.photo_id).into(binding.imageView)
            }

        })

    }

    private fun getImage(){
        val intentChooser = Intent(Intent.ACTION_GET_CONTENT)
        intentChooser.type="image/*"
        startActivityForResult(intentChooser,imageRequestCode)
    }


}