package m.tech.calendar_view.view

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import androidx.core.content.ContextCompat
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.MonthView
import m.tech.calendar_view.utils.CalendarUtils
import m.tech.calendar_view.utils.CalendarUtils.SHOW_ALL
import m.tech.calendar_view.utils.CalendarUtils.SHOW_DOT
import m.tech.calendar_view.utils.CalendarUtils.SHOW_HEART
import m.tech.calendar_view.utils.CalendarUtils.SHOW_HEART_AND_DOT
import m.tech.calendar_view.utils.CalendarUtils.SHOW_HEART_AND_NOTE
import m.tech.calendar_view.utils.CalendarUtils.SHOW_NOTE
import m.tech.calendar_view.utils.CalendarUtils.SHOW_NOTE_AND_DOT
import m.tech.calendar_view.utils.CalendarUtils.dotBitmap
import m.tech.calendar_view.utils.CalendarUtils.getDayType
import m.tech.calendar_view.utils.CalendarUtils.heartBitmap
import m.tech.calendar_view.utils.CalendarUtils.noteBitmap
import m.tech.calendar_view.R
import m.tech.calendar_view.utils.Utils.fromSpToPx
import m.tech.calendar_view.utils.Utils.toPx

/**
 * @author 89hnim
 * @since 21/08/2021
 */
internal class CustomMonthView(context: Context) : MonthView(context) {

    private var radius = 0f
    private var cornerCircleRadius = 0f
    private var selectedCircleRadius = 0f
    private val distanceBetweenBitmap = 1.toPx

    private val ovulationCircleColor = ContextCompat.getColor(context, R.color.ovulation_circle)
    private val ovulationDashColor = ContextCompat.getColor(context, R.color.ovulation_dash)
    private val ovulationCornerColor = ContextCompat.getColor(context, R.color.ovulation_corner)
    private val periodCircleColor = ContextCompat.getColor(context, R.color.period_circle)
    private val periodDashColor = ContextCompat.getColor(context, R.color.period_dash)
    private val periodCornerColor = ContextCompat.getColor(context, R.color.period_corner)
    private val easyPregnantColor = ContextCompat.getColor(context, R.color.easy_pregnant)

    private val dashEffect = DashPathEffect(floatArrayOf(4.toPx, 2.toPx), 0.toPx)

    private val textPaint = TextPaint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }

    private val cornerTextPaint = TextPaint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 12.fromSpToPx
    }

    private val cornerCirclePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val selectedCirclePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.gray_opa)
    }

    private val dashCirclePaint = Paint().apply {
        isAntiAlias = true
        strokeWidth = 1.toPx
    }

    override fun onPreviewHook() {
        radius = mItemWidth.coerceAtMost(mItemHeight) / 5f * 2f
        cornerCircleRadius = radius / 2.2f
        selectedCircleRadius = radius * 1.2f
        mSelectTextPaint.color = Color.WHITE
        mSchemeTextPaint.textSize = 11.fromSpToPx //use for draw small corner text
        mCurDayTextPaint.textSize = 16.fromSpToPx
    }

    /**
     * draw select calendar
     *
     * @param canvas    canvas
     * @param calendar  select calendar
     * @param x         calendar item x start point
     * @param hasScheme is calendar has scheme?
     * @return if return true will call onDrawScheme again
     */
    override fun onDrawSelected(
        canvas: Canvas?,
        calendar: Calendar?,
        x: Int,
        y: Int,
        hasScheme: Boolean
    ): Boolean {
        return false
    }

    /**
     * draw scheme
     * @param canvas
     * @param calendar
     * @param x
     */
    override fun onDrawScheme(canvas: Canvas, calendar: Calendar, x: Int, y: Int) {
    }

    /**
     * draw text
     *
     * @param canvas     canvas
     * @param calendar   calendar
     * @param x          calendar item x start point
     * @param hasScheme  is calendar has scheme?
     * @param isSelected is calendar selected?
     */
    override fun onDrawText(
        canvas: Canvas,
        calendar: Calendar,
        x: Int,
        y: Int,
        hasScheme: Boolean,
        isSelected: Boolean
    ) {
        if (!calendar.isCurrentMonth) return
        val result = getDayType(calendar)
        val dayType = result.first
        val dayRange = result.second
        when (dayType) {
            CalendarUtils.DAY_TYPE.PERIOD -> {
                drawDashCircle(canvas, x, y, periodDashColor, periodCircleColor)
                drawText(canvas, x, y, calendar.day.toString(), Color.BLACK, calendar.isCurrentDay)
                drawCornerText(canvas, x, y, dayRange, true, periodCornerColor)
                if (isSelected) drawSelectedCircle(canvas, x, y)
            }
            CalendarUtils.DAY_TYPE.EASY_PREGNANT -> {
                drawText(
                    canvas,
                    x,
                    y,
                    calendar.day.toString(),
                    easyPregnantColor,
                    calendar.isCurrentDay
                )
                drawCornerText(canvas, x, y, dayRange, false, 0)
                if (isSelected) drawSelectedCircle(canvas, x, y)
            }
            CalendarUtils.DAY_TYPE.OVULATION -> {
                drawDashCircle(canvas, x, y, ovulationDashColor, ovulationCircleColor)
                drawText(canvas, x, y, calendar.day.toString(), Color.BLACK, calendar.isCurrentDay)
                drawCornerText(canvas, x, y, dayRange, true, ovulationCornerColor)
                if (isSelected) drawSelectedCircle(canvas, x, y)
            }
            else -> {
                drawText(canvas, x, y, calendar.day.toString(), Color.BLACK, calendar.isCurrentDay)
                drawCornerText(canvas, x, y, dayRange, false, 0)
                if (isSelected) drawSelectedCircle(canvas, x, y)
            }
        }
        if (hasScheme) {
            drawBottomScheme(canvas, x, y, calendar.scheme)
        }
    }

    private fun drawBottomScheme(canvas: Canvas, x: Int, y: Int, scheme: String) {
        val baselineY = mTextBaseLine + y + 2.toPx
        val cx = x + mItemWidth / 2f

        when (scheme) {
            SHOW_ALL -> drawThreeIcon(
                canvas,
                cx,
                baselineY,
                heartBitmap(context),
                noteBitmap(context),
                dotBitmap(context)
            )
            SHOW_HEART -> drawOneIcon(canvas, cx, baselineY, heartBitmap(context))
            SHOW_NOTE -> drawOneIcon(canvas, cx, baselineY, noteBitmap(context))
            SHOW_DOT -> drawOneIcon(canvas, cx, baselineY, dotBitmap(context))
            SHOW_HEART_AND_NOTE -> drawTwoIcon(
                canvas,
                cx,
                baselineY,
                heartBitmap(context),
                noteBitmap(context)
            )
            SHOW_HEART_AND_DOT -> drawTwoIcon(
                canvas,
                cx,
                baselineY,
                heartBitmap(context),
                dotBitmap(context)
            )
            SHOW_NOTE_AND_DOT -> drawTwoIcon(
                canvas,
                cx,
                baselineY,
                noteBitmap(context),
                dotBitmap(context)
            )
        }
    }

    private fun drawOneIcon(canvas: Canvas, cx: Float, y: Float, bitmap: Bitmap?) {
        if (bitmap == null) return
        canvas.drawBitmap(bitmap, cx - bitmap.width / 2, y, null)
    }

    private fun drawTwoIcon(
        canvas: Canvas,
        cx: Float,
        y: Float,
        bitmap: Bitmap?,
        bitmap2: Bitmap?
    ) {
        if (bitmap == null || bitmap2 == null) return

        canvas.drawBitmap(bitmap, cx - bitmap.width - distanceBetweenBitmap / 2, y, null)
        canvas.drawBitmap(bitmap2, cx + distanceBetweenBitmap + 2, y, null)
    }

    private fun drawThreeIcon(
        canvas: Canvas,
        cx: Float,
        y: Float,
        bitmap: Bitmap?,
        bitmap2: Bitmap?,
        bitmap3: Bitmap?
    ) {
        if (bitmap == null || bitmap2 == null || bitmap3 == null) return
        val centerX = cx - bitmap2.width / 2
        canvas.drawBitmap(bitmap2, centerX, y, null)
        canvas.drawBitmap(
            bitmap,
            centerX - bitmap2.width / 2 - bitmap.width - distanceBetweenBitmap / 2,
            y,
            null
        )
        canvas.drawBitmap(
            bitmap3,
            centerX + bitmap2.width / 2 + bitmap.width + distanceBetweenBitmap / 2,
            y,
            null
        )
    }

    //text paint
    private fun drawText(
        canvas: Canvas,
        x: Int,
        y: Int,
        text: String,
        textColor: Int,
        isToday: Boolean
    ) {
        textPaint.apply {
            color = textColor
            textSize = if (isToday) 18.fromSpToPx else 16.fromSpToPx
            isFakeBoldText = isToday
        }

        val baselineY = mTextBaseLine + y
        val cx = x + mItemWidth / 2
        canvas.drawText(text, cx.toFloat(), baselineY, textPaint)
    }

    //corner text paint, corner circle paint
    private fun drawCornerText(
        canvas: Canvas,
        x: Int,
        y: Int,
        text: String,
        isDrawCircle: Boolean,
        circleColor: Int
    ) {
        val cx = x + mItemWidth / 2 - radius / 2 - getSmallTextWidth(text) / 2
        val cy = y + mItemHeight / 2 - radius / 1.2

        if (isDrawCircle) {
            cornerCirclePaint.color = circleColor
            cornerTextPaint.color = Color.WHITE
            canvas.drawCircle(
                cx,
                cy.toFloat(),
                cornerCircleRadius,
                cornerCirclePaint
            )
        } else {
            cornerTextPaint.color = Color.GRAY
        }

        canvas.drawText(
            text,
            cx,
            cy.toFloat() + cornerCircleRadius / 2f,
            cornerTextPaint
        )
    }

    //selected circle paint
    private fun drawSelectedCircle(canvas: Canvas, x: Int, y: Int) {
        val cx = x + mItemWidth / 2
        val cy = y + mItemHeight / 2
        canvas.drawCircle(cx.toFloat(), cy.toFloat(), selectedCircleRadius, selectedCirclePaint)
    }

    //dash circle paint
    private fun drawDashCircle(canvas: Canvas, x: Int, y: Int, dashColor: Int, fillColor: Int) {
        val cx = x + mItemWidth / 2
        val cy = y + mItemHeight / 2

        dashCirclePaint.apply {
            color = fillColor
            pathEffect = null
            style = Paint.Style.FILL
        }
        canvas.drawCircle(cx.toFloat(), cy.toFloat(), radius, dashCirclePaint)

        dashCirclePaint.apply {
            color = dashColor
            pathEffect = dashEffect
            style = Paint.Style.STROKE
        }
        canvas.drawCircle(cx.toFloat(), cy.toFloat(), radius, dashCirclePaint)
    }

    private fun getSmallTextWidth(text: String): Float {
        return mSchemeTextPaint.measureText(text)
    }

}