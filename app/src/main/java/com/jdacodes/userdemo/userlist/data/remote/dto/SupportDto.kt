package com.jdacodes.userdemo.userlist.data.remote.dto

import com.jdacodes.userdemo.userlist.domain.model.Support

data class SupportDto(
    val text: String,
    val url: String
) {
    fun toSupport(): Support {
        return Support(
            text = text,
            url = url
        )
    }
}
