package com.jdacodes.userdemo.userlist.domain.repository

import androidx.paging.PagingData
import com.jdacodes.userdemo.core.utils.Resource
import com.jdacodes.userdemo.userlist.domain.model.User
import com.jdacodes.userdemo.userlist.domain.model.UserList
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsersPaging(): Flow<PagingData<User>>
    fun getUsers(page: Int): Flow<Resource<UserList>>
    fun getUser(id: Int): Flow<Resource<User>>
}