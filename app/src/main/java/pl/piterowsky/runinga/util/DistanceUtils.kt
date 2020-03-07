package pl.piterowsky.runinga.util

import com.google.android.gms.maps.model.LatLng
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class DistanceUtils {

    companion object {
        fun getDistanceInRounded(distance: Double): String {
            return BigDecimal(distance)
                .setScale(2, RoundingMode.HALF_DOWN)
                .toString()
        }

        fun diff(distanceA: Double, distanceB: Double): String {
            val distanceARounded: String = getDistanceInRounded(distanceA)
            val distanceBRounded: String = getDistanceInRounded(distanceB)
            val diff: String = BigDecimal(distanceARounded)
                .subtract(BigDecimal(distanceBRounded))
                .abs()
                .toString()
            val diffSign: String = diffSign(distanceARounded, distanceBRounded)
            return diffSign + diff
        }

        private fun diffSign(distanceA: String, distanceB: String): String {
            return when (BigDecimal(distanceA).compareTo(BigDecimal(distanceB))) {
                1 -> "+"
                0 -> ""
                -1 -> "-"
                else -> throw IllegalStateException("Result of comparision cannot by from this range")
            }
        }

        fun getDistanceInKm(p1: LatLng, p2: LatLng): Double {
            val earthRadiusInKm = 6371e3

            val lat1 = Math.toRadians(p1.latitude)
            val lat2 = Math.toRadians(p2.latitude)
            val deltaLat = Math.toRadians(p2.latitude - p1.latitude)
            val deltaLng = Math.toRadians(p2.longitude - p1.longitude)

            val a = sin(deltaLat / 2) * sin(deltaLat / 2) +
                    cos(lat1) * cos(lat2) *
                    sin(deltaLng / 2) * sin(deltaLng / 2)

            val c = 2 * atan2(sqrt(a), sqrt(1 - a))

            return earthRadiusInKm * c / 1000
        }

    }

}
