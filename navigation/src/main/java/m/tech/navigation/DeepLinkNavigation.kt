package m.tech.navigation

import android.content.Context

object DeepLinkNavigation {

    /**
     * create a scope wrapper b/c navigate to 1 feature can have many screens
     */
    object CustomViewScope {
        fun navToCollapseTextView(context: Context) {
            DeepLinkManager.CollapseTextView.getDeepLinkIntent(context).apply {
                context.startActivity(this)
            }
        }

        fun navToCircularProgressButton(context: Context) {
            DeepLinkManager.CircularProgressButton.getDeepLinkIntent(context).apply {
                context.startActivity(this)
            }
        }

        fun navToPolygonImageView(context: Context) {
            DeepLinkManager.PolygonImageView.getDeepLinkIntent(context).apply {
                context.startActivity(this)
            }
        }
    }


}