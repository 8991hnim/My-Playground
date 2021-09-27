package m.tech.calendar_view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.haibin.calendarview.Calendar
import kotlinx.android.synthetic.main.fragment_calendar_week_view.calendarView
import kotlinx.android.synthetic.main.fragment_calendar_week_view.switch_calendar
import m.tech.calendar_view.utils.CalendarUtils
import java.util.HashMap

/**
 * @author 89hnim
 * @since 17/08/2021
 */
internal class CalendarWeekViewFragment : Fragment(R.layout.fragment_calendar_week_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switch_calendar.setOnClickListener {
            (requireActivity() as CalendarViewActivity).navToMonthView()
        }

        calendarView.setOnMonthChangeListener { year, month ->
            Toast.makeText(context, "WeekFragment: $month/$year", Toast.LENGTH_SHORT).show()
        }
        initSchemeData()
    }

    private fun initSchemeData() {
        val map: HashMap<String, Calendar> = HashMap()
        val year: Int = calendarView.curYear
        val month: Int = calendarView.curMonth
        map[getSchemeCalendar(year, month, 10, Color.RED, CalendarUtils.SHOW_ALL).toString()] =
            getSchemeCalendar(year, month, 10, Color.RED, CalendarUtils.SHOW_ALL)

        map[getSchemeCalendar(year, month, 23, Color.RED, CalendarUtils.SHOW_ALL).toString()] =
            getSchemeCalendar(year, month, 23, Color.RED, CalendarUtils.SHOW_ALL)

        map[getSchemeCalendar(year, month, 19, Color.RED, CalendarUtils.SHOW_ALL).toString()] =
            getSchemeCalendar(year, month, 19, Color.RED, CalendarUtils.SHOW_ALL)

        map[getSchemeCalendar(year, month, 22, Color.RED, CalendarUtils.SHOW_HEART).toString()] =
            getSchemeCalendar(year, month, 22, Color.RED, CalendarUtils.SHOW_HEART)

        map[getSchemeCalendar(year, month, 16, Color.BLUE, CalendarUtils.SHOW_HEART_AND_DOT).toString()] =
            getSchemeCalendar(year, month, 16, Color.BLUE, CalendarUtils.SHOW_HEART_AND_DOT)

        map[getSchemeCalendar(year, month, 17, Color.BLUE, CalendarUtils.SHOW_HEART_AND_NOTE).toString()] =
            getSchemeCalendar(year, month, 17, Color.BLUE, CalendarUtils.SHOW_HEART_AND_NOTE)

        map[getSchemeCalendar(year, month, 18, Color.BLUE, CalendarUtils.SHOW_NOTE).toString()] =
            getSchemeCalendar(year, month, 18, Color.BLUE, CalendarUtils.SHOW_NOTE)

        map[getSchemeCalendar(year, month, 21, Color.BLUE, CalendarUtils.SHOW_DOT).toString()] =
            getSchemeCalendar(year, month, 21, Color.BLUE, CalendarUtils.SHOW_DOT)

        map[getSchemeCalendar(year, month, 24, Color.BLUE, CalendarUtils.SHOW_NOTE_AND_DOT).toString()] =
            getSchemeCalendar(year, month, 24, Color.BLUE, CalendarUtils.SHOW_NOTE_AND_DOT)

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