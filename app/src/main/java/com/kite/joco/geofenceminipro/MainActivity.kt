package com.kite.joco.geofenceminipro

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kite.joco.geofenceminipro.databinding.ActivityMainBinding
import com.kite.joco.geofenceminipro.location.GeoFenceLocationManager
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.util.*

@RuntimePermissions
class MainActivity : AppCompatActivity(), GeoFenceLocationManager.OnNewLocationAvailabe {

    lateinit var binding: ActivityMainBinding
    private lateinit var mainLocationManager : GeoFenceLocationManager
    var previousLocation : Location?  = null
    var distance : Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainLocationManager = GeoFenceLocationManager(this, this)
        gpsalapWithPermissionCheck()
    }
    @NeedsPermission(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION)
    fun gpsalap(){
        mainLocationManager.startLocationMonitoring()



        showLastKnownLocation()
    }

    private fun showLastKnownLocation() {
        mainLocationManager.getLastLocation { location ->
            /*binding.tvLocation.text =
                " Hosszúság ${location.longitude} szélesség: ${location.latitude} idő:${
                    Date(location.time).toString()
                }"*/
            getLocationText(location)
        }
    }

    override fun onNewLocation(location: Location) {
        binding.tvLocation.text = getLocationText(location)

        if (previousLocation != null && location.accuracy < 20) {
             distance += previousLocation!!.distanceTo(location)
            var kmdistance = distance/1000
            binding.tvDistance.text = "$kmdistance km"
            }


        previousLocation = location
    }

    fun getLocationText(location: Location): String {
        var speed = location.speed * 3.6
        return """
            Provider: ${location.provider}
            Latitude: ${location.latitude}
            Longitude: ${location.longitude}
            Accuracy: ${location.accuracy}
            Altitude: ${location.altitude}            
            Speed: ${"%.1f".format(speed)} km/h 
            Time: ${Date(location.time).toString()}
            """.trimIndent()
    }
    /*
    val pi = 3.14159265358979323
val s = "pi = %.2f".format(pi)
     */

    override fun onDestroy() {
        super.onDestroy()
        mainLocationManager.stopLocationMonitoring()
    }
}
