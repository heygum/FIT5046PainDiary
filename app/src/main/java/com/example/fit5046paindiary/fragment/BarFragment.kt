package com.example.fit5046paindiary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fit5046paindiary.databinding.BarFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class BarFragment: Fragment() {
    private var _binding: BarFragmentBinding? = null
    private val binding get()  = _binding!!
    val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BarFragmentBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}