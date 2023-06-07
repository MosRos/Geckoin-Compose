package com.mrostami.geckoincompose.data.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mrostami.geckoincompose.data.remote.RemoteDataSource
import com.mrostami.geckoincompose.model.RankedCoin
import javax.inject.Inject

class NetworkMarketRanksPagingSource @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : PagingSource<Int, RankedCoin>() {
    override val jumpingSupported: Boolean = true
    override val keyReuseSupported: Boolean = true

    override fun getRefreshKey(state: PagingState<Int, RankedCoin>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RankedCoin> {
        return try {
            val page = params.key ?: 1
            val response = remoteDataSource.getPagedMarketRanks(page = page, perPage = PAGE_SIZE)
                .fold(
                    {
                        listOf()
                    },
                    {
                        it
                    }
                )

            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.isEmpty()) null else page.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}