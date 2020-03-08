package pl.piterowsky.runinga.config

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng

class Settings {
    companion object {
        const val ZOOM_VALUE: Float = 17f
        const val PATH_COLOR: Int = Color.MAGENTA
        const val TIMER_DELAY_VALUE: Long = 1000L
        const val AUDIO_PACE_REMINDERS_FREQUENCY = 10

        val MAP_INITIAL_POINT = LatLng(30.0, 31.0)
    }
}
