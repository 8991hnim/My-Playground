package m.tech.navigation

import android.content.Context

/**
 * @author 89hnim
 * @since 31/07/2021
 */
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

        fun navToCalendarView(context: Context) {
            DeepLinkManager.CalendarView.getDeepLinkIntent(context).apply {
                context.startActivity(this)
            }
        }

        fun navToPolygonProgressView(context: Context) {
            DeepLinkManager.PolygonProgressView.getDeepLinkIntent(context).apply {
                context.startActivity(this)
            }
        }

        fun navToTreeView(context: Context){
            DeepLinkManager.TreeView.getDeepLinkIntent(context).apply {
                context.startActivity(this)
            }
        }

        fun navToOtpView(context: Context){
            DeepLinkManager.OtpView.getDeepLinkIntent(context).apply {
                context.startActivity(this)
            }
        }

        fun navToSpeedBarView(context: Context){
            DeepLinkManager.SpeedBarView.getDeepLinkIntent(context).apply {
                context.startActivity(this)
            }
        }
    }


}