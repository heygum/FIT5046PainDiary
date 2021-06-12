package com.example.fit5046paindiary.fragment

import android.graphics.BitmapFactory
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fit5046paindiary.R
import com.example.fit5046paindiary.databinding.MapFragmentBinding
import com.github.mikephil.charting.components.MarkerView
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import java.util.*


class MapFragment:Fragment() {
    private var _binding: MapFragmentBinding? = null
    private val binding get()  = _binding!!
    lateinit var mapView :MapView
    var latLng = LatLng(-37.876823, 145.045837)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.let { Mapbox.getInstance(it,getString(R.string.mapbox_access_token)) }
        _binding = MapFragmentBinding.inflate(inflater,container,false)

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        var position = CameraPosition.Builder().target(latLng).zoom(9.0)
            .tilt(30.0).build()
        mapView.getMapAsync {
                mapboxMap ->  mapboxMap.setStyle(Style.MAPBOX_STREETS){
            mapboxMap.cameraPosition
        }
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position),4000)
        }

        val geocoder = Geocoder(context, Locale.getDefault())
        val view = binding.root
        binding.adressSend.setOnClickListener {
            val address = geocoder.getFromLocationName(binding.addressEdit.text.toString(),1)
            if(!address.isEmpty()){
                latLng = LatLng(address[0].latitude,address[0].longitude)
                position = CameraPosition.Builder().target(latLng).zoom(13.0)
                    .tilt(30.0).build()
            }
            else
                Toast.makeText(context,"Can not find this address.",Toast.LENGTH_SHORT).show()
            mapView.getMapAsync {
                    mapboxMap ->  mapboxMap.setStyle(Style.MAPBOX_STREETS){
                mapboxMap.cameraPosition
                var symbol = SymbolManager(mapView,mapboxMap,it)
                symbol.iconAllowOverlap = true
                it.addImage("Marker",BitmapFactory.decodeResource(resources,R.drawable.mapbox_marker_icon_default))
                symbol.create(SymbolOptions().withLatLng(latLng).withIconImage("Marker"))
            }
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position),4000)
            }
        }

        return view
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}