package m.tech.polygon_progress_view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.REVERSE
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Property
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import m.tech.polygon_progress_view.Utils.toPx

/**
 * @author 89hnim
 * @since 22/08/2021
 */
internal class PolygonProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /**
     * Provide a custom property so that the progress will auto update when object animator value changes
     * You can use [ValueAnimator] instead but will need update view in UpdateListener
     */
    private val customProperty =
        object : Property<PolygonProgressView, Float>(Float::class.java, "what_ever_name") {
            override fun get(view: PolygonProgressView): Float {
                return view.getCurrentAnimPercent()
            }

            override fun set(view: PolygonProgressView?, value: Float) {
                view?.setCurrentAnimPercent(value)
            }
        }

    private fun getCurrentAnimPercent() = currentAnimPercent
    private fun setCurrentAnimPercent(progress: Float) {
        currentAnimPercent = progress
        invalidate()
    }

    private var objectAnimator: ObjectAnimator? = null

    private val path = Path()
    private val pathAnim = Path()

    private val colors = intArrayOf(
        ContextCompat.getColor(context, R.color.divider_gradient_start_color),
        ContextCompat.getColor(context, R.color.divider_gradient_center_color),
        ContextCompat.getColor(context, R.color.divider_gradient_end_color)
    )

    private val colors2 = intArrayOf(
        ContextCompat.getColor(context, R.color.anim_divider_gradient_start_color),
        ContextCompat.getColor(context, R.color.anim_divider_gradient_center_color),
        ContextCompat.getColor(context, R.color.anim_divider_gradient_end_color)
    )

    private val mainPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
        color = ContextCompat.getColor(context, R.color.main_polygon)
        pathEffect = CornerPathEffect(10.toPx)
    }

    private val borderPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 6.toPx
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        pathEffect = CornerPathEffect(10.toPx)
    }

    private val animationPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 6.toPx
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        pathEffect = CornerPathEffect(10.toPx)
        color = ContextCompat.getColor(context, R.color.anim_color)
    }

    private val animationPaint2 = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 6.toPx
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        pathEffect = CornerPathEffect(10.toPx)
    }

    private val circlePaint = Paint().apply {
        isAntiAlias = true
        strokeWidth = 4.toPx
    }

    private val circleRadius = 6.toPx

    /** circleRadius + circlePaint.strokeWidth */
    private val padding = 10.toPx
    private var progress = 0f //use for drawing border
    private var currentAnimPercent = 0f // use for drawing animation
    private var maxProgress = 0f
    private var closePathX = 0f
    private var closePathY = 0f

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        borderPaint.shader = LinearGradient(
            0f, 0f, 0f, heightSize.toFloat(), colors, null, Shader.TileMode.MIRROR
        )
        animationPaint2.shader = LinearGradient(
            0f, 0f, 0f, heightSize.toFloat(), colors, null, Shader.TileMode.MIRROR
        )
        animationPaint.shader = LinearGradient(
            0f, 0f, 0f, heightSize.toFloat(), colors2, null, Shader.TileMode.MIRROR
        )
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            drawMainPolygon(canvas)
            drawBorderLine(canvas)
            drawAnimationProgress(canvas)
            drawAnimationProgress2(canvas)
            drawCloseCircle(canvas)
        }
    }

    //main
    private fun drawMainPolygon(canvas: Canvas) {
        val width = width.toFloat()
        val height = height.toFloat()
        with(canvas) {
            path.reset()
            path.moveTo(padding, height * 0.25f)
            path.lineTo(width * 0.5f, padding)
            path.lineTo(width - padding, height * 0.25f)
            path.lineTo(width - padding, height * 0.75f)
            path.lineTo(width * 0.5f, height - padding)
            path.lineTo(padding, height * 0.75f)
            path.close()
            drawPath(path, mainPaint)
        }
    }

    /**
     * draw border
     * Depend on current progress draw border for polygon view
     */
    private fun drawBorderLine(canvas: Canvas) {
        val width = width.toFloat()
        val height = height.toFloat()
        val currentPercent = progress / maxProgress * 100

        with(canvas) {
            path.reset()
            path.moveTo(width * 0.5f, padding) //start

            closePathX = width - padding
            closePathY = height * 0.25f
            path.lineTo(closePathX, closePathY)

            if (currentPercent > percentPerEdge) {
                closePathX = width - padding
                closePathY = height * 0.75f
                path.lineTo(closePathX, closePathY)
            }
            if (currentPercent > percentPerEdge * 2 || currentPercent == 50f) {
                closePathX = width * 0.5f
                closePathY = height - padding
                path.lineTo(closePathX, closePathY)
            }
            if (currentPercent > percentPerEdge * 3) {
                closePathX = padding
                closePathY = height * 0.75f
                path.lineTo(closePathX, closePathY)
            }
            if (currentPercent > percentPerEdge * 4) {
                closePathX = padding
                closePathY = height * 0.25f
                path.lineTo(closePathX, closePathY)
            }
            if (currentPercent > percentPerEdge * 5) {
                closePathX = width * 0.5f
                closePathY = padding
                path.close()
            }
            drawPath(path, borderPaint)
        }
    }

    private fun drawCloseCircle(canvas: Canvas) {
        circlePaint.color = Color.WHITE
        circlePaint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawCircle(closePathX, closePathY, circleRadius, circlePaint)
        circlePaint.color = ContextCompat.getColor(context, R.color.circle_color)
        circlePaint.style = Paint.Style.STROKE
        canvas.drawCircle(closePathX, closePathY, circleRadius, circlePaint)
    }

    //animation
    private fun drawAnimationProgress(canvas: Canvas) {
        val width = width.toFloat()
        val height = height.toFloat()
        val currentPercent = currentAnimPercent
        var closePathX: Float
        var closePathY: Float
        with(canvas) {
            pathAnim.reset()
            pathAnim.moveTo(width * 0.5f, padding) //start

            var currentPercentEachEdge = getCurrentPercentInEdge(0, currentPercent)

            var rangeX = width - width * 0.5f - padding
            var rangeY = height * 0.25f - padding

            closePathX = width * 0.5f + rangeX * currentPercentEachEdge / 100
            closePathY = padding + rangeY * currentPercentEachEdge / 100

            pathAnim.lineTo(closePathX, closePathY)
            if (currentPercent > percentPerEdge) {
                currentPercentEachEdge = getCurrentPercentInEdge(1, currentPercent)
                rangeY = height * 0.75f - height * 0.25f - padding
                closePathX = width - padding
                closePathY = 0.25f * height + padding + rangeY * currentPercentEachEdge / 100
                pathAnim.lineTo(closePathX, closePathY)
            }
            if (currentPercent > percentPerEdge * 2 || currentPercent == 50f) {
                currentPercentEachEdge = getCurrentPercentInEdge(2, currentPercent)
                rangeX = width * 0.5f - (width - padding)
                rangeY = height - padding - 0.75f * height
                closePathX = width - padding + rangeX * currentPercentEachEdge / 100
                closePathY = 0.75f * height + rangeY * currentPercentEachEdge / 100
                pathAnim.lineTo(closePathX, closePathY)
            }
            if (currentPercent > percentPerEdge * 3) {
                currentPercentEachEdge = getCurrentPercentInEdge(3, currentPercent)
                rangeX = padding - width * 0.5f
                rangeY = height * 0.75f - (height - padding)
                closePathX = width * 0.5f + rangeX * currentPercentEachEdge / 100
                closePathY = height - padding + rangeY * currentPercentEachEdge / 100
                pathAnim.lineTo(closePathX, closePathY)
            }
            if (currentPercent > percentPerEdge * 4) {
                currentPercentEachEdge = getCurrentPercentInEdge(4, currentPercent)
                rangeY = height * 0.25f - height * 0.75f
                closePathX = padding
                closePathY = height * 0.75f + rangeY * currentPercentEachEdge / 100
                pathAnim.lineTo(closePathX, closePathY)
            }
            if (currentPercent > percentPerEdge * 5) {
                currentPercentEachEdge = getCurrentPercentInEdge(5, currentPercent)
                rangeX = width * 0.5f - padding
                rangeY = padding - height * 0.25f
                closePathX = padding + rangeX * currentPercentEachEdge / 100
                closePathY = height * 0.25f + rangeY * currentPercentEachEdge / 100
                pathAnim.lineTo(closePathX, closePathY)
            }
            drawPath(pathAnim, animationPaint)
        }
    }

    //animation
    private fun drawAnimationProgress2(canvas: Canvas) {
        val width = width.toFloat()
        val height = height.toFloat()
        val currentPercent = currentAnimPercent - 10
        if (currentPercent < 0) return
        var closePathX: Float
        var closePathY: Float
        with(canvas) {
            pathAnim.reset()
            pathAnim.moveTo(width * 0.5f, padding) //start

            var currentPercentEachEdge = getCurrentPercentInEdge(0, currentPercent)

            var rangeX = width - width * 0.5f - padding
            var rangeY = height * 0.25f - padding

            closePathX = width * 0.5f + rangeX * currentPercentEachEdge / 100
            closePathY = padding + rangeY * currentPercentEachEdge / 100

            pathAnim.lineTo(closePathX, closePathY)
            if (currentPercent > percentPerEdge) {
                currentPercentEachEdge = getCurrentPercentInEdge(1, currentPercent)
                rangeY = height * 0.75f - height * 0.25f - padding
                closePathX = width - padding
                closePathY = 0.25f * height + padding + rangeY * currentPercentEachEdge / 100
                pathAnim.lineTo(closePathX, closePathY)
            }
            if (currentPercent > percentPerEdge * 2 || currentPercent == 50f) {
                currentPercentEachEdge = getCurrentPercentInEdge(2, currentPercent)
                rangeX = width * 0.5f - (width - padding)
                rangeY = height - padding - 0.75f * height
                closePathX = width - padding + rangeX * currentPercentEachEdge / 100
                closePathY = 0.75f * height + rangeY * currentPercentEachEdge / 100
                pathAnim.lineTo(closePathX, closePathY)
            }
            if (currentPercent > percentPerEdge * 3) {
                currentPercentEachEdge = getCurrentPercentInEdge(3, currentPercent)
                rangeX = padding - width * 0.5f
                rangeY = height * 0.75f - (height - padding)
                closePathX = width * 0.5f + rangeX * currentPercentEachEdge / 100
                closePathY = height - padding + rangeY * currentPercentEachEdge / 100
                pathAnim.lineTo(closePathX, closePathY)
            }
            if (currentPercent > percentPerEdge * 4) {
                currentPercentEachEdge = getCurrentPercentInEdge(4, currentPercent)
                rangeY = height * 0.25f - height * 0.75f
                closePathX = padding
                closePathY = height * 0.75f + rangeY * currentPercentEachEdge / 100
                pathAnim.lineTo(closePathX, closePathY)
            }
            if (currentPercent > percentPerEdge * 5) {
                currentPercentEachEdge = getCurrentPercentInEdge(5, currentPercent)
                rangeX = width * 0.5f - padding
                rangeY = padding - height * 0.25f
                closePathX = padding + rangeX * currentPercentEachEdge / 100
                closePathY = height * 0.25f + rangeY * currentPercentEachEdge / 100
                pathAnim.lineTo(closePathX, closePathY)
            }
            drawPath(pathAnim, animationPaint2)
        }
    }

    private fun getCurrentPercentInEdge(edgeIndex: Int, currentTotalPercent: Float): Float {
        return ((currentTotalPercent - percentPerEdge * edgeIndex) * 100 / percentPerEdge).let {
            if (it > 100f) 100f else it
        }
    }

    private fun startAnim(targetProgress: Float, animDuration: Long) {
        ObjectAnimator.ofFloat(this, customProperty, targetProgress).apply {
            objectAnimator = this
            repeatMode = REVERSE
            repeatCount = INFINITE

            //Interpolators https://developer.android.com/guide/topics/graphics/prop-animation#api-overview
            interpolator = LinearInterpolator()
            duration = animDuration

            start()
        }
    }

    private fun stopAnim() {
        objectAnimator?.cancel()
        objectAnimator = null
    }

    fun init(maxProgress: Int, progress: Int) {
        stopAnim()
        this.maxProgress = maxProgress.toFloat()
        this.progress = progress.toFloat()
        val (targetProgress, duration) = when (this.progress / this.maxProgress * 100f) {
            in 0f..percentPerEdge -> {
                percentPerEdge to 3000L
            }
            in percentPerEdge..percentPerEdge * 2 -> {
                percentPerEdge * 2 to 4000L
            }
            in percentPerEdge * 2..percentPerEdge * 3 -> {
                percentPerEdge * 3 to 6000L
            }
            in percentPerEdge * 3..percentPerEdge * 4 -> {
                percentPerEdge * 4 to 6000L
            }
            in percentPerEdge * 4..percentPerEdge * 5 -> {
                percentPerEdge * 5 to 6000L
            }
            else -> {
                100f to 7000L
            }
        }
        startAnim(targetProgress, duration)
        invalidate()
    }

    companion object {
        private const val percentPerEdge: Float = 100f / 6f
    }

}
