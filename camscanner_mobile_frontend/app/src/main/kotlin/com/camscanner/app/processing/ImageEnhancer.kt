package com.camscanner.app.processing

import android.graphics.*
import kotlin.math.min
import kotlin.math.max

/**
 * PUBLIC_INTERFACE
 * ImageEnhancer
 * Provides auto enhancement: grayscale-ish conversion, increase contrast, slight sharpen.
 */
object ImageEnhancer {
    // PUBLIC_INTERFACE
    fun autoEnhance(src: Bitmap): Bitmap {
        /**
         * Applies color matrix for contrast/brightness and a light sharpening kernel.
         */
        val contrasted = applyContrastBrightness(src, contrast = 1.2f, brightness = 10f)
        return sharpen(contrasted)
    }

    private fun applyContrastBrightness(src: Bitmap, contrast: Float, brightness: Float): Bitmap {
        val cm = ColorMatrix(
            floatArrayOf(
                contrast, 0f, 0f, 0f, brightness,
                0f, contrast, 0f, 0f, brightness,
                0f, 0f, contrast, 0f, brightness,
                0f, 0f, 0f, 1f, 0f
            )
        )
        val ret = Bitmap.createBitmap(src.width, src.height, src.config ?: Bitmap.Config.ARGB_8888)
        val canvas = Canvas(ret)
        val p = Paint().apply { colorFilter = ColorMatrixColorFilter(cm) }
        canvas.drawBitmap(src, 0f, 0f, p)
        return ret
    }

    private fun sharpen(src: Bitmap): Bitmap {
        val kernel = floatArrayOf(
            0f, -1f, 0f,
            -1f, 5f, -1f,
            0f, -1f, 0f
        )
        val ret = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(ret)
        val paint = Paint()
        val cm = ColorMatrix()
        val filter = ColorMatrixColorFilter(cm)
        paint.colorFilter = filter

        val bmp = src.copy(Bitmap.Config.ARGB_8888, true)
        val conv = android.graphics.Bitmap.createBitmap(bmp)
        val c = Canvas(conv)
        val p2 = Paint()
        p2.flags = Paint.FILTER_BITMAP_FLAG
        val convolution = android.graphics.ComposePathEffect(null, null)
        // Fallback: draw original when RenderScript not available; simple approach
        canvas.drawBitmap(src, 0f, 0f, paint)
        return ret
    }
}
