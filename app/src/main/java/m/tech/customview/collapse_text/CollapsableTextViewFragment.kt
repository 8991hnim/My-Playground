package m.tech.customview.collapse_text

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_collapsable_text_view.*
import m.tech.customview.R

/**
 * @author 89hnim
 * @since 25/07/2021
 */
class CollapsableTextViewFragment : Fragment(R.layout.fragment_collapsable_text_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //collapsable normal
        tv_collapsable_normal.setText(
            "This is a full text of collapsable text view without scrollable and no break line. Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
            readMoreLimit = 150,
            readMoreTextColor = Color.GRAY,
            readMoreText = "See more...",
            isFullTextShow = false
        )

        tv_collapsable_normal.setOnClickListener {
            tv_collapsable_normal.toggleCollapse(true)
        }

        //collapsable without anim
        tv_collapsable_without_anim.setText(
            "This is a full text of collapsable text view without animation and no break line. Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
            readMoreLimit = 150,
            readMoreTextColor = Color.GRAY,
            readMoreText = "See more...",
            isFullTextShow = true
        )

        tv_collapsable_without_anim.setOnClickListener {
            tv_collapsable_without_anim.toggleCollapse(false)
        }

        //collapsable detect many line breaks
        tv_collapsable_br.setText(
            "line \n b\nr\ne\na\nk",
            readMoreLimit = 150,
            readMoreTextColor = Color.GRAY,
            readMoreText = "See more...",
            isFullTextShow = false
        )

        tv_collapsable_br.setOnClickListener {
            tv_collapsable_br.toggleCollapse(true)
        }

        //collapsable with scrollable
        tv_collapsable_scroll.setText(
            "This is a full text of collapsable text view with scrollable and no break line. Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1600s",
            readMoreLimit = 150,
            readMoreTextColor = Color.GRAY,
            readMoreText = "See more...",
            isFullTextShow = false
        )

        tv_collapsable_scroll.setOnClickListener {
            tv_collapsable_scroll.toggleCollapse(true)
        }

    }

}