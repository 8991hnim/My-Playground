package m.tech.circular_progress_button

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import m.tech.navigation.AppDeepLink
import m.tech.navigation.DeepLinkConstants.CIRCULAR_PROGRESS_BUTTON

@AppDeepLink(CIRCULAR_PROGRESS_BUTTON)
internal class CircularProgressButtonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circular_progress_button)

        supportFragmentManager.commit(allowStateLoss = true) {
            replace(R.id.fragment_container_view, CircularProgressButtonFragment())
        }
    }
}