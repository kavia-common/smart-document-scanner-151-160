package com.camscanner.app.sync

import android.content.Context
import android.content.SharedPreferences
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * PUBLIC_INTERFACE
 * SyncManager
 * Manages authentication tokens and background sync enablement.
 */
object SyncManager {
    private const val PREF = "sync_prefs"
    private const val KEY_ACCESS = "access_token"
    private const val KEY_REFRESH = "refresh_token"
    private const val KEY_SYNC_ENABLED = "sync_enabled"
    private const val WM_NAME = "cloud_sync"

    private fun prefs(ctx: Context): SharedPreferences =
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)

    // PUBLIC_INTERFACE
    fun saveAuth(ctx: Context, access: String?, refresh: String?) {
        prefs(ctx).edit().putString(KEY_ACCESS, access).putString(KEY_REFRESH, refresh).apply()
    }

    // PUBLIC_INTERFACE
    fun clearAuth(ctx: Context) {
        prefs(ctx).edit().remove(KEY_ACCESS).remove(KEY_REFRESH).apply()
    }

    // PUBLIC_INTERFACE
    fun isSignedIn(ctx: Context): Boolean = prefs(ctx).getString(KEY_ACCESS, null) != null

    // PUBLIC_INTERFACE
    fun setSyncEnabled(ctx: Context, enabled: Boolean) {
        prefs(ctx).edit().putBoolean(KEY_SYNC_ENABLED, enabled).apply()
        val wm = WorkManager.getInstance(ctx)
        if (enabled) {
            val req = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES).build()
            wm.enqueueUniquePeriodicWork(WM_NAME, ExistingPeriodicWorkPolicy.UPDATE, req)
        } else {
            wm.cancelUniqueWork(WM_NAME)
        }
    }

    // PUBLIC_INTERFACE
    fun isSyncEnabled(ctx: Context): Boolean = prefs(ctx).getBoolean(KEY_SYNC_ENABLED, false)

    // PUBLIC_INTERFACE
    fun accessToken(ctx: Context): String? = prefs(ctx).getString(KEY_ACCESS, null)
}
