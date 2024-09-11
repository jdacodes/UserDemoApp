package com.jdacodes.userdemo.dashboard.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdacodes.userdemo.dashboard.domain.model.Color

@Entity(tableName = "colors")
data class ColorEntity(
    val color: String,
    @PrimaryKey
    val id: Int,
    val name: String,
    val pantoneValue: String,
    val year: Int
) {
    fun toColor(): Color {
        return Color(
            color = color,
            id = id,
            name = name,
            pantoneValue = pantoneValue,
            year = year

        )
    }
}
