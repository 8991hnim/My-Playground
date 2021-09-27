package m.tech.calendar_view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import m.tech.navigation.AppDeepLink
import m.tech.navigation.DeepLinkConstants.CALENDAR_VIEW

/**
 * @author 89hnim
 * @since 17/08/2021
 */
@AppDeepLink(CALENDAR_VIEW)
internal class CalendarViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        supportFragmentManager.commit(allowStateLoss = true) {
            replace(R.id.fragment_container_view, CalendarWeekViewFragment())
        }
    }

    fun navToMonthView(){
        supportFragmentManager.commit(allowStateLoss = true) {
            replace(R.id.fragment_container_view, CalendarMonthViewFragment())
        }
    }

    fun navToWeekView(){
        supportFragmentManager.commit(allowStateLoss = true) {
            replace(R.id.fragment_container_view, CalendarWeekViewFragment())
        }
    }
}