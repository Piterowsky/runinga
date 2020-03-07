package pl.piterowsky.runinga.util

import org.junit.Assert.assertEquals
import org.junit.Test
import java.security.InvalidParameterException

/**
 * @author piterowsky
 */
class TimeUtilsTest {

    @Test
    fun test_stringToSeconds_returnsCorrect() {
        assertEquals(134, TimeUtils.stringToSeconds(TimeUtils.Format.PACE_FORMAT, "2:14"))
        assertEquals(8040, TimeUtils.stringToSeconds(TimeUtils.Format.DISTANCE_PER_TIME_FORMAT, "02:14"))
        assertEquals(8040, TimeUtils.stringToSeconds(TimeUtils.Format.DISTANCE_PER_TIME_FORMAT, "2:14"))
        assertEquals(0, TimeUtils.stringToSeconds(TimeUtils.Format.DISTANCE_PER_TIME_FORMAT, "00:00"))
        assertEquals(0, TimeUtils.stringToSeconds(TimeUtils.Format.DISTANCE_PER_TIME_FORMAT, "0:0"))
    }

    @Test(expected = InvalidParameterException::class)
    fun test_stringToSeconds_returnsCorrects() {
        TimeUtils.stringToSeconds(TimeUtils.Format.DISTANCE_PER_TIME_FORMAT, "0")
    }



}
