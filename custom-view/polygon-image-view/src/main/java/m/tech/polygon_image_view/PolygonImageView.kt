package m.tech.polygon_image_view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

/**
 * @author 89hnim
 * @since 08/08/2021
 * A bitmap wrapped in a polygon
 * You can change the drawable [R.drawable.ic_polygon] with any other shapes you want to wrap the bitmap.
 */
internal class PolygonImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var destinationBitmap: Bitmap? = null
    private val destinationRect = Rect()

    private var polygonBitmap: Bitmap? = null
    private val polygonRect = Rect()

    private val paint = Paint().apply { isAntiAlias = true }
    private val porterDuffModeDstIn = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

    init {
        //https://stackoverflow.com/questions/18387814/drawing-on-canvas-porterduff-mode-clear-draws-black-why
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (destinationBitmap == null || polygonBitmap == null) return

        //don't need dst rect b/c destinationRect already override bitmap size
        canvas?.drawBitmap(destinationBitmap!!, destinationRect, destinationRect, paint)

        //https://developer.android.com/reference/android/graphics/PorterDuff.Mode
        paint.xfermode = porterDuffModeDstIn

        canvas?.drawBitmap(polygonBitmap!!, polygonRect, polygonRect, paint)
    }

    /**
     * set bitmap for the polygon view
     * @param url drawable res of bitmap, you could override this with file, uri, string, drawable .etc.
     *             anything glide supports
     * @param width width in dp in xml
     * @param height height in dp in xml
     */
    fun setBitmap(@DrawableRes url: Int, width: Int, height: Int) {
        setPolygonSize(width, height)

        Glide.with(this).asBitmap().load(url)
            .apply(RequestOptions().override(width, height))
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    destinationBitmap = resource
                    destinationRect.set(0, 0, width, height)
                    invalidate()
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }

    /**
     * generate polygon bitmap with desire width, height
     * @param width width in dp in xml
     * @param height height in dp in xml
     */
    private fun setPolygonSize(width: Int, height: Int) {
        polygonBitmap =
            ContextCompat.getDrawable(context, R.drawable.ic_polygon)?.toBitmap(width, height)
        polygonRect.set(0, 0, width, height)
    }

    //clear resources
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        destinationBitmap = null
        polygonBitmap = null
    }

}
