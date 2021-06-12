package com.example.fit5046paindiary.fragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fit5046paindiary.PainAdapter
import com.example.fit5046paindiary.ViewModel.RecordViewModel
import com.example.fit5046paindiary.databinding.RecordFragmentBinding

class RecordFragment:Fragment() {
    private var _binding: RecordFragmentBinding? = null
    private val binding get()  = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RecordFragmentBinding.inflate(inflater,container,false)
        val view = binding.root

        val painViewModel: RecordViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(
            context?.applicationContext as Application
        ).create(RecordViewModel::class.java)

        painViewModel.UserPain.observe(viewLifecycleOwner, Observer {  pains ->
            val layoutManager = LinearLayoutManager(context)
            binding.recyclerView.layoutManager = layoutManager
            val adapter = PainAdapter(pains)
            binding.recyclerView.adapter = adapter
        })


        return view
}


override fun onDestroy() {
    super.onDestroy()
    _binding = null
}
}