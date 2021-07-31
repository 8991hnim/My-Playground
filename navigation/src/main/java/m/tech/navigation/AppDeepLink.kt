package m.tech.navigation

import com.airbnb.deeplinkdispatch.DeepLinkSpec

@DeepLinkSpec(prefix = ["${DeepLinkConstants.APP_DEEPLINK_PREFIX}://"])
@Retention(value = AnnotationRetention.RUNTIME)
annotation class AppDeepLink(vararg val value: String)