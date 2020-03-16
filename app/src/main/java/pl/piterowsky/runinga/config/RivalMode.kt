package pl.piterowsky.runinga.config

class RivalMode {
    companion object {
        var isActive: Boolean = false
        var modeType: ModeType = ModeType.DISTANCE_PER_TIME

        /**
         * Format mm:ss
         */
        var pace: String = "";

        /**
         * Format in km
         */
        var distance: String = "";

        /**
         * Format in hh:ss
         */
        var time: String = "";

        fun getModeValue(): String {
            return when(modeType) {
                ModeType.PACE -> pace
                ModeType.DISTANCE_PER_TIME -> "$distance / $time"
            }
        }
    }

    enum class ModeType {
        PACE,
        DISTANCE_PER_TIME,
    }
}
