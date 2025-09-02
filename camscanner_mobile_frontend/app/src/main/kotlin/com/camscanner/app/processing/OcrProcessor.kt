package com.camscanner.app.processing

import android.content.Context
import android.graphics.Bitmap
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.TimeUnit

/**
 * PUBLIC_INTERFACE
 * OcrProcessor
 * Wraps ML Kit on-device OCR to extract text from bitmap using TextRecognizerOptions.
 */
object OcrProcessor {
    // PUBLIC_INTERFACE
    fun runOcr(context: Context, bitmap: Bitmap): String {
        /**
         * Performs OCR synchronously via Tasks.await for simplicity.
         */
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val task = recognizer.process(image)
        val result = Tasks.await(task, 10, TimeUnit.SECONDS)
        return result.text ?: ""
    }
}
