package com.mrostami.geckoincompose.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mrostami.geckoincompose.domain.usecases.SearchCoinsUseCase
import com.mrostami.geckoincompose.model.Coin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchCoinsUseCase: SearchCoinsUseCase
) : ViewModel() {

    var uiState: MutableStateFlow<PagingData<Coin>> = MutableStateFlow(PagingData.empty())
        private set
//    fun searchCoin(query: String = "") = searchCoinsUseCase.invoke(query).cachedIn(viewModelScope)

    private val searchJob = SupervisorJob()
    private val searchScope = viewModelScope.plus(searchJob)
    private val SEARCH_DEBUNCE = 650L

    init {
        searchCoins(null)
    }
    fun searchCoins(searchInput: String?) {
        searchScope.coroutineContext.cancelChildren()
        searchScope.launch {
            delay(SEARCH_DEBUNCE)
            val result = searchCoinsUseCase.invoke(
                query = searchInput
            ).catch { t ->

            }.cachedIn(viewModelScope)
            uiState.emitAll(result)
        }
    }
}