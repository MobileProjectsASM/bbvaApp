package com.example.data.sources.local.impl.shared_preferences.mappers

import com.example.data.sources.local.impl.shared_preferences.data.UserData
import com.example.domain.entities.Gender
import com.example.domain.entities.User
import javax.inject.Inject

class UserMapper @Inject constructor() {

    fun userToUserData(user: User): UserData = UserData(
        user.userId,
        user.userName,
        user.userGender.id,
        user.userAge
    )

    fun userDataToUser(userData: UserData): User = User(
        userData.id,
        userData.fullName,
        if (userData.gender == Gender.MALE.id) Gender.MALE else if (userData.gender == Gender.FEMALE.id) Gender.FEMALE else Gender.UNDEFINED,
        userData.age
    )
}