package com.example.domain.entities

data class User(
    val userId: String,
    val userName: String,
    val userGender: Gender,
    val userAge: Int,
    val userImage: String? = null
)

enum class Gender(val id: String) {
    MALE("male"),
    FEMALE("female"),
    UNDEFINED("")
}
