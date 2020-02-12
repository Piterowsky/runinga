package pl.piterowsky.runinga.activity

import android.content.DialogInterface
import android.os.Bundle
import android.transition.Visibility
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import pl.piterowsky.runinga.R
import pl.piterowsky.runinga.config.Settings
import pl.piterowsky.runinga.service.api.WorkoutService
import pl.piterowsky.runinga.service.impl.WorkoutServiceImpl
import pl.piterowsky.runinga.util.ConstantsUtils
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
        PermissionUtils.requestPermissions(this)
        if(!Settings.RivalMode.isActive) {
            findViewById<TextView>(R.id.distance_rival_difference).visibility = View.INVISIBLE
        }
    }

    private fun setupChronometerContainer() {
        val chronometerContainer = findViewById<RelativeLayout>(R.id.workout_chronometer_container)
        chronometerContainer.translationY = -(chronometerContainer.height.toFloat())
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map!!
        map.uiSettings.isRotateGesturesEnabled = false
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ConstantsUtils.centerOfEarth, 0f))
        setupMapPath(map)
        currentPositionMarker = map.addMarker(MarkerOptions().position(ConstantsUtils.centerOfEarth))
    }

    private fun setupMapPath(map: GoogleMap) {
        val polylineOptions = PolylineOptions()
        polylineOptions.color(Settings.Global.PATH_COLOR)
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
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.end_workout_alert_title))
            .setMessage(getString(R.string.end_workout_alert_message))
            .setPositiveButton(
                getString(R.string.end_workout_alert_positive_button),
                DialogInterface.OnClickListener { _, _ ->
                    Toast.makeText(this, getString(R.string.end_workout_toast_workout_ended), Toast.LENGTH_SHORT).show()
                    workoutService.workoutStop()
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
            //doChronometerContainerSlideDownAnimation()
            button.text = getString(R.string.workout_button_pause)
            button.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.light_yellow, null))
            isWorkoutPaused = true
            workoutService.workoutStart()
        }
    }

    private fun doChronometerContainerSlideDownAnimation() {
        val chronometerContainer = findViewById<RelativeLayout>(R.id.workout_chronometer_container)
        chronometerContainer.animate().setDuration(500).translationY(0f)
    }

    private fun setupMapFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }
}
