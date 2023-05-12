package com.example.projectforcource.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.projectforcource.MainActivity
import com.example.projectforcource.R
import com.example.projectforcource.databinding.ActivityLoginBinding
import com.example.projectforcource.dbFirebase.AUTH
import com.example.projectforcource.dbFirebase.initFirebase
import com.example.projectforcource.dbFirebase.initUSER
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUSER()
        initFirebase()

    }

    fun onClick_CreateAccount(view: View) {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
        finish()
    }

    fun onClick_SIgnIn(view: View)=with(binding) {
        AUTH = FirebaseAuth.getInstance()

        if(editTextEmail.text.toString().isEmpty()||editTextPassword.text.toString().isEmpty()){
            Toast.makeText(this@LoginActivity,"Введите email и пароль", Toast.LENGTH_LONG).show()
        }else{
            AUTH.signInWithEmailAndPassword(editTextEmail.text.toString(),editTextPassword.text.toString())
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this@LoginActivity,"Вы успешно авторизировались",Toast.LENGTH_LONG).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("uid", AUTH.currentUser?.uid.toString())
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this@LoginActivity,"Ошибка!",Toast.LENGTH_LONG).show()
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