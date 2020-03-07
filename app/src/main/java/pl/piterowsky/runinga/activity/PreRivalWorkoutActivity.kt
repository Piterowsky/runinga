package pl.piterowsky.runinga.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pl.piterowsky.runinga.R
import pl.piterowsky.runinga.config.RivalMode
import pl.piterowsky.runinga.util.InputTimeWatcher


class PreRivalWorkoutActivity : AppCompatActivity() {

    private lateinit var paceView: View
    private lateinit var distancePerTimeView: View
    private lateinit var switch: Switch

    private lateinit var startButton: Button

    private lateinit var distanceInput: EditText
    private lateinit var timeInput: EditText
    private lateinit var paceInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_rival_workout)
        bindUiWidgets()

        switch.setOnCheckedChangeListener { _, isChecked -> switchViews(isChecked) }
        timeInput.addTextChangedListener(InputTimeWatcher(this, timeInput))
        paceInput.addTextChangedListener(InputTimeWatcher(this, paceInput))
        startButton.setOnClickListener { handleStartButton() }
    }

    private fun bindUiWidgets() {
        startButton = findViewById(R.id.pre_workout_start_button)
        paceView = findViewById(R.id.pre_workout_pace_view)
        distancePerTimeView = findViewById(R.id.pre_workout_time_per_distance_view)
        switch = findViewById(R.id.pre_workout_switch)
        timeInput = findViewById(R.id.pre_workout_time_input)
        distanceInput = findViewById(R.id.pre_workout_distance_input)
        paceInput = findViewById(R.id.pre_workout_pace_input)
    }

    private fun switchViews(isChecked: Boolean) {
        if (!isChecked) {
            paceView.visibility = View.GONE
            distancePerTimeView.visibility = View.VISIBLE
            RivalMode.modeType = RivalMode.ModeType.DISTANCE_PER_TIME;
        } else {
            paceView.visibility = View.VISIBLE
            distancePerTimeView.visibility = View.GONE
            RivalMode.modeType = RivalMode.ModeType.PACE;
        }
    }

    private fun handleStartButton() {
        RivalMode.distance = distanceInput.text.toString()
        RivalMode.time = timeInput.text.toString()
        RivalMode.pace = paceInput.text.toString()

        if (isInputEmpty()) {
            Toast.makeText(this, getString(R.string.pre_workout_provide_inputs), Toast.LENGTH_SHORT).show()
        } else {
            startActivity(Intent(this, WorkoutActivity::class.java))
        }
    }

    private fun isInputEmpty(): Boolean {
        return when (RivalMode.modeType) {
            RivalMode.ModeType.DISTANCE_PER_TIME -> RivalMode.distance.isBlank() || RivalMode.time.isBlank()
            RivalMode.ModeType.PACE -> RivalMode.pace.isBlank()
        }
    }


}
