package m.tech.collapse_text_view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import m.tech.navigation.AppDeepLink
import m.tech.navigation.DeepLinkConstants.COLLAPSE_TEXT_VIEW

@AppDeepLink(COLLAPSE_TEXT_VIEW)
internal class CollapseTextViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collapse_text_view)

        supportFragmentManager.commit(allowStateLoss = true) {
            replace(R.id.fragment_container_view, CollapsableTextViewFragment())
        }
    }
}