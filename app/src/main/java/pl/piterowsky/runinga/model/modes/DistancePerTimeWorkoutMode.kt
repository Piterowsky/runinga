package pl.piterowsky.runinga.model.modes

import pl.piterowsky.runinga.config.RivalMode
import pl.piterowsky.runinga.model.WorkoutMode
import pl.piterowsky.runinga.util.TimeUtils

class DistancePerTimeWorkoutMode : WorkoutMode {
    override fun calculateRivalDistanceOnUnitTime(): Double {
        val format = TimeUtils.Format.DISTANCE_PER_TIME_FORMAT

        return RivalMode.distance.toDouble()
            .div(TimeUtils.stringToSecondsShortFormat(format, RivalMode.time).toDouble())
    }
}
