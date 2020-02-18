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
import java.util.regex.Pattern


class PreRivalWorkoutActivity : AppCompatActivity() {

    companion object {
        const val MINUTES_PATTERN: String = "^[0-9]$"
        const val SECONDS_PATTERN: String = "(^[0-5][0-6]$)|(^60$)"
    }

    private lateinit var paceMinutes: EditText
    private lateinit var paceSeconds: EditText
    private lateinit var startButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_rival_workout)

        paceMinutes = findViewById(R.id.pace_field_min)
        paceSeconds = findViewById(R.id.pace_field_sec)
        startButton = findViewById(R.id.pre_workout_start_workout)

        startButton.setOnClickListener { handleStartButton() }
        paceMinutes.addTextChangedListener(getRegexTextWatcher(MINUTES_PATTERN, 1))
        paceSeconds.addTextChangedListener(getRegexTextWatcher(SECONDS_PATTERN, 2))
    }

    private fun handleStartButton() {
        Settings.RivalMode.pace = convertPaceToDouble(paceMinutes.text.toString(), paceSeconds.text.toString())
        startActivity(Intent(this, WorkoutActivity::class.java))
    }

    private fun getRegexTextWatcher(pattern: String, fieldLength: Int): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                val length = text.length

                if (length > (fieldLength - 1) && !Pattern.matches(pattern, text)) {
                    s!!.delete(length - 1, length)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                /* no implementation */
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                /* no implementation */
            }
        }
    }

    private fun convertPaceToDouble(paceMin: String, paceSec: String) =
        paceMin.toDouble() + (paceSec.toDouble() / 100) * 1.66

}
