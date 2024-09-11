package com.jdacodes.userdemo.dashboard.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jdacodes.userdemo.auth.data.local.AuthPreferences
import com.jdacodes.userdemo.core.utils.Resource
import com.jdacodes.userdemo.dashboard.data.local.ColorDao
import com.jdacodes.userdemo.dashboard.data.remote.ColorApiService
import com.jdacodes.userdemo.dashboard.data.remote.ColorPagingSource
import com.jdacodes.userdemo.dashboard.domain.model.Color
import com.jdacodes.userdemo.dashboard.domain.repository.ColorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class ColorRepositoryImpl(
    private val api: ColorApiService,
    private val dao: ColorDao,
    private val authPreferences: AuthPreferences
): ColorRepository {
    override fun getColorsPaging(): Flow<PagingData<Color>> {
        return Pager(
            config = PagingConfig(pageSize = 6, enablePlaceholders = false),
            pagingSourceFactory = { ColorPagingSource(api, dao) }
        ).flow
    }

    override fun getColor(id: Int): Flow<Resource<Color>> = flow {
        emit(Resource.Loading())

        val user = dao.getSingleColorById(id)?.toColor()
        emit(Resource.Loading(data = user))

        try {
            val remoteColor = api.getColor(id).data.toColorEntity()
            dao.deleteSingleColor(id)
            dao.insertSingleColor(remoteColor)
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "Oops something went wrong!",
                    data = user
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Couldn't reach server, check your internet connection.",
                    data = user
                )
            )
        }

        val newColor = dao.getSingleColorById(id)?.toColor()
        emit(Resource.Success(newColor))
    }

    override fun getUserProfile(): Flow<String> {
        return authPreferences.getUserData
    }

}