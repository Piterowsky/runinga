package pl.piterowsky.runinga.service.impl

import android.content.Context
import android.location.Location
import android.media.MediaPlayer
import android.util.Log
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import pl.piterowsky.runinga.R
import pl.piterowsky.runinga.activity.WorkoutActivity
import pl.piterowsky.runinga.config.Language
import pl.piterowsky.runinga.config.RivalMode
import pl.piterowsky.runinga.config.Settings
import pl.piterowsky.runinga.model.*
import pl.piterowsky.runinga.model.modes.DistancePerTimeWorkoutMode
import pl.piterowsky.runinga.model.modes.PaceWorkoutMode
import pl.piterowsky.runinga.service.api.WorkoutService
import pl.piterowsky.runinga.util.ChronometerWrapper
import pl.piterowsky.runinga.util.DistanceUtils
import pl.piterowsky.runinga.util.LoggerTag
import pl.piterowsky.runinga.util.VoicesService
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.math.absoluteValue


class WorkoutServiceImpl(private val context: Context) : WorkoutService {

    private val timerName = "WORKOUT_TIMER"

    private lateinit var chronometerWrapper: ChronometerWrapper
    private lateinit var geolocationService: GeolocationService
    private lateinit var workout: Workout
    private lateinit var workoutTimer: Timer
    private lateinit var voicesService: VoicesService

    private var isWorkoutInitialized: Boolean = false

    init {
        if(RivalMode.isActive) {
            voicesService = VoicesService()
        }
    }

    override fun workoutStart() {
        val workoutMode: WorkoutMode = when (RivalMode.modeType) {
            RivalMode.ModeType.PACE -> PaceWorkoutMode()
            RivalMode.ModeType.DISTANCE_PER_TIME -> DistancePerTimeWorkoutMode()
        }

        if (!isWorkoutInitialized) {
            workout = Workout(workoutMode)
            isWorkoutInitialized = true
        }
        chronometerWrapper = ChronometerWrapper.getInstance(context)!!
        geolocationService = GeolocationService(context)
        chronometerWrapper.start()
        startTimer()
    }

    override fun workoutStop() {
        stopTimer()
        chronometerWrapper.stop()
        geolocationService.stopTracking()
        isWorkoutInitialized = false
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
        workoutTimer.scheduleAtFixedRate(0L, Settings.TIMER_DELAY_VALUE) { timerAction() }
    }

    private fun timerAction() {
        Log.i(LoggerTag.TAG_WORKOUT_TIMER, "Timer next iteration")

        updateSteps()
        updateUI()

        if(RivalMode.isActive) {
            playAudioPaceControl()
            playDistanceReachedVoice()
        }
    }

    private fun playDistanceReachedVoice() {
        if (isDistanceReached() && !workout.isDistanceReached) {
            workout.isDistanceReached = true
            val voice = voicesService.reachedDistanceVoice
            val mp: MediaPlayer = MediaPlayer.create(context, voice)
            mp.start()
        }
    }

    private fun isDistanceReached() = workout.getDistance() >= RivalMode.distance.toDouble()

    private fun updateUI() {
        val steps = workout.getSteps()
        if (steps.isNotEmpty()) {
            updateMap(steps[steps.lastIndex].latLng)
        }
        updateStats()
    }

    private fun playAudioPaceControl() {
        val minimumDiffDistance = 0.05
        val shouldReminderBePlayed = chronometerWrapper.getSeconds() % Settings.AUDIO_PACE_REMINDERS_FREQUENCY == 0 &&
                (workout.getDistance() - workout.getRivalDistance()).absoluteValue > minimumDiffDistance

        if (shouldReminderBePlayed) {
            Log.i(LoggerTag.TAG_WORKOUT_TIMER, "Playing reminder, seconds = ${chronometerWrapper.getSeconds()}")
            val voice = if (workout.isRivalWinning()) voicesService.speedUpVoice else voicesService.slowDownVoice
            val mp: MediaPlayer = MediaPlayer.create(context, voice)
            mp.start()
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
            workoutActivity.map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    currentPosition,
                    Settings.ZOOM_VALUE
                )
            )
        }
    }

    private fun updateSteps() {
        Log.i(LoggerTag.TAG_WORKOUT_TIMER, "StepsSize=${workout.getSteps().size}")
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
                String.format(
                    context.getString(R.string.workout_value_distance_pattern),
                    DistanceUtils.getDistanceRounded(workout.getDistance())
                )

            if (RivalMode.isActive) {
                context.findViewById<TextView>(R.id.distance_rival_difference).text =
                    String.format(
                        context.getString(R.string.workout_value_distance_pattern),
                        DistanceUtils.diff(workout.getRivalDistance(), workout.getDistance())
                    )
                context.setRivalDistanceLabelColor()
            }
        }
    }
}


