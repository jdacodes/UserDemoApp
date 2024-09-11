package com.jdacodes.userdemo.dashboard.domain.model

import com.jdacodes.userdemo.userlist.domain.model.Support

data class SingleColorResponse(
    val data: Color,
    val support: Support
)
