package com.camscanner.app.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

import com.camscanner.app.storage.DocumentRepository

/**
 * PUBLIC_INTERFACE
 * SyncWorker
 * Periodically syncs local documents to cloud backend (placeholder implementation).
 */
class SyncWorker(ctx: Context, params: WorkerParameters): CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val token = SyncManager.accessToken(applicationContext) ?: return Result.success()
        val repo = DocumentRepository.getInstance(applicationContext)
        val docs = repo.getAllDocuments()
        // TODO: Push docs/previews to BuildConfig.API_BASE_URL using token and CLOUD_BUCKET
        // For now, just simulate success.
        return Result.success()
    }
}
