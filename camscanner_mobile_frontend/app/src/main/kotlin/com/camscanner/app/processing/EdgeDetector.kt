package com.camscanner.app.processing

import android.graphics.*

/**
 * PUBLIC_INTERFACE
 * EdgeDetector
 * Provides naive auto-crop by detecting document-like area via thresholding and trimming borders.
 */
object EdgeDetector {
    // PUBLIC_INTERFACE
    fun autoCrop(src: Bitmap): Bitmap {
        /**
         * Simple border trim based on luminance threshold; not perfect but serves as demo.
         */
        val w = src.width
        val h = src.height
        val threshold = 240 // white-ish
        val pixels = IntArray(w * h)
        src.getPixels(pixels, 0, w, 0, 0, w, h)

        var top = 0
        while (top < h) {
            if (!isRowWhite(pixels, w, h, top, threshold)) break
            top++
        }
        var bottom = h - 1
        while (bottom > top) {
            if (!isRowWhite(pixels, w, h, bottom, threshold)) break
            bottom--
        }
        var left = 0
        while (left < w) {
            if (!isColWhite(pixels, w, h, left, threshold)) break
            left++
        }
        var right = w - 1
        while (right > left) {
            if (!isColWhite(pixels, w, h, right, threshold)) break
            right--
        }
        val cropW = (right - left + 1).coerceAtLeast(1)
        val cropH = (bottom - top + 1).coerceAtLeast(1)
        return try {
            Bitmap.createBitmap(src, left, top, cropW, cropH)
        } catch (e: Exception) {
            src
        }
    }

    private fun isRowWhite(pix: IntArray, w: Int, h: Int, row: Int, th: Int): Boolean {
        val start = row * w
        for (i in 0 until w) {
            val c = pix[start + i]
            val r = Color.red(c)
            val g = Color.green(c)
            val b = Color.blue(c)
            val y = (0.299*r + 0.587*g + 0.114*b).toInt()
            if (y < th) return false
        }
        return true
    }

    private fun isColWhite(pix: IntArray, w: Int, h: Int, col: Int, th: Int): Boolean {
        var idx = col
        for (i in 0 until h) {
            val c = pix[idx]
            val r = Color.red(c)
            val g = Color.green(c)
            val b = Color.blue(c)
            val y = (0.299*r + 0.587*g + 0.114*b).toInt()
            if (y < th) return false
            idx += w
        }
        return true
    }
}
