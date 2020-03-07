package pl.piterowsky.runinga.util

import java.security.InvalidParameterException

class TimeUtils {

    companion object {

        fun stringToSeconds(format: Format, time: String): Int {
            var timeInSeconds = 0
            val parts = time.split(Regex(":"))

            if(parts.size < 2) {
                throw InvalidParameterException("Splitted time should have two parts, actual: ${parts.size}")
            }

            when (format) {
                Format.PACE_FORMAT -> {
                    timeInSeconds += parts[0].toInt() * 60 // Minutes
                    timeInSeconds += parts[1].toInt() // Seconds
                }
                Format.DISTANCE_PER_TIME_FORMAT -> {
                    timeInSeconds += parts[0].toInt() * 3600 // Hours
                    timeInSeconds += parts[1].toInt() * 60 // Minutes
                }
            }
            return timeInSeconds
        }
    }

    enum class Format {
        PACE_FORMAT,
        DISTANCE_PER_TIME_FORMAT
    }


}
