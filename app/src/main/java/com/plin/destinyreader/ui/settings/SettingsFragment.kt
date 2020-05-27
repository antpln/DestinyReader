package com.plin.destinyreader.ui.settings

import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceFragmentCompat
import com.plin.destinyreader.R

class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        Log.i("destinyreader", "onCreatePreferences called")
        addPreferencesFromResource(R.xml.settings)
    }


}
