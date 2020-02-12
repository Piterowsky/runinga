package pl.piterowsky.runinga.model

import pl.piterowsky.runinga.config.Settings
import pl.piterowsky.runinga.util.WorkoutUtils
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.collections.ArrayList

class Workout {

    private val steps: MutableList<Step> = ArrayList()
    private var rivalDistance: Double = 0.0
    private var distance: Double = 0.0

    fun addStep(step: Step) {
        steps.add(step)
        updateDistance()
    }

    private fun updateDistance() {
        if(steps.size > 1) {
            val step: Step = steps[steps.size - 1]
            val prevStep: Step = steps[steps.size - 2]

            distance += WorkoutUtils.calculateDistanceInMeters(prevStep.latLng, step.latLng)
            if(Settings.RivalMode.isActive) {
                val timeToDoubleConverter = 1.66
                rivalDistance +=  (Settings.Global.TIMER_DELAY_VALUE / 100) * timeToDoubleConverter / Settings.RivalMode.pace
            }
        }
    }

    fun getDistanceInKilometers(distance: Double): String {
        return BigDecimal(distance / 1000)
            .round(MathContext(2, RoundingMode.DOWN))
            .toString()
    }

    fun getSteps() = this.steps

    fun getDistance() = this.distance

    fun getRivalDistance() = this.rivalDistance


}
