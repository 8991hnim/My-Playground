package m.tech.calendar_view.utils

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

    val Number.fromSpToPx
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        )

}