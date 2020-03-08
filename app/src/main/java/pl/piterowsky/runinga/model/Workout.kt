package pl.piterowsky.runinga.model

import pl.piterowsky.runinga.config.RivalMode
import pl.piterowsky.runinga.util.DistanceUtils

class Workout(private val workoutMode: WorkoutMode) {

    private val steps: MutableList<Step> = ArrayList()
    private var rivalDistance: Double = 0.0
    private var distance: Double = 0.0
    var isDistanceReached: Boolean = false

    fun addStep(step: Step) {
        steps.add(step)
        updateDistance()
    }

    private fun updateDistance() {
        if (steps.size > 1) {
            val step: Step = steps[steps.size - 1]
            val prevStep: Step = steps[steps.size - 2]

            distance += DistanceUtils.getDistanceInKm(prevStep.latLng, step.latLng)
            if (RivalMode.isActive) {
                rivalDistance += workoutMode.calculateRivalDistanceOnUnitTime()
            }
        }
    }

    fun isRivalWinning() = rivalDistance > distance

    fun getSteps() = this.steps

    fun getDistance() = this.distance

    fun getRivalDistance() = this.rivalDistance

}
