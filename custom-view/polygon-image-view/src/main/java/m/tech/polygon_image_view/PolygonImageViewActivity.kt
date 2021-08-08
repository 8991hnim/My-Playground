package m.tech.polygon_image_view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import m.tech.navigation.AppDeepLink
import m.tech.navigation.DeepLinkConstants.POLYGON_IMAGE_VIEW

@AppDeepLink(POLYGON_IMAGE_VIEW)
internal class PolygonImageViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_polygon_image_view)

        supportFragmentManager.commit(allowStateLoss = true) {
            setReorderingAllowed(true)
            replace(R.id.fragment_container_view, PolygonImageViewFragment::class.java, null)
        }
    }

}