package com.jdacodes.userdemo.dashboard.domain.use_case

import androidx.paging.PagingData
import com.jdacodes.userdemo.dashboard.domain.model.Color
import com.jdacodes.userdemo.dashboard.domain.repository.ColorRepository
import kotlinx.coroutines.flow.Flow

class GetColorListCase(private val repository: ColorRepository) {
    operator fun invoke(): Flow<PagingData<Color>> {
        return repository.getColorsPaging()
    }
}