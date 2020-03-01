package pl.piterowsky.runinga.model

import pl.piterowsky.runinga.config.Settings
import pl.piterowsky.runinga.util.DistanceUtils
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

            distance += DistanceUtils.calculateDistanceInKilometers(prevStep.latLng, step.latLng)
            if(Settings.RivalMode.isActive) {
                val delayUnitInSeconds = Settings.Global.TIMER_DELAY_VALUE / 1000
                rivalDistance += delayUnitInSeconds.toDouble() / Settings.RivalMode.paceInSeconds
            }
        }
    }

    fun getSteps() = this.steps

    fun getDistance() = this.distance

    fun getRivalDistance() = this.rivalDistance


}
