package m.tech.calendar_view.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.haibin.calendarview.Calendar
import m.tech.calendar_view.R
import java.util.concurrent.TimeUnit
import kotlin.math.abs

/**
 * @author 89hnim
 * @since 21/08/2021
 */
internal object CalendarUtils {

    enum class DAY_TYPE {
        NORMAL, PERIOD, OVULATION, EASY_PREGNANT
    }

    const val PERIOD_DAY = 6
    const val PERIOD_LENGTH = 28

    const val START_DAY = 10
    const val START_MONTH = 8
    const val START_YEAR = 2021

    //schemes
    const val SHOW_ALL = "SHOW_ALL"
    const val SHOW_HEART = "SHOW_HEART"
    const val SHOW_NOTE = "SHOW_NOTE"
    const val SHOW_DOT = "SHOW_DOT"
    const val SHOW_HEART_AND_NOTE = "SHOW_HEART_AND_NOTE"
    const val SHOW_HEART_AND_DOT = "SHOW_HEART_AND_DOT"
    const val SHOW_NOTE_AND_DOT = "SHOW_NOTE_AND_DOT"

    //formula
    fun getDayType(calendar: Calendar): Pair<DAY_TYPE, String> {
        val start = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.YEAR, START_YEAR)
            set(java.util.Calendar.MONTH, START_MONTH - 1)
            set(java.util.Calendar.DAY_OF_MONTH, START_DAY)
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }
        val rangeTime = calendar.timeInMillis - start.timeInMillis

        var rangeDate: Int = (TimeUnit.DAYS.convert(
            abs(calendar.timeInMillis - start.timeInMillis),
            TimeUnit.MILLISECONDS
        ) % PERIOD_LENGTH).toInt()

        rangeDate = when {
            rangeDate == 0 -> 1
            rangeTime < 0 -> PERIOD_LENGTH - rangeDate + 1
            else -> rangeDate + 1
        }

        return when {
            rangeDate < PERIOD_DAY -> {
                DAY_TYPE.PERIOD to rangeDate.toString()
            }
            rangeDate == 14 -> {
                DAY_TYPE.OVULATION to rangeDate.toString()
            }
            rangeDate in 10..18 -> {
                DAY_TYPE.EASY_PREGNANT to rangeDate.toString()
            }
            else -> {
                DAY_TYPE.NORMAL to rangeDate.toString()
            }
        }
    }

    //bitmap
    private var heartBitmap: Bitmap? = null
    private var dotBitmap: Bitmap? = null
    private var noteBitmap: Bitmap? = null

    fun heartBitmap(context: Context): Bitmap? {
        if (heartBitmap == null) {
            heartBitmap = AppCompatResources.getDrawable(context, R.drawable.ic_heart)?.toBitmap()
        }
        return heartBitmap
    }

    fun dotBitmap(context: Context): Bitmap? {
        if (dotBitmap == null) {
            dotBitmap = AppCompatResources.getDrawable(context, R.drawable.ic_dot)?.toBitmap()
        }
        return dotBitmap
    }

    fun noteBitmap(context: Context): Bitmap? {
        if (noteBitmap == null) {
            noteBitmap = AppCompatResources.getDrawable(context, R.drawable.ic_note)?.toBitmap()
        }
        return noteBitmap
    }

}