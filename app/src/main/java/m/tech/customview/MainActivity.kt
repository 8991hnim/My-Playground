package m.tech.customview

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import m.tech.navigation.DeepLinkNavigation
import m.tech.tree_view.SampleModel
import m.tech.tree_view.TreeViewFragment.Companion.getNodes

/**
 * @author 89hnim
 * @since 24/07/2021
 * Just a container class
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            tree_view.initialize(
                itemLayoutRes = R.layout.item_node_main,
                nodes = getNodes(),
                showAllNodes = false,
                onBind = { view,  position, item ->
                    val myItem = (item as? SampleModel) ?: return@initialize
                    view.findViewById<TextView>(m.tech.tree_view.R.id.node_name).text = myItem.name
                }
            )
        }
    }

    fun navCircularProgressButton(view: View) {
        DeepLinkNavigation.CustomViewScope.navToCircularProgressButton(this)
    }

    fun navCollapsableTextView(view: View) {
        DeepLinkNavigation.CustomViewScope.navToCollapseTextView(this)
    }

    fun navPolygonImageView(view: View) {
        DeepLinkNavigation.CustomViewScope.navToPolygonImageView(this)
    }

    fun navCalendarView(view: View) {
        DeepLinkNavigation.CustomViewScope.navToCalendarView(this)
    }

    fun navPolygonProgressView(view: View) {
        DeepLinkNavigation.CustomViewScope.navToPolygonProgressView(this)
    }

    fun navTreeView(view: View) {
        DeepLinkNavigation.CustomViewScope.navToTreeView(this)
    }

}