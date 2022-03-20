package com.example.speed_bar_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.speed_bar_view.Utils.fromSpToPx
import com.example.speed_bar_view.Utils.toPx
import kotlin.math.floor

/**
 * @author 89hnim
 * @since 20/03/2022
 */
internal class SpeedBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var callback: SpeedBarViewCallback? = null

    private val paint = Paint().apply {
        isAntiAlias = true
    }

    private val textPaint = TextPaint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        textSize = 12.fromSpToPx
        color = ContextCompat.getColor(context, R.color.text_color)
    }

    private val textSpeedPaint = TextPaint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
        textSize = 12.fromSpToPx
        color = ContextCompat.getColor(context, R.color.text_color)
    }

    private val circlePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.circle_color_ok)
    }

    private val circleStrokePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.circle_stroke_color)
    }

    // total lines
    private var lines = mutableListOf<String>()
    private var linesNumber = 0
    private var defaultLineIndex = 1

    // space between lines
    private var spaceBetweenLines = 0f

    // line width
    private val lineWidth = resources.getDimension(R.dimen.speed_bar_line_width)

    // line height
    private val normalLineHeight = resources.getDimension(R.dimen.speed_bar_line_height)
    private val boldLineHeight = resources.getDimension(R.dimen.speed_bar_bold_line_height)

    // line color
    private val lineColor = ContextCompat.getColor(context, R.color.speed_bar_line_color)
    private val boldLineColor = ContextCompat.getColor(context, R.color.speed_bar_bold_line_color)

    // indexes of bold line : index - value text
    private val boldLineIndexes = mutableListOf<Int>()

    // calculate text bound
    private val boundOfText = Rect()
    private val spaceBetweenBottomTextAndBoldLine = 8.toPx
    private val spaceBetweenTopTextAndBoldLine = 4.toPx

    // touch circle
    private var circleX: Float = 0f
    private var circleStrokeRadius: Float = 8.toPx
    private var circleRadius: Float = 6.toPx
    private var isMovingCircle = false
    private var currentSpeed: String = ""

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // setup circle start X
        circleX = (defaultLineIndex - 1) * (lineWidth + spaceBetweenLines) + paddingLeft + lineWidth
        getSpeedFromX(circleX)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        // calculate space between two lines
        val totalLinesWidth = linesNumber * lineWidth
        spaceBetweenLines =
            (widthSize - paddingLeft - paddingRight - totalLinesWidth) / (linesNumber - 1)

        /** calculate bottom text height */
        textPaint.getTextBounds("0.1x", 0, "0.1x".length, boundOfText)
        val bottomTextHeight = boundOfText.height() + spaceBetweenBottomTextAndBoldLine

        /** calculate upper text height */
        textSpeedPaint.getTextBounds("0.1x", 0, "0.1x".length, boundOfText)
        val upperTextHeight = boundOfText.height() + spaceBetweenTopTextAndBoldLine

        /** calculate height of view */
        // must * 2 b/c lines will always draw at the center vertical
        // if draw bottom text, then the top will have the same space
        val height =
            boldLineHeight + bottomTextHeight * 2 + upperTextHeight * 2 + paddingTop + paddingBottom

        // ready to serve :D
        setMeasuredDimension(widthSize, height.toInt())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                // make sure click in the bar view
                val startYOfBarView = height / 2 - boldLineHeight / 2
                val endYOfBarView = height / 2 + boldLineHeight / 2
                if (event.y >= startYOfBarView && event.y <= startYOfBarView + endYOfBarView) {
                    actionDown(event.x)
                    isMovingCircle = true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (isMovingCircle) {
                    actionMove(event.x)
                }
            }
            MotionEvent.ACTION_UP -> {
                isMovingCircle = false
            }
        }
        return true
    }

    private fun actionDown(rawX: Float) {
        val boundaryStartX = paddingLeft + lineWidth
        val boundaryEndX =
            (linesNumber - 1) * (lineWidth + spaceBetweenLines) + paddingLeft + lineWidth
        circleX = when {
            rawX < boundaryStartX -> boundaryStartX
            rawX > boundaryEndX -> boundaryEndX
            else -> rawX
        }
        getSpeedFromX(circleX)
        invalidate()
    }

    private fun actionMove(rawX: Float) {
        val boundaryStartX = paddingLeft + lineWidth
        val boundaryEndX =
            (linesNumber - 1) * (lineWidth + spaceBetweenLines) + paddingLeft + lineWidth
        circleX = when {
            rawX < boundaryStartX -> boundaryStartX
            rawX > boundaryEndX -> boundaryEndX
            else -> rawX
        }
        getSpeedFromX(circleX)
        invalidate()
    }

    private fun getSpeedFromX(x: Float) {
        // based on formula calculate startX
        // val x = (? - 1) * (lineWidth + spaceBetweenLines) + paddingLeft + lineWidth
        val linesNumber =
            floor((x - lineWidth - paddingLeft) / (lineWidth + spaceBetweenLines) + 1).toInt()
        val speed = lines[linesNumber - 1]

        if (currentSpeed != speed) {
            callback?.onSpeedChanged(speed)
        }

        currentSpeed = speed
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            paint.strokeWidth = lineWidth

            // draw lines
            for (i in 1 until linesNumber + 1) {
                // i - 1 because start from 1
                val startX = (i - 1) * (lineWidth + spaceBetweenLines) + paddingLeft + lineWidth
                if (isBoldLine(i)) {
                    drawBoldLine(this, startX)
                } else {
                    drawNormalLine(this, startX)
                }
            }

            // draw bottom text
            drawText(this)

            // draw circle
            drawCircle(this)

            // draw current speed
            drawSpeedText(this)
        }
    }

    /**
     * @param lines list lines and its string value
     * @param boldLineIndexes indexes of bold lines. start from 1
     * @param defaultLineIndex automatically move to this line
     * @param callback listener
     */
    fun init(
        lines: List<String>,
        boldLineIndexes: List<Int>,
        defaultLineIndex: Int = 1,
        callback: SpeedBarViewCallback
    ) {
        boldLineIndexes.forEach { boldIndex ->
            if (boldIndex == 0) throw IllegalArgumentException("Bold line index must greater than zero")
            if (boldIndex > lines.size) throw IllegalArgumentException("Bold line index $boldIndex must smaller than total lines ${lines.size}")
        }

        // save default line index
        this.defaultLineIndex = defaultLineIndex

        // save bold lines
        this.boldLineIndexes.clear()
        this.boldLineIndexes.addAll(boldLineIndexes)

        // save lines
        this.lines.clear()
        this.lines.addAll(lines)
        this.linesNumber = lines.size

        // listener
        this.callback = callback

        invalidate()
    }

    private fun drawCircle(canvas: Canvas) {
        // draw 2 circles with different radius to make a border
        canvas.drawCircle(circleX, height / 2f, circleStrokeRadius, circleStrokePaint)
        canvas.drawCircle(circleX, height / 2f, circleRadius, circlePaint)
    }

    private fun drawNormalLine(canvas: Canvas, startX: Float) {
        paint.color = lineColor
        val startY = height / 2 - normalLineHeight / 2
        val stopY = height / 2 + normalLineHeight / 2
        canvas.drawLine(startX, startY, startX, stopY, paint)
    }

    private fun drawBoldLine(canvas: Canvas, startX: Float) {
        paint.color = boldLineColor
        val startY = height / 2 - boldLineHeight / 2
        val stopY = height / 2 + boldLineHeight / 2
        canvas.drawLine(startX, startY, startX, stopY, paint)
    }

    private fun drawText(canvas: Canvas) {
        boldLineIndexes.forEach { index ->
            // b/c index start from 1
            val text = lines[index - 1]
            val startX = (index - 1) * (lineWidth + spaceBetweenLines) + paddingLeft + lineWidth

            textPaint.getTextBounds(text, 0, text.length, boundOfText)
            val textHeight = boundOfText.height()

            canvas.drawText(
                text,
                startX,
                height / 2 + boldLineHeight / 2 + textHeight + spaceBetweenBottomTextAndBoldLine,
                textPaint
            )
        }
    }

    private fun drawSpeedText(canvas: Canvas) {
        val text = currentSpeed
        val startX = circleX

        textSpeedPaint.getTextBounds(text, 0, text.length, boundOfText)
        val textHeight = boundOfText.height()

        canvas.drawText(
            text,
            startX,
            height / 2 - boldLineHeight / 2 - textHeight - spaceBetweenTopTextAndBoldLine,
            textSpeedPaint
        )
    }

    private fun isBoldLine(index: Int) = boldLineIndexes.contains(index)

    interface SpeedBarViewCallback {
        fun onSpeedChanged(speed: String)
    }
}