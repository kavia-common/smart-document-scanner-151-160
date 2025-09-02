package com.camscanner.app.util

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.camscanner.app.storage.entities.Document
import com.itextpdf.text.Document as ITextDocument
import com.itextpdf.text.pdf.PdfCopy
import com.itextpdf.text.pdf.PdfReader
import java.io.File
import java.io.FileOutputStream

/**
 * PUBLIC_INTERFACE
 * PdfExporter
 * Exports selected documents as PDFs or merges them into a single PDF.
 */
object PdfExporter {
    // PUBLIC_INTERFACE
    fun exportAsPdf(context: Context, documents: List<Document>) {
        /**
         * Generates one PDF per document with its first page preview image.
         */
        documents.forEach { doc ->
            val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
            val outFile = File(outputDir, "${doc.title}-${doc.id}.pdf")
            val bmp = doc.previewPath?.let { BitmapFactory.decodeFile(it) } ?: return@forEach

            val pdf = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(bmp.width, bmp.height, 1).create()
            val page = pdf.startPage(pageInfo)
            val canvas: Canvas = page.canvas
            canvas.drawBitmap(bmp, 0f, 0f, null)
            pdf.finishPage(page)
            FileOutputStream(outFile).use { fos -> pdf.writeTo(fos) }
            pdf.close()
        }
    }

    // PUBLIC_INTERFACE
    fun mergeIntoSinglePdf(context: Context, documents: List<Document>) {
        /**
         * Merges generated PDFs (one per document) into a single merged.pdf file.
         */
        val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
        val sources = documents.mapNotNull { d ->
            val f = File(outputDir, "${d.title}-${d.id}.pdf")
            if (f.exists()) f else null
        }
        if (sources.isEmpty()) return

        val merged = File(outputDir, "merged.pdf")
        val itextDoc = ITextDocument()
        val copy = PdfCopy(itextDoc, FileOutputStream(merged))
        itextDoc.open()
        sources.forEach { file ->
            val reader = PdfReader(file.absolutePath)
            val n = reader.numberOfPages
            for (i in 1..n) {
                val page = copy.getImportedPage(reader, i)
                copy.addPage(page)
            }
            reader.close()
        }
        itextDoc.close()
    }
}
