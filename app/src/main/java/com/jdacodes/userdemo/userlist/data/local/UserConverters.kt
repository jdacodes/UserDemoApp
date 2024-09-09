package com.jdacodes.userdemo.userlist.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter

import com.google.gson.reflect.TypeToken
import com.jdacodes.userdemo.core.utils.JsonParser
import com.jdacodes.userdemo.userlist.domain.model.Support
import com.jdacodes.userdemo.userlist.domain.model.User

@ProvidedTypeConverter
class UserConverters(private val jsonParser: JsonParser) {

    @TypeConverter
    fun fromUserList(value: List<User>?): String {
        val type = object : TypeToken<List<User>>() {}.type
        return jsonParser.toJson(value, type) ?: "[]"
    }

    @TypeConverter
    fun toUserList(value: String): List<User>? {
        val type = object : TypeToken<List<User>>() {}.type
        return jsonParser.fromJson(value, type) ?: emptyList()
    }

    @TypeConverter
    fun fromUser(user: User?): String {
        val type = object : TypeToken<User>() {}.type
        return jsonParser.toJson(user, type) ?: ""
    }

    @TypeConverter
    fun toUser(value: String): User? {
        val type = object : TypeToken<User>() {}.type
        return jsonParser.fromJson(value, type)
    }

    @TypeConverter
    fun fromSupport(value: Support?): String {
        val type = object : TypeToken<List<User>>() {}.type
        return jsonParser.toJson(value, type) ?: "[]"
    }

    @TypeConverter
    fun toSupport(value: String): Support? {
        val type = object : TypeToken<List<User>>() {}.type
        return jsonParser.fromJson(value, type)
    }
}


