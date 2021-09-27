package m.tech.tree_view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import m.tech.navigation.AppDeepLink
import m.tech.navigation.DeepLinkConstants

/**
 * @author 89hnim
 * @since 27/09/2021
 */
@AppDeepLink(DeepLinkConstants.TREE_VIEW)
internal class TreeViewActivity : AppCompatActivity(R.layout.activity_tree_view) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.commit(allowStateLoss = true) {
            setReorderingAllowed(true)
            replace(R.id.fragment_container_view, TreeViewFragment::class.java, null)
        }
    }
}