package com.jdacodes.userdemo.dashboard.domain.repository

import androidx.paging.PagingData
import com.jdacodes.userdemo.core.utils.Resource
import com.jdacodes.userdemo.dashboard.domain.model.Color
import kotlinx.coroutines.flow.Flow

interface ColorRepository {
    fun getColorsPaging(): Flow<PagingData<Color>>
    fun getColor(id: Int): Flow<Resource<Color>>
    fun getUserProfile(): Flow<String>
}