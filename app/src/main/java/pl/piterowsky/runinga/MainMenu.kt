package pl.piterowsky.runinga

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import pl.piterowsky.runinga.activity.WorkoutActivity


class MainMenu : AppCompatActivity() {

    private lateinit var quickStartButton: Button
    private lateinit var virtualRivalButton: Button
    private lateinit var historyButton: Button
    private lateinit var settingsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        setupButtons()
        setupAnimations()
        setupButtonsHandlers()
    }

    private fun setupButtonsHandlers() {
        quickStartButton.setOnClickListener() { handleQuickStartButton() }
    }

    private fun setupButtons() {
        quickStartButton = findViewById<Button>(R.id.quick_start)
        virtualRivalButton = findViewById<Button>(R.id.virtual_rival)
        historyButton = findViewById<Button>(R.id.history)
        settingsButton = findViewById<Button>(R.id.settings)
    }

    private fun handleQuickStartButton() {
        val workoutIntent = Intent(this, WorkoutActivity::class.java)
        startActivity(workoutIntent)
    }

    private fun setupAnimations() {
        val animationDuration: Long = 600

        quickStartButton.alpha = 0f
        quickStartButton.translationX = -800f
        quickStartButton.animate()
            .setDuration(animationDuration)
            .translationX(0f)
            .alpha(1f)

        virtualRivalButton.alpha = 0f
        virtualRivalButton.translationX = 800f
        virtualRivalButton.animate()
            .setStartDelay(animationDuration - 150)
            .setDuration(animationDuration)
            .translationX(0f)
            .alpha(1f)

        historyButton.alpha = 0f
        historyButton.translationX = -800f
        historyButton.animate()
            .setStartDelay(2 * (animationDuration - 150))
            .setDuration(animationDuration)
            .translationX(0f)
            .alpha(1f)


        settingsButton.alpha = 0f
        settingsButton.translationX = 800f
        settingsButton.animate()
            .setStartDelay(3 * (animationDuration - 150))
            .setDuration(animationDuration)
            .translationX(0f)
            .alpha(1f)

    }

}
