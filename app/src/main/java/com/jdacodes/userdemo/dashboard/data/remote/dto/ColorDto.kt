package com.jdacodes.userdemo.dashboard.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.jdacodes.userdemo.dashboard.data.local.entity.ColorEntity
import com.jdacodes.userdemo.dashboard.domain.model.Color

data class ColorDto(
    @SerializedName("color")
    val color: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("pantone_value")
    val pantoneValue: String,
    @SerializedName("year")
    val year: Int
) {
    companion object {
        fun toColorList(colorListDto: List<ColorDto>): List<Color> {
            return colorListDto.map { dto ->
                Color(
                    color = dto.color,
                    id = dto.id,
                    name = dto.name,
                    pantoneValue = dto.pantoneValue,
                    year = dto.year
                )
            }
        }
    }

    fun toColor(): Color {
        return Color(
            color = color,
            id = id,
            name = name,
            pantoneValue = pantoneValue,
            year = year
        )
    }

    fun toColorEntity(): ColorEntity {
        return ColorEntity(
            color = color,
            id = id,
            name = name,
            pantoneValue = pantoneValue,
            year = year
        )
    }
}