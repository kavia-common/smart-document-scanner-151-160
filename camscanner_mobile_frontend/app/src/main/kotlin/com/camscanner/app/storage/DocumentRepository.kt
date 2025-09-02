package com.camscanner.app.storage

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import com.camscanner.app.storage.entities.Document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * PUBLIC_INTERFACE
 * DocumentRepository
 * Encapsulates CRUD operations for documents using Room.
 */
class DocumentRepository private constructor(context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val docDao = db.documentDao()
    private val appContext = context.applicationContext

    companion object {
        @Volatile private var INSTANCE: DocumentRepository? = null

        // PUBLIC_INTERFACE
        fun getInstance(context: Context): DocumentRepository {
            /**
             * Returns singleton repository instance.
             */
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DocumentRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    // PUBLIC_INTERFACE
    suspend fun createDocumentFromBitmap(title: String, bitmap: Bitmap): Long = withContext(Dispatchers.IO) {
        val previewPath = saveBitmap(title, bitmap)
        val id = docDao.insert(Document(title = title, previewPath = previewPath))
        id
    }

    // PUBLIC_INTERFACE
    suspend fun getAllDocuments(): List<Document> = withContext(Dispatchers.IO) {
        docDao.getAll()
    }

    // PUBLIC_INTERFACE
    suspend fun searchDocuments(query: String): List<Document> = withContext(Dispatchers.IO) {
        docDao.search(query)
    }

    private fun saveBitmap(title: String, bitmap: Bitmap): String {
        val dir = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: appContext.filesDir
        val safeName = title.replace(Regex("[^a-zA-Z0-9_-]"), "_")
        val file = File(dir, "${safeName}_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
        }
        return file.absolutePath
    }
}
