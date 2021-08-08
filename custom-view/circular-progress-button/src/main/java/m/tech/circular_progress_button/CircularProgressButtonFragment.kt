package m.tech.circular_progress_button

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_circular_progress_button.*
import m.tech.navigation.DeepLinkNavigation

/**
 * @author 89hnim
 * @since 24/07/2021
 */
internal class CircularProgressButtonFragment : Fragment(R.layout.fragment_circular_progress_button) {

    private var percent = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_start.setOnClickListener {
            circular_progress_button.showProgress(true)

            //demo increase percent by 50
            percent += 50
            circular_progress_button.setPercent(percent)
        }

        circular_progress_button.setOnClickListener {
            circular_progress_button.cancel()
            percent = 0
            circular_progress_button.setImageResource(R.drawable.circular_progress_button_ic_example)
        }

        circular_progress_button.setImageResource(R.drawable.circular_progress_button_ic_example)

    }

}