package com.jdacodes.userdemo.userlist.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import com.jdacodes.userdemo.userlist.data.local.entity.UserEntity

@Dao
@TypeConverters(UserConverters::class)
interface UserDao {

    // UserListEntity Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserList(userListEntity: List<UserEntity>)

    @Query("DELETE FROM users WHERE id IN (:ids)")
    suspend fun deleteUserList(ids: List<Int>)

    @Query("SELECT * FROM users WHERE id IN (SELECT id FROM users LIMIT :limit OFFSET :offset)")
    suspend fun getUserListByPage(limit: Int, offset: Int): List<UserEntity>

    // SingleUserResponseEntity Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleUser(singleUser: UserEntity)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteSingleUser(id: Int)

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getSingleUserById(id: Int): UserEntity?
}
