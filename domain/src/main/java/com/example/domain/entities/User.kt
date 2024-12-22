package com.example.domain.entities

data class User(
    val userId: String,
    val userName: String,
    val userGender: Gender,
    val userAge: Int
)

enum class Gender(id: String) {
    MALE("male"),
    FEMALE("female"),
    UNDEFINED("")
}
