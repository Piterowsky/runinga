package pl.piterowsky.runinga.model

import java.util.*
import kotlin.collections.ArrayList

class Workout {

    val workoutStartTimestamp: Long = System.nanoTime()
    var workoutEndTimestamp: Long = -1
    val steps: MutableList<Step> = ArrayList()

}
