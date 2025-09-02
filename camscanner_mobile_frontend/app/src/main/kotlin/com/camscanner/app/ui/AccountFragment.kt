package com.camscanner.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.camscanner.app.Config
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import com.camscanner.app.R
import com.camscanner.app.sync.SyncManager

/**
 * PUBLIC_INTERFACE
 * AccountFragment
 * Shows authentication status and allows sign-in/out and toggling cloud sync.
 */
class AccountFragment: Fragment() {
    private var txtStatus: TextView? = null
    private var txtApiBase: TextView? = null
    private var txtCloudBucket: TextView? = null
    private var btnSignin: Button? = null
    private var btnSignout: Button? = null
    private var switchSync: Switch? = null

    companion object {
        // PUBLIC_INTERFACE
        fun newInstance(): AccountFragment = AccountFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        txtStatus = view.findViewById(R.id.txtStatus)
        txtApiBase = view.findViewById(R.id.txtApiBase)
        txtCloudBucket = view.findViewById(R.id.txtCloudBucket)
        btnSignin = view.findViewById(R.id.btnSignin)
        btnSignout = view.findViewById(R.id.btnSignout)
        switchSync = view.findViewById(R.id.switchSync)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        txtApiBase?.text = Config.API_BASE_URL
        txtCloudBucket?.text = Config.CLOUD_BUCKET

        btnSignin?.setOnClickListener {
            startActivity(Intent(requireContext(), AuthActivity::class.java))
        }
        btnSignout?.setOnClickListener {
            SyncManager.clearAuth(requireContext())
            txtStatus?.text = "Signed out"
        }
        switchSync?.setOnCheckedChangeListener { _, isChecked ->
            SyncManager.setSyncEnabled(requireContext(), isChecked)
        }

        if (SyncManager.isSignedIn(requireContext())) {
            txtStatus?.text = "Signed in"
        } else {
            txtStatus?.text = "Signed out"
        }
        switchSync?.isChecked = SyncManager.isSyncEnabled(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        txtStatus = null
        txtApiBase = null
        txtCloudBucket = null
        btnSignin = null
        btnSignout = null
        switchSync = null
    }
}
