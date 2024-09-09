package com.jdacodes.userdemo.userlist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jdacodes.userdemo.userlist.data.local.entity.SingleUserResponseEntity
import com.jdacodes.userdemo.userlist.data.local.entity.UserEntity
import com.jdacodes.userdemo.userlist.data.local.entity.UserListEntity

@Database(
    entities = [UserListEntity::class, SingleUserResponseEntity::class,UserEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(UserConverters::class) // You may need to define type converters for custom types like List<User>
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}