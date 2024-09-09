package com.jdacodes.userdemo.userlist.domain.use_case

import androidx.paging.PagingData
import com.jdacodes.userdemo.userlist.domain.model.User
import com.jdacodes.userdemo.userlist.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserListCase(private val repository: UserRepository) {
    operator fun invoke(): Flow<PagingData<User>> {
        return repository.getUsersPaging()
    }
}
