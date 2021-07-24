package m.tech.customview.circular_progress_button

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Property
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.withStyledAttributes
import m.tech.customview.R

/**
 * @author minhta
 * @since 24/07/2021
 * A image button with a border progress around it
 * Like download file attachments in Gapo App
 */
class CircularProgressButton
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageButton(context, attrs, defStyleAttr) {

    private var progressColor = DEFAULT_PROGRESS_COLOR
    private var cancelColor = DEFAULT_CANCEL_COLOR
    private var backgroundDrawableRes = NULL_BACKGROUND_DRAWABLE_RES
    private var borderWidth: Float = 1f

    private var isShowProgress = false

    private val rectF = RectF()

    private var currentSweepAngle = 0f
    private val progressPaint = Paint()
    private val cancelPaint = Paint()

    /**
     * Provide a custom property so that the progress will auto update when object animator value changes
     * You can use [ValueAnimator] instead but will need update view in UpdateListener
     */
    private val sweepProperty =
        object : Property<CircularProgressButton, Float>(Float::class.java, "what_ever_name") {
            override fun get(circularProgressButton: CircularProgressButton): Float {
                return circularProgressButton.getCurrentSweep()
            }

            override fun set(circularProgressButton: CircularProgressButton?, value: Float) {
                circularProgressButton?.setCurrentSweep(value)
            }
        }
    private var objectAnimator: ObjectAnimator? = null

    init {
        context.withStyledAttributes(attrs, R.styleable.CircularProgressButton) {
            progressColor =
                getColor(R.styleable.CircularProgressButton_progressColor, DEFAULT_PROGRESS_COLOR)
            cancelColor =
                getColor(R.styleable.CircularProgressButton_cancelColor, DEFAULT_CANCEL_COLOR)
            backgroundDrawableRes = getResourceId(
                R.styleable.CircularProgressButton_backgroundDrawableRes,
                NULL_BACKGROUND_DRAWABLE_RES
            )
            borderWidth =
                getDimensionPixelSize(
                    R.styleable.CircularProgressButton_borderWidth,
                    DEFAULT_BORDER_WIDTH
                ).toFloat()
        }

        progressPaint.apply {
            isAntiAlias = true
            color = progressColor
            strokeWidth = borderWidth
            style = Paint.Style.STROKE
        }
        cancelPaint.apply {
            isAntiAlias = true
            color = cancelColor
            strokeWidth = borderWidth
            style = Paint.Style.STROKE
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        /** only draw progress if [isShowProgress] == true */
        if (!isShowProgress) return

        canvas?.apply {
            /**
             * draw progress
             * start angle 270 == start at 12h clock
             */
            rectF.left = borderWidth / 2
            rectF.right = width.toFloat() - borderWidth / 2
            rectF.top = borderWidth / 2
            rectF.bottom = height.toFloat() - borderWidth / 2
            drawArc(rectF, 270f, currentSweepAngle, false, progressPaint)

            /**
             * draw cancel "X" button with size: width = height = [getWidth] / 3
             * you can customize this by draw a bitmap or whatever you want
             */
            val cancelButtonSize = width / 3
            drawLine(
                width / 2f - cancelButtonSize / 2,
                height / 2f - cancelButtonSize / 2,
                width / 2f + cancelButtonSize / 2,
                height / 2f + cancelButtonSize / 2,
                cancelPaint
            )
            drawLine(
                width / 2f + cancelButtonSize / 2,
                height / 2f - cancelButtonSize / 2,
                width / 2f - cancelButtonSize / 2,
                height / 2f + cancelButtonSize / 2,
                cancelPaint
            )

        }
    }

    fun showProgress(isShow: Boolean) {
        if (isShow == isShowProgress) return

        isShowProgress = isShow
        if (isShow) {
            setBackgroundResource(backgroundDrawableRes)
            setImageDrawable(null)
        } else {
            setBackgroundResource(NULL_BACKGROUND_DRAWABLE_RES)
        }
    }

    fun setPercent(percent: Int) {
        val targetAngle = percent * ONE_PERCENT_IN_ANGLE
        stopSweepAnim()
        startSweepAnim(targetAngle)
    }

    private fun startSweepAnim(targetAngle: Float) {
        ObjectAnimator.ofFloat(this, sweepProperty, targetAngle).apply {
            objectAnimator = this

            //Interpolators https://developer.android.com/guide/topics/graphics/prop-animation#api-overview
            interpolator = AccelerateDecelerateInterpolator()
            duration = DEFAULT_ANIMATOR_DURATION

            start()
        }
    }

    fun cancel() {
        if (isRunning()) {
            stopSweepAnim()
            showProgress(false)
            currentSweepAngle = 0f
        }
    }

    private fun stopSweepAnim() {
        objectAnimator?.cancel()
        objectAnimator = null
    }

    private fun isRunning(): Boolean = isShowProgress

    /** setter use for [sweepProperty] */
    private fun setCurrentSweep(sweepAngle: Float) {
        this.currentSweepAngle = sweepAngle
        invalidate()
    }

    /** getter use for [sweepProperty] */
    private fun getCurrentSweep() = currentSweepAngle

    companion object {
        private const val NULL_BACKGROUND_DRAWABLE_RES = 0
        private const val DEFAULT_BORDER_WIDTH = 1
        private const val DEFAULT_PROGRESS_COLOR = Color.WHITE
        private const val DEFAULT_CANCEL_COLOR = Color.WHITE
        private const val DEFAULT_ANIMATOR_DURATION = 1000L

        /**
         * angle: 0 -> 360
         * percent: 0 -> 100
         * 1 percent = 360 / 100 angle
         */
        private const val ONE_PERCENT_IN_ANGLE = 3.6f
    }

}
