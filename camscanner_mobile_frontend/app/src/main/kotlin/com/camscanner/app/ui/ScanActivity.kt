package com.camscanner.app.ui

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Size
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.camera.view.PreviewView
import com.camscanner.app.R
import com.camscanner.app.processing.EdgeDetector
import com.camscanner.app.processing.ImageEnhancer
import com.camscanner.app.processing.OcrProcessor
import com.camscanner.app.storage.DocumentRepository
import com.camscanner.app.util.ImageUtils
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * PUBLIC_INTERFACE
 * ScanActivity
 * Provides camera preview, capture, auto-edge detection and crop, image enhancement, OCR extraction,
 * and saving as a new document. Acts as a modal-like flow from FAB.
 */
class ScanActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var btnCapture: ImageButton
    private lateinit var btnClose: ImageButton
    private lateinit var btnSave: Button
    private lateinit var btnOcr: Button
    private lateinit var btnEnhance: Button
    private lateinit var resultImage: ImageView
    private lateinit var progress: ProgressBar
    private lateinit var resultContainer: View
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private var imageCapture: ImageCapture? = null

    private val repo by lazy { DocumentRepository.getInstance(applicationContext) }
    private val uiScope = CoroutineScope(Dispatchers.Main)

    // PUBLIC_INTERFACE
    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * Sets up CameraX and UI controls for capture and processing.
         */
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        previewView = findViewById(R.id.previewView)
        btnCapture = findViewById(R.id.btnCapture)
        btnClose = findViewById(R.id.btnClose)
        btnSave = findViewById(R.id.btnSave)
        btnOcr = findViewById(R.id.btnOcr)
        btnEnhance = findViewById(R.id.btnEnhance)
        resultImage = findViewById(R.id.resultImage)
        progress = findViewById(R.id.progress)
        resultContainer = findViewById(R.id.resultContainer)

        setupCamera()
        setupActions()
    }

    private fun setupCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val provider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .setTargetResolution(Size(1080, 1920))
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .setTargetResolution(Size(1080, 1920))
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            provider.unbindAll()
            provider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun setupActions() {
        btnCapture.setOnClickListener {
            captureImage()
        }
        btnClose.setOnClickListener { finish() }

        btnSave.setOnClickListener {
            // Save current processed bitmap
            val bmp = (resultImage.tag as? Bitmap)
            if (bmp == null) {
                Toast.makeText(this, "No image to save", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            uiScope.launch {
                val id = repo.createDocumentFromBitmap("Scan ${System.currentTimeMillis()}", bmp)
                Toast.makeText(this@ScanActivity, "Saved document #$id", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        btnOcr.setOnClickListener {
            val bmp = (resultImage.tag as? Bitmap)
            if (bmp == null) {
                Toast.makeText(this, "Capture first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progress.visibility = View.VISIBLE
            uiScope.launch(Dispatchers.IO) {
                val text = OcrProcessor.runOcr(this@ScanActivity, bmp)
                launch(Dispatchers.Main) {
                    progress.visibility = View.GONE
                    findViewById<android.widget.TextView>(R.id.txtOcrResult).text = text
                }
            }
        }

        btnEnhance.setOnClickListener {
            val bmp = (resultImage.tag as? Bitmap) ?: return@setOnClickListener
            progress.visibility = View.VISIBLE
            uiScope.launch(Dispatchers.IO) {
                val enhanced = ImageEnhancer.autoEnhance(bmp)
                launch(Dispatchers.Main) {
                    progress.visibility = View.GONE
                    resultImage.setImageBitmap(enhanced)
                    resultImage.tag = enhanced
                }
            }
        }
    }

    private fun captureImage() {
        val capture = imageCapture ?: return
        capture.takePicture(ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val rotation = image.imageInfo.rotationDegrees.toFloat()
                    val bmp = ImageUtils.imageProxyToBitmap(image)
                    image.close()
                    val matrix = Matrix().apply { postRotate(rotation) }
                    val rotated = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)

                    // Edge detection and crop
                    progress.visibility = View.VISIBLE
                    uiScope.launch(Dispatchers.IO) {
                        val cropped = EdgeDetector.autoCrop(rotated)
                        val enhanced = ImageEnhancer.autoEnhance(cropped)
                        launch(Dispatchers.Main) {
                            progress.visibility = View.GONE
                            resultImage.setImageBitmap(enhanced)
                            resultImage.tag = enhanced
                            resultContainer.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@ScanActivity, "Capture failed: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            }
        )
    }
}
