package com.camscanner.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.widget.FrameLayout
import com.camscanner.app.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.camscanner.app.ui.AccountFragment
import com.camscanner.app.ui.DocumentsFragment
import com.camscanner.app.ui.ScanActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * PUBLIC_INTERFACE
 * MainActivity
 * Entry point hosting the bottom navigation and a floating scan button.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var fabScan: FloatingActionButton

    // PUBLIC_INTERFACE
    override fun onCreate(savedInstanceState: Bundle?) {
        /** Sets up bottom nav and FAB for scanning. */
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNav)
        fabScan = findViewById(R.id.fabScan)

        setupBottomNav(bottomNav)
        fabScan.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }

        if (savedInstanceState == null) {
            openFragment(DocumentsFragment.newInstance())
        }
    }

    private fun setupBottomNav(bottomNav: BottomNavigationView) {
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_documents -> openFragment(DocumentsFragment.newInstance())
                R.id.nav_account -> openFragment(AccountFragment.newInstance())
                else -> false
            }
        }
    }

    private fun openFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
        return true
    }
}
