package com.bible.core

import android.content.Context

private const val PREFERENCE_NAME = "coreModulePreference"

private const val DEVICE_TOKEN = "device_token"
private const val DEVICE_ID = "device_id"

object SessionManager {

    fun saveSession(context: Context, key: String, value: String = "") {
        val sharedPref =
            context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(key, value)
            commit()
        }
    }

    fun saveSessionLatLng(context: Context, key: String, value: Float = 0.0f) {
        val sharedPref =
            context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putFloat(key, value)
            commit()
        }
    }

    fun getSessionLatLng(context: Context, key: String, defValue: Float = 0.0f): Float {
        val sharedPref =
            context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE) ?: return defValue
        return sharedPref.getFloat(key, defValue) ?: defValue
    }


    fun getSession(context: Context, key: String, defValue: String = ""): String {
        val sharedPref =
            context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE) ?: return defValue
        return sharedPref.getString(key, defValue) ?: defValue
    }

    fun saveSession(context: Context, key: String, value: Int) {
        val sharedPref =
            context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(key, value)
            commit()
        }
    }

    fun getSession(context: Context, key: String, defValue: Int = 0): Int {
        val sharedPref =
            context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE) ?: return defValue
        return sharedPref.getInt(key, defValue)
    }

    fun saveSession(context: Context, key: String, value: Boolean) {
        val sharedPref =
            context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putBoolean(key, value)
            commit()
        }
    }

    fun getSession(context: Context, key: String, defValue: Boolean = false): Boolean {
        val sharedPref =
            context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE) ?: return defValue
        return sharedPref.getBoolean(key, defValue)
    }

    fun setDeviceId(context: Context, value: String) {
        val sharedPref =
            context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(DEVICE_ID, value)
            commit()
        }
    }

    fun setDeviceToken(context: Context, value: String) {
        val sharedPref =
            context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(DEVICE_TOKEN, value)
            commit()
        }
    }

    fun getDeviceId(context: Context): String {
        val sharedPref =
            context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE) ?: return ""
        return sharedPref.getString(DEVICE_ID, "") ?: ""
    }

    fun getDeviceToken(context: Context): String {
        val sharedPref =
            context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE) ?: return ""
        return sharedPref.getString(DEVICE_TOKEN, "") ?: ""
    }
}
