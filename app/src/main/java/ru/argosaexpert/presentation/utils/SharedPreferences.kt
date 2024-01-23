package ru.argosaexpert.presentation.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(context: Context) {
    companion object {
        const val PREFS_NAME = "my_prefs"
    }

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun save(KEY_NAME: String, value: String) {
        sharedPref.let {
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putString(KEY_NAME, value)
            editor.apply()
        }
    }

    fun saveInt(KEY_NAME: String, value: Int) {
        sharedPref.let {
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putInt(KEY_NAME, value)
            editor.apply()
        }
    }

    fun saveBoolean(KEY_NAME: String, value: Boolean) {
        sharedPref.let {
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putBoolean(KEY_NAME, value)
            editor.apply()
        }
    }

    fun saveFloat(KEY_NAME: String, value: Float) {
        sharedPref.let {
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putFloat(KEY_NAME, value)
            editor.apply()
        }
    }

    fun getStringValue(KEY_NAME: String, def: String? = null): String? =
        sharedPref.getString(KEY_NAME, def)


    fun getBooleanValue(KEY_NAME: String, defValue: Boolean = false): Boolean {
        sharedPref.let { return sharedPref.getBoolean(KEY_NAME, defValue) }
    }

    fun getIntValue(KEY_NAME: String, defValue: Int = 0): Int {
        sharedPref.let { return sharedPref.getInt(KEY_NAME, defValue) }
    }

    fun getFloatValue(KEY_NAME: String, defValue: Float = 0f): Float {
        sharedPref.let { return sharedPref.getFloat(KEY_NAME, defValue) }
    }

    fun removeStringValue(KEY_NAME: String) {
        sharedPref.let {
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.remove(KEY_NAME)
            editor.apply()
        }
    }

    fun contains(KEY_NAME: String, value: Boolean) {
        sharedPref.let {
            if (!sharedPref.contains(KEY_NAME)) {
                with(sharedPref.edit()) {
                    putBoolean(KEY_NAME, value)
                    apply()
                }
            }
        }
    }

    fun contains(KEY_NAME: String, value: String) {
        sharedPref.let {
            if (!sharedPref.contains(KEY_NAME)) {
                with(sharedPref.edit()) {
                    putString(KEY_NAME, value)
                    apply()
                }
            }
        }
    }

    fun contains(KEY_NAME: String, value: Int) {
        sharedPref.let {
            if (!sharedPref.contains(KEY_NAME)) {
                with(sharedPref.edit()) {
                    putInt(KEY_NAME, value)
                    apply()
                }
            }
        }
    }

    fun contains(KEY_NAME: String): Boolean {
        sharedPref.let {
            return sharedPref.contains(KEY_NAME)
        }
    }
}