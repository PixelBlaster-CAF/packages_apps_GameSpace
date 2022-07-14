package com.blaster.gamespace.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager


object SharedPreferenceUtils {
    private const val PERFORMANCE_MODE_STATUS = "performance_mode_status"
    private const val SESSION_HANDLE = "session_handle"
    private val TAG = SharedPreferenceUtils::class.java.simpleName
    private val isAtLeastN: Boolean
        private get() = Build.VERSION.SDK_INT >= 24

    private fun getDefaultSharedPreferences(context: Context): SharedPreferences {
        val storageContext: Context
        if (isAtLeastN) {
            val name = PreferenceManager.getDefaultSharedPreferencesName(context)
            storageContext = context.createDeviceProtectedStorageContext()
        } else {
            storageContext = context
        }
        return PreferenceManager.getDefaultSharedPreferences(storageContext)
    }

    fun getPerformanceModeStatus(context: Context): Boolean {
        return getDefaultSharedPreferences(context).getBoolean(PERFORMANCE_MODE_STATUS, false)
    }

    fun setPerformanceModeStatus(context: Context, isOn: Boolean) {
        val editor = getDefaultSharedPreferences(context).edit()
        editor.putBoolean(PERFORMANCE_MODE_STATUS, isOn)
        editor.apply()
    }

    fun getSessionHandle(context: Context): Int {
        return getDefaultSharedPreferences(context).getInt(SESSION_HANDLE, -1)
    }

    fun setSessionHandle(context: Context, handle: Int) {
        val editor = getDefaultSharedPreferences(context).edit()
        editor.putInt(SESSION_HANDLE, handle)
        editor.apply()
    }
}