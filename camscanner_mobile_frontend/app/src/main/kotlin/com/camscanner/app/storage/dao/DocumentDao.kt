package com.camscanner.app.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.camscanner.app.storage.entities.Document

/**
 * PUBLIC_INTERFACE
 * DocumentDao
 * Room DAO for documents.
 */
@Dao
interface DocumentDao {
    // PUBLIC_INTERFACE
    @Insert
    suspend fun insert(doc: Document): Long

    // PUBLIC_INTERFACE
    @Query("SELECT * FROM documents ORDER BY createdAt DESC")
    suspend fun getAll(): List<Document>

    // PUBLIC_INTERFACE
    @Query("SELECT * FROM documents WHERE title LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    suspend fun search(query: String): List<Document>
}
