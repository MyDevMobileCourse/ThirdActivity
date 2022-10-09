package com.example.thirdactivity

import android.R
import android.annotation.SuppressLint
import android.graphics.Movie
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thirdactivity.api.RestApiService
import com.example.thirdactivity.api.UserInfo
import com.example.thirdactivity.databinding.HomeFragmentBinding


class Home : Fragment() {
    private var reView : RecyclerView? = null
    private var mAdapter : ListAdapter? = null
    private var mUsers : MutableList<UserInfo> = ArrayList()

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.load?.setOnClickListener {
            findNavController().navigate(com.example.thirdactivity.R.id.home_form);
        }
        reView = binding.recyclerview as RecyclerView
        reView!!.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = ListAdapter(requireContext(), mUsers, com.example.thirdactivity.R.layout.user_item)
        reView!!.adapter = mAdapter
        fetchUsers();
    }

    private fun fetchUsers(){
        val apiService = RestApiService()
        println("i'm in fetchUsers")
        println("i'm sending a request")
        val call = apiService.getUsers {
                res ->
            run {
                println("response")
                if (res == null) {
                    println("res is null")
                    mUsers = ArrayList()
                    mAdapter!!.notifyDataSetChanged()
                } else {
                    println("res is not null")
                    println(res)
                    mUsers.addAll(res)
                    mAdapter!!.notifyDataSetChanged()
                }
            }
        }

    }

}
