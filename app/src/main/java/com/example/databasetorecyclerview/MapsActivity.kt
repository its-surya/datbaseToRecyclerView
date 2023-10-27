package com.example.databasetorecyclerview

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.io.IOException


class MapsActivity : FragmentActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null

    // creating a variable
    // for search view.
    var searchView: SearchView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        Places.initialize(applicationContext, "AIzaSyAj8a8zwnvw7jtVOPDyA7-c9MITDmiwa7k")
        //normal use of layout id
        var autoCompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                as AutocompleteSupportFragment
        autoCompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ADDRESS
//                ,Place.Field.LAT_LNG
            )
        )
        autoCompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(place: Status) {
                Toast.makeText(
                    this@MapsActivity, "Some Error in Search",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onPlaceSelected(place: Place) {
                val address = place.address
//                val latLng = place.latLng
//
//                val latitude = latLng?.latitude
//                val longitude = latLng?.longitude

                Intent(this@MapsActivity, MainActivity::class.java).also {
//                    it.putExtra("SEARCH_LATITUDE", latitude)
//                    it.putExtra("SEARCH_LONGITUDE", longitude)
                    it.putExtra("SEARCH_ADDRESS", address)
                    startActivity(it)
                }
            }
        })


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }
}