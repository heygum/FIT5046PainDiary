package com.example.fit5046paindiary.fragment

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fit5046paindiary.PainAdapter
import com.example.fit5046paindiary.R
import com.example.fit5046paindiary.ViewModel.RecordViewModel
import com.example.fit5046paindiary.databinding.PieFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PieFragment: Fragment() {
    private var _binding: PieFragmentBinding? = null
    private val binding get()  = _binding!!
    val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PieFragmentBinding.inflate(inflater,container,false)
        val view = binding.root
        //var locationList = resources.getStringArray(R.array.location)

        val painViewModel: RecordViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(
            context?.applicationContext as Application
        ).create(RecordViewModel::class.java)

        /*painViewModel.locationCount.observe(viewLifecycleOwner, Observer {

        })*/

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}