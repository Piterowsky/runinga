package pl.piterowsky.runinga.util

class LoggerTag {
    companion object {
        private const val TAG_PREFIX = "RUNINGA"

        const val TAG_LOCATION = "$TAG_PREFIX.LOCATION"
        const val TAG_WORKOUT = "$TAG_PREFIX.WORKOUT"
        const val TAG_WORKOUT_TIMER = "$TAG_WORKOUT.TIMER"
    }
}
