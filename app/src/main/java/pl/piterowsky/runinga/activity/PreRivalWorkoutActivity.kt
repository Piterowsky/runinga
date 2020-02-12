package pl.piterowsky.runinga.activity

import android.content.Intent
import android.os.Bundle
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
        Settings.pace = findViewById<EditText>(R.id.pace_field).text.toString().toDouble()
        startActivity(Intent(this, WorkoutActivity::class.java))
    }

}
