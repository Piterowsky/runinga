package pl.piterowsky.runinga.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import pl.piterowsky.runinga.R
import pl.piterowsky.runinga.config.Settings

class SettingsActivity : AppCompatActivity() {

    private lateinit var zoomInput: EditText
    private lateinit var pathInput: EditText
    private lateinit var timerInput: EditText
    private lateinit var audioFreqInput: EditText
    private lateinit var apply: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        bindUI()
        setInitialInputTexts()

        apply.setOnClickListener { applySettingsAndFinish() }
    }

    private fun applySettingsAndFinish() {
        Settings.ZOOM_VALUE = zoomInput.text.toString().toFloat()
        Settings.PATH_COLOR = Color.parseColor(pathInput.text.toString())
        Settings.TIMER_DELAY_VALUE = timerInput.text.toString().toLong()
        Settings.AUDIO_PACE_REMINDERS_FREQUENCY = audioFreqInput.text.toString().toInt()

        finish()
    }

    private fun setInitialInputTexts() {
        zoomInput.setText(Settings.ZOOM_VALUE.toString())
        pathInput.setText(String.format("#%06X", 0xFFFFFF and Settings.PATH_COLOR))
        timerInput.setText(Settings.TIMER_DELAY_VALUE.toString())
        audioFreqInput.setText(Settings.AUDIO_PACE_REMINDERS_FREQUENCY.toString())
    }

    private fun bindUI() {
        zoomInput = findViewById(R.id.settings_zoom)
        pathInput = findViewById(R.id.settings_path_color)
        timerInput = findViewById(R.id.settings_timer_delay)
        audioFreqInput = findViewById(R.id.settings_audio_reminders_freq)
        apply = findViewById(R.id.settings_apply)
    }
}
