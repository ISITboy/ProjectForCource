package com.example.projectforcource.Models

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class DataModel : ViewModel() {
    val uID:MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }
    val posters=MutableLiveData<Films>()
    val sync:MutableLiveData<Boolean> by lazy{
        MutableLiveData<Boolean>()
    }
    val context:MutableLiveData<Context> by lazy{
        MutableLiveData<Context>()
    }
}