package pl.piterowsky.runinga.activity

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import pl.piterowsky.runinga.R
import pl.piterowsky.runinga.service.api.WorkoutService
import pl.piterowsky.runinga.service.impl.WorkoutServiceImpl
import pl.piterowsky.runinga.util.ConstantsUtils
import pl.piterowsky.runinga.util.PermissionUtils

class WorkoutActivity : AppCompatActivity(), OnMapReadyCallback {

    private var workoutService: WorkoutService = WorkoutServiceImpl(this)

    private var map: GoogleMap? = null
    private var isWorkoutPaused: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.workout_activity)
        setupMapFragment()
        setupButtons()
        PermissionUtils.requestPermissions(this);
    }

    private fun setupButtons() {
        val startPauseButton: Button = findViewById(R.id.start_pause_button);
        val stopButton: Button = findViewById(R.id.stop_button)

        startPauseButton.setOnClickListener { onClickStartPauseButton(startPauseButton) }
        stopButton.setOnClickListener { onClickStopButton() }
    }

    private fun onClickStopButton() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.end_workout_alert_title))
            .setMessage(getString(R.string.end_workout_alert_message))
            .setPositiveButton(
                getString(R.string.end_workout_alert_positive_button),
                DialogInterface.OnClickListener { _, _ ->
                    Toast.makeText(this, getString(R.string.end_workout_toast_workout_ended), Toast.LENGTH_SHORT).show()
                    workoutService.workoutStop()
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

    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(ConstantsUtils.centerOfEarth, 0f))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        PermissionUtils.onRequestPermissionResult(this, requestCode, grantResults)
    }
}
