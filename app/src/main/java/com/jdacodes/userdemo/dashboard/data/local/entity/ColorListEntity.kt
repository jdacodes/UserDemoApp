package com.jdacodes.userdemo.dashboard.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdacodes.userdemo.dashboard.domain.model.Color
import com.jdacodes.userdemo.dashboard.domain.model.Support

@Entity
data class ColorListEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val data: List<Color>,
    val page: Int,
    val perPage: Int,
    val support: Support,
    val total: Int,
    val totalPages: Int
)
