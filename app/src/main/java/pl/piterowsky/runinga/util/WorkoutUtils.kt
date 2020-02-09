package pl.piterowsky.runinga.util

import com.google.android.gms.maps.model.LatLng
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class WorkoutUtils {

    companion object {
        // Formula from https://www.movable-type.co.uk/scripts/latlong.html
        fun calculateDistanceInMeters(p1: LatLng, p2: LatLng): Double {
            val earthRadiusInKm = 6371e3;

            val lat1 = Math.toRadians(p1.latitude)
            val lat2 = Math.toRadians(p2.latitude)
            val deltaLat = Math.toRadians(p2.latitude - p1.latitude)
            val deltaLng = Math.toRadians(p2.longitude - p1.longitude)

            val a = sin(deltaLat / 2) * sin(deltaLat / 2) +
                    cos(lat1) * cos(lat2) *
                    sin(deltaLng / 2) * sin(deltaLng / 2)

            val c = 2 * atan2(sqrt(a), sqrt(1 - a))

            return earthRadiusInKm * c;
        }
    }


}
