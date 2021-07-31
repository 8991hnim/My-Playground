package m.tech.customview

import android.app.Activity
import android.os.Bundle
import com.airbnb.deeplinkdispatch.BaseDeepLinkDelegate
import com.airbnb.deeplinkdispatch.DeepLinkHandler
import m.tech.circular_progress_button.CircularProgressButtonDeepLinkModule
import m.tech.circular_progress_button.CircularProgressButtonDeepLinkModuleRegistry
import m.tech.collapse_text_view.CollapseTextViewDeepLinkModule
import m.tech.collapse_text_view.CollapseTextViewDeepLinkModuleRegistry

@DeepLinkHandler(value = [
    AppDeepLinkModule::class,
    CircularProgressButtonDeepLinkModule::class,
    CollapseTextViewDeepLinkModule::class
])
class DeepLinkActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val deepLinkDelegate = BaseDeepLinkDelegate(listOf(
            AppDeepLinkModuleRegistry(),
            CircularProgressButtonDeepLinkModuleRegistry(),
            CollapseTextViewDeepLinkModuleRegistry()
        ))

        deepLinkDelegate.dispatchFrom(this)

        finish()
    }

}