package pl.piterowsky.runinga.service.impl

import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import pl.piterowsky.runinga.R
import pl.piterowsky.runinga.util.LoggerTag

// TODO: Add asking for gps when disabled
class GeolocationService(private val context: Context) : LocationListener {

    var currentLocation: Location? = null
    private lateinit var locationManager: LocationManager

    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            currentLocation = location
            Log.i(LoggerTag.TAG_LOCATION, "Location changed, Location=${location}")
        } else {
            Log.w(LoggerTag.TAG_LOCATION, "Cannot get location: Location=null")
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.i(LoggerTag.TAG_LOCATION, "Provider=${provider}, Status=${status}")
    }

    override fun onProviderEnabled(provider: String?) {
        Log.i(LoggerTag.TAG_LOCATION, "Provider enabled, Provider=${provider}")
    }

    override fun onProviderDisabled(provider: String?) {
        Log.i(LoggerTag.TAG_LOCATION, "Provider disabled, Provider=${provider}")
    }

    fun startTracking() {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            MaterialAlertDialogBuilder(context)
                .setTitle(context.getString(R.string.gps_alert_title))
                .setMessage(context.getString(R.string.gps_alert_message))
                .show()
        }

        val trustedLocationManager: LocationManager = locationManager

        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE
        criteria.powerRequirement = Criteria.POWER_HIGH

        val provider: String = trustedLocationManager.getBestProvider(criteria, true) ?: ""
        if (provider.isBlank()) {
            Log.w(LoggerTag.TAG_LOCATION, "Cannot get provider")
            return
        }

        try {
            trustedLocationManager.requestLocationUpdates(provider, 1000L, 1f, this)
        } catch (e: SecurityException) {
            Log.e(LoggerTag.TAG_LOCATION, "No required permissions, $e")
        }

        Log.d(LoggerTag.TAG_LOCATION, "Tracking position started")
    }

    fun stopTracking() {
        locationManager.removeUpdates(this)
    }
}
