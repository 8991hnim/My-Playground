package m.tech.calendar_view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import kotlinx.android.synthetic.main.fragment_calendar_month_view.calendarView
import kotlinx.android.synthetic.main.fragment_calendar_month_view.switch_calendar
import m.tech.calendar_view.utils.CalendarUtils.SHOW_ALL
import m.tech.calendar_view.utils.CalendarUtils.SHOW_DOT
import m.tech.calendar_view.utils.CalendarUtils.SHOW_HEART
import m.tech.calendar_view.utils.CalendarUtils.SHOW_HEART_AND_DOT
import m.tech.calendar_view.utils.CalendarUtils.SHOW_HEART_AND_NOTE
import m.tech.calendar_view.utils.CalendarUtils.SHOW_NOTE
import m.tech.calendar_view.utils.CalendarUtils.SHOW_NOTE_AND_DOT
import java.util.*

/**
 * @author 89hnim
 * @since 17/08/2021
 */
internal class CalendarMonthViewFragment : Fragment(R.layout.fragment_calendar_month_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        switch_calendar.setOnClickListener {
            (requireActivity() as CalendarViewActivity).navToWeekView()
        }

        calendarView.setOnCalendarSelectListener(object : CalendarView.OnCalendarSelectListener {
            override fun onCalendarOutOfRange(calendar: Calendar?) {
                Log.d(
                    "icd",
                    "onCalendarOutOfRange: ${calendar.toString()} - ${calendar?.hasScheme()}"
                )
            }

            override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
                Log.d(
                    "icd",
                    "onCalendarSelect: ${calendar.toString()} - ${calendar?.hasScheme()}"
                )
            }

        })

        calendarView.setOnMonthChangeListener { year, month ->
            Toast.makeText(context, "MonthFragment: $month/$year", Toast.LENGTH_SHORT).show()
        }

        val cYear: Int = calendarView.curYear
        val cMonth: Int = calendarView.curMonth
        val cDay: Int = calendarView.curDay
        calendarView.hideWeekBar()
        calendarView.hideMonthPager()
        calendarView.showMonthRecycle()
        calendarView.scrollToCalendar(cYear, cMonth, cDay)
        initSchemeData()
    }

    private fun initSchemeData() {
        val map: HashMap<String, Calendar> = HashMap()
        val year: Int = calendarView.curYear
        val month: Int = calendarView.curMonth
        map[getSchemeCalendar(year, month, 10, Color.RED, SHOW_ALL).toString()] =
            getSchemeCalendar(year, month, 10, Color.RED, SHOW_ALL)

        map[getSchemeCalendar(year, month, 23, Color.RED, SHOW_ALL).toString()] =
            getSchemeCalendar(year, month, 23, Color.RED, SHOW_ALL)

        map[getSchemeCalendar(year, month, 19, Color.RED, SHOW_ALL).toString()] =
            getSchemeCalendar(year, month, 19, Color.RED, SHOW_ALL)

        map[getSchemeCalendar(year, month, 22, Color.RED, SHOW_HEART).toString()] =
            getSchemeCalendar(year, month, 22, Color.RED, SHOW_HEART)

        map[getSchemeCalendar(year, month, 16, Color.BLUE, SHOW_HEART_AND_DOT).toString()] =
            getSchemeCalendar(year, month, 16, Color.BLUE, SHOW_HEART_AND_DOT)

        map[getSchemeCalendar(year, month, 17, Color.BLUE, SHOW_HEART_AND_NOTE).toString()] =
            getSchemeCalendar(year, month, 17, Color.BLUE, SHOW_HEART_AND_NOTE)

        map[getSchemeCalendar(year, month, 18, Color.BLUE, SHOW_NOTE).toString()] =
            getSchemeCalendar(year, month, 18, Color.BLUE, SHOW_NOTE)

        map[getSchemeCalendar(year, month, 21, Color.BLUE, SHOW_DOT).toString()] =
            getSchemeCalendar(year, month, 21, Color.BLUE, SHOW_DOT)

        map[getSchemeCalendar(year, month, 24, Color.BLUE, SHOW_NOTE_AND_DOT).toString()] =
            getSchemeCalendar(year, month, 24, Color.BLUE, SHOW_NOTE_AND_DOT)

        calendarView.setSchemeDate(map)
    }

    private fun getSchemeCalendar(
        year: Int,
        month: Int,
        day: Int,
        color: Int,
        text: String
    ): Calendar {
        val calendar = Calendar()
        calendar.year = year
        calendar.month = month
        calendar.day = day
        calendar.schemeColor = color //If the color is marked separately, this color will be used
        calendar.scheme = text
        return calendar
    }

}