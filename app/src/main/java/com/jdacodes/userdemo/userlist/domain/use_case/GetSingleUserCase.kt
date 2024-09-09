package com.jdacodes.userdemo.userlist.domain.use_case

import com.jdacodes.userdemo.core.utils.Resource
import com.jdacodes.userdemo.userlist.domain.model.User
import com.jdacodes.userdemo.userlist.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetSingleUserCase(private val repository: UserRepository) {
    operator fun invoke(id: Int): Flow<Resource<User>> {
        return repository.getUser(id)
    }
}