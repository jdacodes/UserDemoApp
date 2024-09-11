package com.jdacodes.userdemo.dashboard.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jdacodes.userdemo.dashboard.data.local.ColorDao
import com.jdacodes.userdemo.dashboard.domain.model.Color

class ColorPagingSource(
    private val api: ColorApiService,
    private val dao: ColorDao
) : PagingSource<Int, Color>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Color> {
        // Get the current page, default to 1 if null
        val page = params.key ?: 1
        return try {
            // Fetch data from API
            val response = api.getColors(page)

            // Convert the list of Color dto to entity
            val colorEntities = response.data.map { it.toColorEntity() }
            dao.insertColorList(colorEntities)

            // Convert UserEntity to User domain model for display
            val colors = colorEntities.map { it.toColor() }

            // Return the loaded data and the next/prev keys
            LoadResult.Page(
                data = colors,
                prevKey = if (page == 1) null else page - 1, // No previous page if we're on the first page
                nextKey = if (response.data.isNotEmpty()) page + 1 else null // Increment page for next request
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Color>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            // Get the page number of the anchor position
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}