package com.jdacodes.userdemo.dashboard.domain.use_case

import com.jdacodes.userdemo.core.utils.Resource
import com.jdacodes.userdemo.dashboard.domain.model.Color
import com.jdacodes.userdemo.dashboard.domain.repository.ColorRepository
import kotlinx.coroutines.flow.Flow

class GetSingleColorCase(private val repository: ColorRepository) {
    operator fun invoke(id: Int): Flow<Resource<Color>> {
        return repository.getColor(id)
    }
}