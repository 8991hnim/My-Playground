package m.tech.polygon_progress_view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import m.tech.navigation.AppDeepLink
import m.tech.navigation.DeepLinkConstants.POLYGON_PROGRESS_VIEW

/**
 * @author 89hnim
 * @since 22/08/2021
 */
@AppDeepLink(POLYGON_PROGRESS_VIEW)
internal class PolygonProgressViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_polygon_progress_view)

        supportFragmentManager.commit(allowStateLoss = true) {
            setReorderingAllowed(true)
            replace(R.id.fragment_container_view, PolygonProgressViewFragment::class.java, null)
        }
    }
}