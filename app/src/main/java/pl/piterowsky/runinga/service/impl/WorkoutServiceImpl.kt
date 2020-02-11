package pl.piterowsky.runinga.service.impl

import android.content.Context
import android.location.Location
import android.os.SystemClock
import android.text.format.DateFormat
import android.util.Log
import android.widget.Chronometer
import android.widget.Chronometer.OnChronometerTickListener
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import pl.piterowsky.runinga.R
import pl.piterowsky.runinga.activity.WorkoutActivity
import pl.piterowsky.runinga.config.Settings
import pl.piterowsky.runinga.model.Step
import pl.piterowsky.runinga.model.Workout
import pl.piterowsky.runinga.service.api.WorkoutService
import pl.piterowsky.runinga.util.LoggerTag
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate


class WorkoutServiceImpl(private val context: Context) : WorkoutService {

    private val timerName = "WORKOUT_TIMER"

    private lateinit var chronometerWrapper: ChronometerWrapper
    private lateinit var geolocationService: GeolocationService
    private lateinit var workout: Workout
    private lateinit var workoutTimer: Timer

    override fun workoutStart() {
        chronometerWrapper = ChronometerWrapper.getInstance(context)!!
        geolocationService = GeolocationService(context)
        workout = Workout()
        chronometerWrapper.start()
        startTimer()
    }

    override fun workoutStop() {
        stopTimer()
        chronometerWrapper.stop()
        geolocationService.stopTracking()
    }

    override fun workoutPause() {
        Log.i(LoggerTag.TAG_WORKOUT_TIMER, "Pausing timer")
        chronometerWrapper.pause()
        workoutTimer.cancel()
    }

    private fun stopTimer() {
        Log.i(LoggerTag.TAG_WORKOUT_TIMER, "Stopping timer")
        workoutTimer.cancel()
        //workoutTimer.purge() // TODO: Throws error operation not permitted
    }

    private fun startTimer() {
        Log.i(LoggerTag.TAG_WORKOUT_TIMER, "Starting timer")
        geolocationService.startTracking()

        workoutTimer = Timer(timerName, false)
        workoutTimer.scheduleAtFixedRate(0L, Settings.TIMER_DELAY_VALUE) {
            val steps = workout.getSteps()
            Log.i(LoggerTag.TAG_WORKOUT_TIMER, "Timer next iteration, StepsSize=${steps.size}")
            updateSteps()
            if (steps.isNotEmpty()) {
                updateMap(steps[steps.lastIndex].latLng)
                updateStats()
            }
        }
    }

    private fun updateMap(currentPosition: LatLng) {
        Log.i(LoggerTag.TAG_WORKOUT, "Updating map")
        val workoutActivity: WorkoutActivity = context as WorkoutActivity

        context.runOnUiThread() {
            val points = workoutActivity.polyline.points
            points.add(currentPosition)

            workoutActivity.currentPositionMarker.position = currentPosition

            workoutActivity.polyline.points = points
            workoutActivity.map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, Settings.ZOOM_VALUE))
        }
    }

    private fun updateSteps() {
        val currentLocation: Location? = geolocationService.currentLocation
        if (currentLocation != null) {
            val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
            val step = Step(latLng, System.currentTimeMillis())

            workout.addStep(step)

            Log.w(LoggerTag.TAG_WORKOUT, "New step, Step=${step}")
        } else {
            Log.w(LoggerTag.TAG_WORKOUT, "Location didn't change")
        }
    }

    private fun updateStats() {
        val distance = (context as WorkoutActivity).findViewById<TextView>(R.id.distance)
        context.runOnUiThread() {
            distance.text =
                String.format(context.getString(R.string.workout_value_distance_pattern), workout.getDistanceInKilometers())
        }
    }


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

        private var chronometer: Chronometer = (context as WorkoutActivity).findViewById(R.id.workout_chronometer)
        private var timeWhenStopped: Long = 0

        init {
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.onChronometerTickListener = OnChronometerTickListener { chronometer ->
                val t = SystemClock.elapsedRealtime() - chronometer.base - TimeZone.getDefault().rawOffset
                chronometer.text = DateFormat.format("kk:mm:ss", t)
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

}


