package com.jdacodes.userdemo.dashboard.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jdacodes.userdemo.dashboard.data.local.entity.ColorEntity
import com.jdacodes.userdemo.dashboard.data.local.entity.ColorListEntity
import com.jdacodes.userdemo.dashboard.data.local.entity.SingleColorResponseEntity

@Database(
    entities = [ColorListEntity::class, SingleColorResponseEntity::class, ColorEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ColorConverters::class) // You may need to define type converters for custom types like List<User>
abstract class ColorDatabase : RoomDatabase() {
    abstract fun colorDao(): ColorDao
}