package com.jdacodes.userdemo.dashboard.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdacodes.userdemo.dashboard.domain.model.Color
import com.jdacodes.userdemo.dashboard.domain.model.Support


@Entity
data class SingleColorResponseEntity(
    @PrimaryKey val id: Int,
    val data: Color,
    val support: Support
)
