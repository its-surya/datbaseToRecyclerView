package com.example.databasetorecyclerview

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.io.IOException


class MapsActivity : FragmentActivity(), OnMapReadyCallback,GoogleMap.OnMapClickListener {
    private var mMap: GoogleMap? = null
    private var marker: Marker? =null
    private var delhi = LatLng(28.7041, 77.1025)
    // creating a variable
    // for search view.
    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    var searchView: SearchView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location->
            if(location!=null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                marker = mMap?.addMarker(
                    MarkerOptions()
                        .position(currentLatLng)
                        .title("Current")
                        .draggable(true)
                )
                mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,15f))
            }
        }

        val supportMapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)


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

//        mMap!!.addMarker(
//            MarkerOptions()
//                .position(delhi)
//                .draggable(true)
//
//        )
       // mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(delhi,6f))
        mMap!!.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{
            override fun onMarkerDrag(p0: Marker) {
            }

            override fun onMarkerDragEnd(p0: Marker) {
                val newPosition = p0?.position
                val latitude = newPosition?.latitude
                val longitude = newPosition?.longitude
                val title = getAddress(latitude!!, longitude!!).toString()

                val intent = Intent(this@MapsActivity, MainActivity::class.java)
//                intent.putExtra("Latitude", latitude)
//                intent.putExtra("Longitude", longitude)
                intent.putExtra("Title", title)
                startActivity(intent)
            }

            override fun onMarkerDragStart(p0: Marker) {
            }
        })
    }

    override fun onMapClick(p0: LatLng) {
        if(marker!=null){
            marker!!.position=p0
        }
    }

    private fun getAddress(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(latitude, longitude, 1)
        return list!![0].getAddressLine(0)
    }

}