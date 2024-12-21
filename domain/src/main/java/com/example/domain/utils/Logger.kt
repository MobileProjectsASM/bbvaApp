package com.example.domain.utils

interface Logger {
    fun logI(location: String, message:String)
    fun logE(location: String, throwable:Throwable)
}