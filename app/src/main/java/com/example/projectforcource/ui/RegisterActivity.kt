package com.example.projectforcource.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.projectforcource.MainActivity
import com.example.projectforcource.databinding.ActivityRegisterBinding
import com.example.projectforcource.dbFirebase.*
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFirebase()
    }


    fun onClick_Register(view: View)=with(binding) {
        AUTH = FirebaseAuth.getInstance()

        if (editTextEmail.text.toString().isEmpty() || editTextPassword.text.toString().isEmpty()) {
            Toast.makeText(this@RegisterActivity, "Введите email и пароль", Toast.LENGTH_LONG)
                .show()
        }
        else {
            AUTH.createUserWithEmailAndPassword(
                editTextEmail.text.toString(),
                editTextPassword.text.toString()
            ).addOnCompleteListener {
                    val uid = AUTH.currentUser?.uid.toString()
                    if (it.isSuccessful) {
                        val dateMap = mutableMapOf<String,Any>()
                        dateMap[CHILD_ID]=uid
                        dateMap[CHILD_EMAIL] = AUTH.currentUser?.email.toString()
                        dateMap[CHILD_USERNAME] = uid
                        dateMap[CHILD_PHONE]=""
                        dateMap[CHILD_STATUS]="0"
                        dateMap[CHILD_PHOTO_ID]="empty"
                        Log.d("MyLog", "${REF_DATABASE_ROOT.toString()}, $CURRENT_UID")
                        REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
                            .addOnCompleteListener {task2->
                                if(task2.isSuccessful){
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Аккаунт успешно создан",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                    intent.putExtra("uid",AUTH.currentUser?.uid.toString())
                                    startActivity(intent)
                                    finish()
                                }else{
                                    Toast.makeText(this@RegisterActivity, "Ошибка!", Toast.LENGTH_LONG).show()
                                }

                        }

                    }
                }
        }
    }





    override fun onBackPressed(){
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }
}