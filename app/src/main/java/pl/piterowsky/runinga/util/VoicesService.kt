package pl.piterowsky.runinga.util

import pl.piterowsky.runinga.R
import pl.piterowsky.runinga.config.Language
import java.util.*

class VoicesService {

    var speedUpVoice: Int = R.raw.en_maja_speed_up
    var slowDownVoice: Int = R.raw.en_maja_slow_down
    var reachedDistanceVoice: Int = R.raw.en_maja_distance_reached

    init {
        when (Locale.getDefault().language) {
            Language.POLISH.language -> {
                speedUpVoice = R.raw.pl_maja_speed_up
                slowDownVoice = R.raw.pl_maja_slow_down
                reachedDistanceVoice = R.raw.pl_maja_distance_reached
            }
        }
    }
}
