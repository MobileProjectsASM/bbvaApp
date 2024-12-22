package com.example.data.sources.local.impl

import android.content.SharedPreferences
import com.example.data.sources.local.abstract_locals.AuthLocalSource
import com.example.domain.entities.User
import com.google.gson.Gson

class AuthLocalSourceImpl constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
): AuthLocalSource {
    companion object {
        const val SESSION_KEY = "session"
    }

    override suspend fun saveSession(user: User) {
        val session = gson.toJson(user)
        with(sharedPreferences.edit()) {
            putString(SESSION_KEY, session)
            commit()
        }
    }

    override suspend fun isSessionActive(): Boolean = sharedPreferences.contains(SESSION_KEY)

    override suspend fun closeSession() {
        with(sharedPreferences.edit()) {
            remove(SESSION_KEY)
            commit()
        }
    }

    override suspend fun fetchSession(): User {
        val userJson = sharedPreferences.getString(SESSION_KEY, "")
        if (userJson.isNullOrEmpty()) throw Exception("Session not exists")
        val user = gson.fromJson(userJson, User::class.java)
        return user
    }
}