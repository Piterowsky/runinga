package pl.piterowsky.runinga.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import pl.piterowsky.runinga.R
import pl.piterowsky.runinga.config.Settings

class PreRivalWorkoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_rival_workout)

        val startButton: Button = findViewById(R.id.pre_workout_start_workout)
        startButton.setOnClickListener { handleStartButton() }
    }

    private fun handleStartButton() {
        val paceMin = findViewById<EditText>(R.id.pace_field_min).text.toString()
        val paceSec = findViewById<EditText>(R.id.pace_field_sec).text.toString()
        Settings.RivalMode.pace = convertPaceToDouble(paceMin, paceSec)
        startActivity(Intent(this, WorkoutActivity::class.java))
    }

    private fun convertPaceToDouble(paceMin: String, paceSec: String) =
        paceMin.toDouble() + (paceSec.toDouble() / 100) * 1.66

}
