package com.kite.joco.geofenceminipro.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions


class GeoFenceLocationManager(context: Context, private val onNewLocationHandler: OnNewLocationAvailabe ) {

    interface OnNewLocationAvailabe{
        fun onNewLocation(location: Location)
    }

    private val fusedLocationClient : FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private var locationCallback : LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            onNewLocationHandler.onNewLocation(locationResult.lastLocation)
        }
    }

    @Throws(SecurityException::class)
    fun startLocationMonitoring() {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 1000
        locationRequest.fastestInterval = 500
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())

    }

    @Throws(SecurityException::class)
    fun stopLocationMonitoring() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


//    @SuppressLint("MissingPermission")
//    @NeedsPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION)
    @Throws(SecurityException::class)
    fun getLastLocation(callback: (Location) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? -> callback(location!!) }
    }
}