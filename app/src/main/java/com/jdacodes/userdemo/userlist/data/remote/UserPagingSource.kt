package com.jdacodes.userdemo.userlist.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jdacodes.userdemo.userlist.data.local.UserDao
import com.jdacodes.userdemo.userlist.domain.model.User

class UserPagingSource(
    private val api: UserApiService,
    private val dao: UserDao
) : PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        // Get the current page, default to 1 if null
        val page = params.key ?: 1
        return try {
            // Fetch data from API
            val response = api.getUsers(page)

            // Convert the list of UserDto to UserEntity
            val userEntities = response.data.map { it.toUserEntity() }
            dao.insertUserList(userEntities)

            // Convert UserEntity to User domain model for display
            val users = userEntities.map { it.toUser() }

            // Return the loaded data and the next/prev keys
            LoadResult.Page(
                data = users,
                prevKey = if (page == 1) null else page - 1, // No previous page if we're on the first page
                nextKey = if (response.data.isNotEmpty()) page + 1 else null // Increment page for next request
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // Get the page number of the anchor position
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1) ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
