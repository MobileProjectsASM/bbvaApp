package com.example.data.sources.local.impl.shared_preferences

import android.content.SharedPreferences
import com.example.data.sources.local.abstract_locals.AuthLocalSource
import com.example.data.sources.local.impl.shared_preferences.data.UserData
import com.example.data.sources.local.impl.shared_preferences.mappers.UserMapper
import com.example.domain.entities.User
import com.google.gson.Gson
import javax.inject.Inject

class AuthLocalSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val userMapper: UserMapper,
    private val gson: Gson,
): AuthLocalSource {
    companion object {
        const val SESSION_KEY = "session"
    }

    override suspend fun saveSession(user: User) {
        val userData = userMapper.userToUserData(user)
        val session = gson.toJson(userData)
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
        val userData = gson.fromJson(userJson, UserData::class.java)
        val user = userMapper.userDataToUser(userData)
        return user
    }
}