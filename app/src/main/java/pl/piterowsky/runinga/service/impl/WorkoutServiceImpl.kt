package pl.piterowsky.runinga.service.impl

import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
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

    private lateinit var workout: Workout
    private lateinit var geolocationService: GeolocationService
    private lateinit var workoutTimer: Timer

    override fun workoutStart() {
        workout = Workout()
        geolocationService = GeolocationService(context)
        startTimer()
    }

    override fun workoutStop() {
        stopTimer()
        workout.workoutEndTimestamp = System.nanoTime()
        geolocationService.stopTracking()
    }

    override fun workoutPause() {
        Log.i(LoggerTag.TAG_WORKOUT_TIMER, "Pausing timer")
        workoutTimer.cancel();
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
            val steps = workout.steps
            Log.i(LoggerTag.TAG_WORKOUT_TIMER, "Timer next iteration, StepsSize=${steps.size}")
            updateSteps()
            if(steps.isNotEmpty()) {
                updateMap(steps[steps.lastIndex].latLng)
            }
        }
    }

    private fun updateMap(currentPosition: LatLng) {
        Log.i(LoggerTag.TAG_WORKOUT, "Updating map")
        val workoutActivity: WorkoutActivity = context as WorkoutActivity

        context.runOnUiThread() {
            val points = workoutActivity.polyline.points
            points.add(currentPosition)
            workoutActivity.polyline.points = points
            workoutActivity.map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, Settings.ZOOM_VALUE))
        }
    }

    private fun updateSteps() {
        val currentLocation: Location? = geolocationService.currentLocation
        if (currentLocation != null) {
            val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
            val step = Step(latLng, System.currentTimeMillis())

            workout.steps.add(step)

            Log.w(LoggerTag.TAG_WORKOUT, "New step, Step=${step}")
        } else {
            Log.w(LoggerTag.TAG_WORKOUT, "Location didn't change")
        }
    }
}
