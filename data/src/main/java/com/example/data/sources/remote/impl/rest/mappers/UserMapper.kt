package com.example.data.sources.remote.impl.rest.mappers

import com.example.data.sources.remote.impl.rest.data.UserData
import com.example.domain.entities.Gender
import com.example.domain.entities.User
import javax.inject.Inject

class UserMapper @Inject constructor() {

    fun userDataToUser(userData: UserData): User = User(
        userName = "${userData.name} ${userData.lastName}",
        userId = userData.id,
        userGender = if (userData.gender == "male") Gender.MALE else if (userData.gender == "female") Gender.FEMALE else Gender.UNDEFINED,
        userAge = userData.age
    )
}