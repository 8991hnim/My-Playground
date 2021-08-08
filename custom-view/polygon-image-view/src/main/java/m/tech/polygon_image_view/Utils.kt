package m.tech.polygon_image_view

import android.content.res.Resources
import android.util.TypedValue

/**
 * @author 89hnim
 * @since 08/08/2021
 */
object Utils {

    val Number.toPx
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        )

}