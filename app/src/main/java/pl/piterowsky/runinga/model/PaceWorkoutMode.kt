package pl.piterowsky.runinga.model

import pl.piterowsky.runinga.config.RivalMode
import pl.piterowsky.runinga.config.Settings
import pl.piterowsky.runinga.util.TimeUtils

class PaceWorkoutMode : WorkoutMode {

    /**
     * Calculates rival distance on timer unit
     *
     * Pace = Time / Km
     *
     * Example for timer unit 2s and pace 1:00 for 1 km
     * 60s - 1km
     * 2s  - x
     * 2s * 1km / 60s = Distance that rival traveled by the time 2s
     */
    override fun calculateRivalDistanceOnUnitTime(): Double {
        val delayUnitInSeconds = Settings.Global.TIMER_DELAY_VALUE / 1000
        val oneKilometer = 1

        val format = TimeUtils.Format.PACE_FORMAT
        return delayUnitInSeconds.toDouble() * oneKilometer / TimeUtils.stringToSeconds(format, RivalMode.pace)
    }
}
