package com.camscanner.app

/**
 * PUBLIC_INTERFACE
 * Config
 * Provides runtime configuration constants. In a full Gradle setup these would come from BuildConfig fields.
 */
object Config {
    /** Base URL for backend API (used by Sync). */
    const val API_BASE_URL: String = "https://api.example.com"
    /** OAuth Client ID for AppAuth. */
    const val OAUTH_CLIENT_ID: String = ""
    /** OAuth Redirect URI. */
    const val OAUTH_REDIRECT_URI: String = "your.app://oauth-redirect"
    /** Cloud storage bucket name/path. */
    const val CLOUD_BUCKET: String = "scans"
}
