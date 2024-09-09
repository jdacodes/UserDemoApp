package com.jdacodes.userdemo.userlist.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jdacodes.userdemo.core.utils.Resource
import com.jdacodes.userdemo.userlist.data.local.UserDao
import com.jdacodes.userdemo.userlist.data.remote.UserApiService
import com.jdacodes.userdemo.userlist.domain.model.Support
import com.jdacodes.userdemo.userlist.domain.model.User
import com.jdacodes.userdemo.userlist.domain.model.UserList
import com.jdacodes.userdemo.userlist.domain.repository.UserRepository
import com.jdacodes.userdemo.userlist.data.remote.UserPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class UserRepositoryImpl(
    private val api: UserApiService,
    private val dao: UserDao
) : UserRepository {

    override fun getUsersPaging(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = 6, enablePlaceholders = false),
            pagingSourceFactory = { UserPagingSource(api, dao) }
        ).flow
    }

    //todo: To be deleted when paging fully handles the User list data
    override fun getUsers(page: Int): Flow<Resource<UserList>> = flow {
        emit(Resource.Loading())

        val offset = (page - 1) * 20
        val localUsers = dao.getUserListByPage(20, offset).map { it.toUser() }
        // Assuming a default support object if not fetched from the API.
        val defaultSupport = Support(
            text = "Default Support Text",
            url = "http://default.support.url"
        )
        emit(Resource.Loading(data = UserList(localUsers, page, 20, defaultSupport, 0, 0)))

        try {
            val remoteResponse = api.getUsers(page)
            val remoteUsers = remoteResponse.data.map { it.toUserEntity() }
            val support =
                remoteResponse.support.toSupport() // Extract support from the API response

            dao.deleteUserList(localUsers.map { it.id })  // Delete old data based on IDs
            dao.insertUserList(remoteUsers)  // Insert new data

            val newUsers = dao.getUserListByPage(20, offset).map { it.toUser() }

            emit(
                Resource.Success(
                    UserList(
                        newUsers,
                        page,
                        20,
                        support,
                        remoteResponse.total,
                        remoteResponse.totalPages
                    )
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "Oops something went wrong!",
                    data = UserList(localUsers, page, 20, defaultSupport, 0, 0)
                )
            )

        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Couldn't reach server, check your internet connection.",
                    data = UserList(localUsers, page, 20, defaultSupport, 0, 0)
                )
            )

        }


    }

    override fun getUser(id: Int): Flow<Resource<User>> = flow {
        emit(Resource.Loading())

        val user = dao.getSingleUserById(id)?.toUser()
        emit(Resource.Loading(data = user))

        try {
            val remoteUser = api.getUser(id).data.toUserEntity()
            dao.deleteSingleUser(id)
            dao.insertSingleUser(remoteUser)
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

        val newUser = dao.getSingleUserById(id)?.toUser()
        emit(Resource.Success(newUser))
    }
}
