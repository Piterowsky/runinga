package pl.piterowsky.runinga.model

import java.util.*

class Workout {

    val workoutStartTimestamp: Long = System.nanoTime()
    var workoutEndTimestamp: Long = -1
    val steps: MutableList<Step> = Collections.emptyList()

}
