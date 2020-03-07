package pl.piterowsky.runinga.util

import android.app.Activity
import android.content.Context
import android.os.SystemClock
import android.text.format.DateFormat
import android.widget.Chronometer
import pl.piterowsky.runinga.R
import java.util.*

class ChronometerWrapper private constructor(context: Context) {

    companion object {
        private var INSTANCE: ChronometerWrapper? = null
        private var initialized: Boolean = false

        fun getInstance(context: Context): ChronometerWrapper? {
            if (!initialized) {
                initialized = true
                INSTANCE = ChronometerWrapper(context)
            }
            return INSTANCE
        }
    }

    private var chronometer: Chronometer = (context as Activity).findViewById(R.id.workout_chronometer)
    private var timeWhenStopped: Long = 0

    init {
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.onChronometerTickListener = Chronometer.OnChronometerTickListener { chronometer ->
            val time = SystemClock.elapsedRealtime() - chronometer.base - TimeZone.getDefault().rawOffset
            val format = "kk:mm:ss"
            chronometer.text = DateFormat.format(format, time)
        }
    }

    fun start() {
        chronometer.base = SystemClock.elapsedRealtime() + timeWhenStopped
        chronometer.start()
    }

    fun stop() {
        chronometer.base = SystemClock.elapsedRealtime()
        timeWhenStopped = 0
        chronometer.stop()
        initialized = false
        INSTANCE = null
    }

    fun pause() {
        timeWhenStopped = chronometer.base - SystemClock.elapsedRealtime()
        chronometer.stop()
    }

}
