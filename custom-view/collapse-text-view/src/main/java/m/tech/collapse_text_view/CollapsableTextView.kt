package m.tech.collapse_text_view

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.ScrollingMovementMethod
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.toSpanned
import androidx.transition.TransitionManager

/**
 * @author 89hnim
 * @since 25/07/2021
 * Collapsable Text like Facebook see more text in feeds
 */
class CollapsableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var isFullTextShow = false
    private var fullText: Spanned = SpannableString("")
    private var shortText: Spanned = SpannableString("")

    init {
        movementMethod = ScrollingMovementMethod()
    }

    fun setText(
        fullText: String,
        readMoreLimit: Int = NO_LIMIT, //default no read more
        readMoreTextColor: Int = Color.GRAY, //default color read more text
        readMoreText: String = DEFAULT_READ_MORE_TEXT, //default read more string
        isFullTextShow: Boolean = true //default show full text
    ) {
        this.isFullTextShow = isFullTextShow
        this.fullText = SpannableString(fullText)

        this.shortText = getShortSpannable(fullText, readMoreLimit)

        //case text can collapse. append read more
        if (isCollapsable()) {
            val shortSpan = SpannableStringBuilder(shortText)
            shortSpan.append(SPACE)
            shortSpan.append(readMoreText)

            shortSpan.setSpan(
                ForegroundColorSpan(readMoreTextColor),
                shortSpan.length - readMoreText.length,
                shortSpan.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )

            this.shortText = shortSpan
        } else {
            //text can't collapse, always show fulltext
            this.isFullTextShow = true
        }

        val text = if (isFullTextShow) {
            fullText
        } else {
            shortText
        }

        setText(text, BufferType.SPANNABLE)
    }

    /**
     * 2 case
     * case 1: [readMoreLimit] == [NO_LIMIT]
     * case 2: [readMoreLimit] is longer than [fullText]
     * case 2.1: [fullText] has few texts with many lines break (still need append read more)
     * case 2.2: [fullText] is really shorter than [readMoreLimit] -> no read more needed
     */
    private fun getShortSpannable(
        fullText: String,
        readMoreLimit: Int
    ): Spanned {
        return when {
            readMoreLimit == NO_LIMIT -> {
                SpannableString(fullText)
            }
            readMoreLimit >= fullText.length -> {
                //in my case I will append read more if there are more than 3 break lines
                if (fullText.count { it.toString() == BREAK_LINE } > 3) {
                    //case 2.1: append read more at fourth line break
                    var linesBreakFound = 0
                    val indexOfFourthBreakLine = fullText.indexOfFirst {
                        if (it.toString() == BREAK_LINE) {
                            linesBreakFound++
                        }
                        linesBreakFound == 4
                    }
                    if (indexOfFourthBreakLine > 0) {
                        SpannableString(fullText.substring(0, indexOfFourthBreakLine)).toSpanned()
                    } else {
                        //if can't find the fourth break line for some reasons (should never occur)
                        SpannableString(fullText)
                    }
                } else {
                    //case 2.2
                    SpannableString(fullText)
                }
            }
            else -> {
                SpannableString(fullText.substring(0, readMoreLimit))
            }
        }
    }

    fun toggleCollapse(useAnimation: Boolean = false) {
        if (isCollapsable()) {
            if (useAnimation) {
                val parentLayout = parent as? ViewGroup ?: return
                TransitionManager.beginDelayedTransition(parentLayout)
            }

            val text = if (isFullTextShow) {
                shortText
            } else {
                fullText
            }

            setText(text, BufferType.SPANNABLE)
            scrollTo(0, 0)

            isFullTextShow = !isFullTextShow
        }
    }

    private fun isCollapsable() = shortText != fullText

    //clear running animation
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        val parentLayout = parent as? ViewGroup ?: return
        TransitionManager.endTransitions(parentLayout)
    }

    companion object {
        private const val NO_LIMIT = -1
        private const val DEFAULT_READ_MORE_TEXT = "Read more..."

        private const val SPACE = " "
        private const val BREAK_LINE = "\n"
    }

}