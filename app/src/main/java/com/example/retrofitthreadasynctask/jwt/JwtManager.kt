package com.example.retrofitthreadasynctask.jwt

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

class JwtManager(private val context: Context) {
    private val PREF_NAME = "JwtPrefs"
    private val KEY_TOKEN = "jwt_token"
    private val KEY_EXPIRATION = "jwt_expiration"

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var token: String?
        get() = sharedPreferences.getString(KEY_TOKEN, null)
        set(value) = sharedPreferences.edit().putString(KEY_TOKEN, value).apply()

    var expiration: Date?
        get() {
            val expirationTime = sharedPreferences.getLong(KEY_EXPIRATION, 0)
            if (expirationTime > 0) {
                return Date(expirationTime)
            }
            return null
        }
        set(value) {
            if (value != null) {
                sharedPreferences.edit().putLong(KEY_EXPIRATION, value.time).apply()
            } else {
                sharedPreferences.edit().remove(KEY_EXPIRATION).apply()
            }
        }

    fun isTokenExpired(): Boolean {
        val expirationTime = sharedPreferences.getLong(KEY_EXPIRATION, 0)
        return expirationTime <= System.currentTimeMillis()
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private var instance: JwtManager? = null

        fun getInstance(context: Context): JwtManager {
            if (instance == null) {
                instance = JwtManager(context.applicationContext)
            }
            return instance!!
        }
    }
}