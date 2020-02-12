package pl.piterowsky.runinga.config

import android.graphics.Color

class Settings {
    class Global {
        companion object {
            const val ZOOM_VALUE: Float = 17f
            const val PATH_COLOR: Int = Color.MAGENTA
            const val TIMER_DELAY_VALUE: Long = 2000L
        }
    }

    class RivalMode {
        companion object {
            var isActive: Boolean = false
            var pace: Double = 0.0
        }
    }

}
