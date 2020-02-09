package pl.piterowsky.runinga.model

import pl.piterowsky.runinga.util.WorkoutUtils
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.collections.ArrayList

class Workout {

    private val steps: MutableList<Step> = ArrayList()
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
        }
    }

    fun getDistanceInKilometers(): String {
        return BigDecimal(this.distance / 1000)
            .round(MathContext(2, RoundingMode.DOWN))
            .toString()
    }

    fun getSteps() = this.steps

    fun getDistance() = this.distance


}
