package m.tech.polygon_progress_view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_polygon_progress_view.*

/**
 * @author 89hnim
 * @since 22/08/2021
 */
internal class PolygonProgressViewFragment: Fragment(R.layout.fragment_polygon_progress_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        polygon_view.init(28, 1)

    }

}