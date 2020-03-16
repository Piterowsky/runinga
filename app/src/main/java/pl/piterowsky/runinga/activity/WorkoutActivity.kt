package pl.piterowsky.runinga.activity

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import pl.piterowsky.runinga.R
import pl.piterowsky.runinga.config.RivalMode
import pl.piterowsky.runinga.config.Settings
import pl.piterowsky.runinga.database.dao.WorkoutHistoryDao
import pl.piterowsky.runinga.database.entity.WorkoutHistoryEntity
import pl.piterowsky.runinga.service.api.WorkoutService
import pl.piterowsky.runinga.service.impl.WorkoutServiceImpl
import pl.piterowsky.runinga.util.PermissionUtils

class WorkoutActivity : AppCompatActivity(), OnMapReadyCallback {

    private var workoutService: WorkoutService = WorkoutServiceImpl(this)
    private var isWorkoutPaused: Boolean = false

    lateinit var polyline: Polyline
    lateinit var map: GoogleMap
    lateinit var currentPositionMarker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.workout_activity)
        setupMapFragment()
        setupButtons()
        setupRivalDistanceLabel()
        PermissionUtils.requestPermissions(this)
    }

    private fun setupRivalDistanceLabel() {
        val rivalDistanceLabel = findViewById<TextView>(R.id.distance_rival_difference)
        if (!RivalMode.isActive) {
            rivalDistanceLabel.visibility = View.INVISIBLE
        }
    }

    fun setRivalDistanceLabelColor() {
        val rivalDistanceLabel = findViewById<TextView>(R.id.distance_rival_difference)
        when (rivalDistanceLabel.text[0]) {
            '-' -> rivalDistanceLabel.setTextColor(Color.BLUE)
            '+' -> rivalDistanceLabel.setTextColor(Color.RED)
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map!!
        map.uiSettings.isRotateGesturesEnabled = false
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(Settings.MAP_INITIAL_POINT, 0f))
        setupMapPath(map)
        currentPositionMarker = map.addMarker(MarkerOptions().position(Settings.MAP_INITIAL_POINT))
    }

    private fun setupMapPath(map: GoogleMap) {
        val polylineOptions = PolylineOptions()
        polylineOptions.color(Settings.PATH_COLOR)
        polylineOptions.width(5f)
        this.polyline = map.addPolyline(polylineOptions)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        PermissionUtils.onRequestPermissionResult(this, requestCode, grantResults)
    }

    private fun setupButtons() {
        val startPauseButton: Button = findViewById(R.id.start_pause_button);
        val stopButton: Button = findViewById(R.id.stop_button)

        startPauseButton.setOnClickListener { onClickStartPauseButton(startPauseButton) }
        stopButton.setOnClickListener { onClickStopButton() }
    }

    private fun onClickStopButton() {
        showEndWorkoutAlert()
    }

    private fun saveWorkoutHistory() {
        val workoutHistoryEntity = WorkoutHistoryEntity()

        workoutHistoryEntity.time =
            (workoutService as WorkoutServiceImpl).getChronometerWrapper().getFormattedTime().toString()

        if (RivalMode.isActive) {
            workoutHistoryEntity.workoutModeName = resources.getString(
                when (RivalMode.modeType) {
                    RivalMode.ModeType.PACE -> R.string.rival_pace_mode_type
                    RivalMode.ModeType.DISTANCE_PER_TIME -> R.string.rival_distance_per_time_mode_type
                }
            );

            workoutHistoryEntity.workoutModeValue = RivalMode.getModeValue()
        } else {
            workoutHistoryEntity.workoutModeName = resources.getString(R.string.rival_free_mode_type)
            workoutHistoryEntity.workoutModeValue =
                (workoutService as WorkoutServiceImpl).getWorkout().getDistance().toString()
        }

        WorkoutHistoryDao(this).add(workoutHistoryEntity)
        Log.i("DATABASE", "Saved workout to database")
    }

    private fun showEndWorkoutAlert() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.end_workout_alert_title))
            .setMessage(getString(R.string.end_workout_alert_message))
            .setPositiveButton(
                getString(R.string.end_workout_alert_positive_button),
                DialogInterface.OnClickListener { _, _ ->
                    Toast.makeText(this, getString(R.string.end_workout_toast_workout_ended), Toast.LENGTH_SHORT).show()
                    workoutService.workoutStop()
                    saveWorkoutHistory()
                    finish() // TODO: Go to the new intent -> Workout summary
                })
            .show()
    }

    private fun onClickStartPauseButton(button: Button) {
        if (isWorkoutPaused) {
            button.text = getString(R.string.workout_button_start)
            button.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.light_green, null))
            isWorkoutPaused = false
            workoutService.workoutPause()
        } else {
            button.text = getString(R.string.workout_button_pause)
            button.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.light_yellow, null))
            isWorkoutPaused = true
            workoutService.workoutStart()
        }
    }

    private fun setupMapFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }
}
