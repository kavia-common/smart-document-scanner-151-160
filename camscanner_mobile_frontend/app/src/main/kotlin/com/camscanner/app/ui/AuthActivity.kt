package com.camscanner.app.ui

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.camscanner.app.R
import com.camscanner.app.sync.SyncManager

/**
 * PUBLIC_INTERFACE
 * AuthActivity (Placeholder)
 * Simulates a successful sign-in to enable testing without external OAuth.
 * Stores a dummy token via SyncManager and finishes.
 */
class AuthActivity: AppCompatActivity() {
    private lateinit var btnStartAuth: Button

    // PUBLIC_INTERFACE
    override fun onCreate(savedInstanceState: Bundle?) {
        /** Simulated auth flow for testing. */
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        btnStartAuth = findViewById(R.id.btnStartAuth)

        btnStartAuth.setOnClickListener {
            // Simulate success
            SyncManager.saveAuth(this, access = "dummy_access_token", refresh = "dummy_refresh_token")
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
