package com.jdacodes.userdemo.dashboard.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.jdacodes.userdemo.core.utils.JsonParser
import com.jdacodes.userdemo.dashboard.domain.model.Color
import com.jdacodes.userdemo.dashboard.domain.model.Support


@ProvidedTypeConverter
class ColorConverters(private val jsonParser: JsonParser) {

    @TypeConverter
    fun fromColorList(value: List<Color>?): String {
        val type = object : TypeToken<List<Color>>() {}.type
        return jsonParser.toJson(value, type) ?: "[]"
    }

    @TypeConverter
    fun toColorList(value: String): List<Color>? {
        val type = object : TypeToken<List<Color>>() {}.type
        return jsonParser.fromJson(value, type) ?: emptyList()
    }

    @TypeConverter
    fun fromColor(color: Color?): String {
        val type = object : TypeToken<Color>() {}.type
        return jsonParser.toJson(color, type) ?: ""
    }

    @TypeConverter
    fun toColor(value: String): Color? {
        val type = object : TypeToken<Color>() {}.type
        return jsonParser.fromJson(value, type)
    }

    @TypeConverter
    fun fromSupport(value: Support?): String {
        val type = object : TypeToken<List<Color>>() {}.type
        return jsonParser.toJson(value, type) ?: "[]"
    }

    @TypeConverter
    fun toSupport(value: String): Support? {
        val type = object : TypeToken<List<Color>>() {}.type
        return jsonParser.fromJson(value, type)
    }
}