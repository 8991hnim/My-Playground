package m.tech.customview

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import m.tech.customview.circular_progress_button.CircularProgressButtonFragment
import m.tech.customview.collapse_text.CollapsableTextViewFragment

/**
 * @author 89hnim
 * @since 24/07/2021
 * Just a container class
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun navCircularProgressButton(view: View) {
        supportFragmentManager.commit(allowStateLoss = true) {
            Toast.makeText(this@MainActivity, "navCircularProgressButton", Toast.LENGTH_SHORT).show()
            replace(R.id.fragment_container_view, CircularProgressButtonFragment())
            addToBackStack(null)
        }
    }

    fun navCollapsableTextView(view: View) {
        supportFragmentManager.commit(allowStateLoss = true) {
            Toast.makeText(this@MainActivity, "navCollapsableTextView", Toast.LENGTH_SHORT).show()
            replace(R.id.fragment_container_view, CollapsableTextViewFragment())
            addToBackStack(null)
        }
    }
}