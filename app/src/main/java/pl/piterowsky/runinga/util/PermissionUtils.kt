package pl.piterowsky.runinga.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionUtils private constructor() {

    companion object {
        private const val ACCESS_FINE_LOCATION_REQUEST_CODE = 100

        fun requestPermissions(activity: Activity?) {
            if (isAccessFineLocationGranted(activity)) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    ACCESS_FINE_LOCATION_REQUEST_CODE
                )
            }
        }

        fun isAccessFineLocationGranted(activity: Activity?): Boolean {
            return ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        }

        fun onRequestPermissionResult(activity: Activity, requestCode: Int, grantResults: IntArray) {
            if (requestCode == ACCESS_FINE_LOCATION_REQUEST_CODE) {
                if (isPermissionGranted(grantResults)) {
                    Toast.makeText(activity, "Location permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    activity.finish()
                }
            }
        }

        private fun isPermissionGranted(grantResults: IntArray) =
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
    }
}
