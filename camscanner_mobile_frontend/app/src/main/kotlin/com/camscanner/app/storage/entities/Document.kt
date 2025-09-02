package com.camscanner.app.storage.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * PUBLIC_INTERFACE
 * Document entity
 * Represents a scanned document.
 */
@Entity(tableName = "documents")
data class Document(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val previewPath: String?,
    val createdAt: Date = Date()
)
