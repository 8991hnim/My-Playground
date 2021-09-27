package m.tech.calendar_view.view

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.TextView
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.WeekBar
import m.tech.calendar_view.R

/**
 * @author 89hnim
 * @since 21/08/2021
 */
internal class CustomWeekBar(context: Context): WeekBar(context) {

    private var mPreSelectedIndex = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_week_bar, this, true)
        setBackgroundColor(Color.WHITE)
    }

    override fun onDateSelected(calendar: Calendar?, weekStart: Int, isClick: Boolean) {
        getChildAt(mPreSelectedIndex).isSelected = false
        val viewIndex = getViewIndexByCalendar(calendar, weekStart)
        getChildAt(viewIndex).isSelected = true
        mPreSelectedIndex = viewIndex
    }

    /**
     * When the beginning of the week changes, using a custom layout needs to rewrite this method to avoid problems
     *
     * @param weekStart 周起始
     */
    override fun onWeekStartChange(weekStart: Int) {
        for (i in 0 until childCount) {
            (getChildAt(i) as TextView).text = getWeekString(i, weekStart)
        }
    }

    /**
     * Or week text, this method is only for the parent class
     *
     * @param index     index
     * @param weekStart weekStart
     * @return 或者周文本
     */
    private fun getWeekString(index: Int, weekStart: Int): String? {
        val weeks = context.resources.getStringArray(R.array.custom_week_string_array)
        if (weekStart == 1) {
            return weeks[index]
        }
        return if (weekStart == 2) {
            weeks[if (index == 6) 0 else index + 1]
        } else weeks[if (index == 0) 6 else index - 1]
    }

}