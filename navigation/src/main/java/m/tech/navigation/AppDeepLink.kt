package m.tech.navigation

import com.airbnb.deeplinkdispatch.DeepLinkSpec

/**
 * @author 89hnim
 * @since 31/07/2021
 */
@DeepLinkSpec(prefix = ["${DeepLinkConstants.APP_DEEPLINK_PREFIX}://"])
@Retention(value = AnnotationRetention.RUNTIME)
annotation class AppDeepLink(vararg val value: String)