package m.tech.polygon_image_view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_polygon_image_view.*
import m.tech.polygon_image_view.Utils.toPx

/**
 * @author 89hnim
 * @since 08/08/2021
 */
internal class PolygonImageViewFragment : Fragment(R.layout.fragment_polygon_image_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_view_normal.setBitmap(R.drawable.ic_test_large, 200.toPx.toInt(), 200.toPx.toInt())
        iv_view_large.setBitmap(R.drawable.ic_test_large, 250.toPx.toInt(), 250.toPx.toInt())
    }

}