package pl.piterowsky.runinga.model

import com.google.android.gms.maps.model.LatLng

interface WorkoutMode {

    /**
     * Calculates new distance that rival should traveled in one unit of timer value
     *
     * @return distance in meters
     */
    fun calculateRivalDistanceOnUnitTime() : Double

}
