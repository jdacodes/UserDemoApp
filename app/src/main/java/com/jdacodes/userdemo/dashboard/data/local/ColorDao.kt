package com.jdacodes.userdemo.dashboard.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import com.jdacodes.userdemo.dashboard.data.local.entity.ColorEntity

@Dao
@TypeConverters(ColorConverters::class)
interface ColorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertColorList(colorListEntity: List<ColorEntity>)

    @Query("DELETE FROM colors WHERE id IN (:ids)")
    suspend fun deleteColorList(ids: List<Int>)

    @Query("SELECT * FROM colors WHERE id IN (SELECT id FROM colors LIMIT :limit OFFSET :offset)")
    suspend fun getColorListByPage(limit: Int, offset: Int): List<ColorEntity>

    //Single ColorEntity Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleColor(singleColor: ColorEntity)

    @Query("DELETE FROM colors WHERE id = :id")
    suspend fun deleteSingleColor(id: Int)

    @Query("SELECT * FROM colors WHERE id = :id LIMIT 1")
    suspend fun getSingleColorById(id: Int): ColorEntity?
}