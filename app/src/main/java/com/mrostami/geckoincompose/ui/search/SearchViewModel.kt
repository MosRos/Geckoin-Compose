package com.mrostami.geckoincompose.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mrostami.geckoincompose.domain.usecases.SearchCoinsUseCase
import com.mrostami.geckoincompose.model.Coin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchCoinsUseCase: SearchCoinsUseCase
) : ViewModel() {

    fun searchCoin(query: String = ""): Flow<PagingData<Coin>> = searchCoinsUseCase.invoke(query).cachedIn(viewModelScope)
}