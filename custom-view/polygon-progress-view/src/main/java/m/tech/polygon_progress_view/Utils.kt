package m.tech.polygon_progress_view

import android.content.res.Resources
import android.util.TypedValue

/**
 * @author 89hnim
 * @since 22/08/2021
 */
object Utils {

    val Number.toPx
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        )

}