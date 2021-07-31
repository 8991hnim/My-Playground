package m.tech.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import m.tech.navigation.DeepLinkConstants.APP_DEEPLINK_PREFIX
import m.tech.navigation.DeepLinkConstants.CIRCULAR_PROGRESS_BUTTON
import m.tech.navigation.DeepLinkConstants.COLLAPSE_TEXT_VIEW

internal sealed class DeepLinkManager {

    abstract val link: String

    fun getDeepLinkIntent(context: Context) = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("$APP_DEEPLINK_PREFIX://$link")
        setPackage(context.packageName)
    }

    object CollapseTextView : DeepLinkManager() {
        override val link: String
            get() = COLLAPSE_TEXT_VIEW

    }

    object CircularProgressButton : DeepLinkManager() {
        override val link: String
            get() = CIRCULAR_PROGRESS_BUTTON

    }

}