package com.example.data.sources.remote.impl.rest.mappers

import com.example.data.sources.remote.impl.rest.data.UserImage
import javax.inject.Inject

class MediaMapper @Inject constructor() {
    fun userImageToDomain(userImage: UserImage): String = userImage.message
}