package com.dicoding.aplikasistoryku.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.aplikasistoryku.data.api.ApiService
import com.dicoding.aplikasistoryku.data.repository.UserRepository
import com.dicoding.aplikasistoryku.data.response.StoryResponse

//class StoryPagingSource(private val apiService: ApiService, private val userRepository: UserRepository) : PagingSource<Int, StoryResponse>() {
//    private companion object {
//        const val INITIAL_PAGE_INDEX = 1
//        const val PAGE_SIZE = 20 // Ukuran halaman default
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResponse> {
//        return try {
//            val position = params.key ?: INITIAL_PAGE_INDEX
//            val token = userRepository.getUser().token
//            val responseData = apiService.getStories(token, position, params.loadSize)
//            LoadResult.Page(
//                data = responseData,
//                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
//                nextKey = if (responseData.isEmpty()) null else position + 1
//            )
//        } catch (exception: Exception) {
//            LoadResult.Error(exception)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, StoryResponse>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//}