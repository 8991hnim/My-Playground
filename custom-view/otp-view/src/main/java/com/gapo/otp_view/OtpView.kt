package com.gapo.otp_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.InputFilter
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

/**
 * @author 89hnim
 * @since 05/11/2021
 */
internal class OtpView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    // max OTP char = 8
    private val maxChar = 6

    // in my case, I use W / H = 44 / 56
    private val rectWidthHeightRatio = 44f / 56f

    // space between rect
    private val space = context.resources.getDimension(R.dimen.otp_rectangle_space)

    // radius of rect
    private val rectRadius = context.resources.getDimension(R.dimen.otp_rectangle_radius)

    // calculate text bound
    private val boundOfChar = Rect()

    // size of a otp number
    private var rectWidth = 0f
    private var rectHeight = 0f

    private val otpPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.parseColor("#F1F2F4")
    }

    init {
        filters = arrayOf(InputFilter.LengthFilter(maxChar))
        background = null
        isCursorVisible = false
        paint.textAlign = Paint.Align.CENTER
        setOnClickListener {
            // move cursor to end of text when tapped
            setSelection(text.toString().length)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        rectWidth = (widthSize - paddingRight - paddingLeft - space * (maxChar - 1)) / maxChar
        rectHeight = rectWidth / rectWidthHeightRatio

        setMeasuredDimension(widthSize, rectHeight.toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            drawRectangle(it)
            drawText(it)
        }
    }

    private fun drawRectangle(canvas: Canvas) {
        for (i in 0 until maxChar) {
            canvas.drawRoundRect(
                i * (space + rectWidth),
                0f,
                i * (space + rectWidth) + rectWidth,
                rectHeight,
                rectRadius,
                rectRadius,
                otpPaint
            )
        }
    }

    private fun drawText(canvas: Canvas) {
        val text = text.toString()

        text.forEachIndexed { index, char ->
            paint.getTextBounds(char.toString(), 0, 1, boundOfChar)
            val textHeight = boundOfChar.height()

            canvas.drawText(
                char.toString(),
                0,
                1,
                index * (space + rectWidth) + rectWidth / 2,
                rectHeight / 2 + textHeight / 2,
                paint
            )
        }
    }

}