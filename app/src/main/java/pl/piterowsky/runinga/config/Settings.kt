package pl.piterowsky.runinga.config

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng

class Settings {
    companion object {
        var ZOOM_VALUE: Float = 17f
        var PATH_COLOR: Int = Color.MAGENTA
        var TIMER_DELAY_VALUE: Long = 1000L
        var AUDIO_PACE_REMINDERS_FREQUENCY = 10
        val MAP_INITIAL_POINT = LatLng(30.0, 31.0)
    }
}
