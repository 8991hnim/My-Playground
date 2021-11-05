package m.tech.customview

import android.app.Activity
import android.os.Bundle
import com.airbnb.deeplinkdispatch.BaseDeepLinkDelegate
import com.airbnb.deeplinkdispatch.DeepLinkHandler
import com.gapo.otp_view.OtpViewDeepLinkModule
import com.gapo.otp_view.OtpViewDeepLinkModuleRegistry
import m.tech.calendar_view.CalendarViewDeepLinkModule
import m.tech.calendar_view.CalendarViewDeepLinkModuleRegistry
import m.tech.circular_progress_button.CircularProgressButtonDeepLinkModule
import m.tech.circular_progress_button.CircularProgressButtonDeepLinkModuleRegistry
import m.tech.collapse_text_view.CollapseTextViewDeepLinkModule
import m.tech.collapse_text_view.CollapseTextViewDeepLinkModuleRegistry
import m.tech.polygon_image_view.PolygonImageViewDeepLinkModule
import m.tech.polygon_image_view.PolygonImageViewDeepLinkModuleRegistry
import m.tech.polygon_progress_view.PolygonProgressViewDeepLinkModule
import m.tech.polygon_progress_view.PolygonProgressViewDeepLinkModuleRegistry
import m.tech.tree_view.TreeViewDeepLinkModule
import m.tech.tree_view.TreeViewDeepLinkModuleRegistry

/**
 * @author 89hnim
 * @since 31/07/2021
 */
@DeepLinkHandler(
    value = [
        AppDeepLinkModule::class,
        CircularProgressButtonDeepLinkModule::class,
        CollapseTextViewDeepLinkModule::class,
        PolygonImageViewDeepLinkModule::class,
        CalendarViewDeepLinkModule::class,
        PolygonProgressViewDeepLinkModule::class,
        TreeViewDeepLinkModule::class,
        OtpViewDeepLinkModule::class,
    ]
)
class DeepLinkActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val deepLinkDelegate = BaseDeepLinkDelegate(
            listOf(
                AppDeepLinkModuleRegistry(),
                CircularProgressButtonDeepLinkModuleRegistry(),
                CollapseTextViewDeepLinkModuleRegistry(),
                PolygonImageViewDeepLinkModuleRegistry(),
                CalendarViewDeepLinkModuleRegistry(),
                PolygonProgressViewDeepLinkModuleRegistry(),
                TreeViewDeepLinkModuleRegistry(),
                OtpViewDeepLinkModuleRegistry()
            )
        )

        deepLinkDelegate.dispatchFrom(this)

        finish()
    }

}