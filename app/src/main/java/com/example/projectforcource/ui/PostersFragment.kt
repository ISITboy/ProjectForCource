package com.example.projectforcource.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectforcource.Models.DataModel
import com.example.projectforcource.adapters.RecyclerViewForPostersAdapter
import com.example.projectforcource.databinding.FragmentPostersBinding

class PostersFragment : Fragment() {

    lateinit var binding : FragmentPostersBinding


    private val dataModel: DataModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentPostersBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dataModel.sync.observe(activity as LifecycleOwner) {
            if(it){

            }
            dataModel.context.observe(activity as LifecycleOwner) {

            }

        }



    }
    companion object {
        @JvmStatic
        fun newInstance() = PostersFragment()
    }




}