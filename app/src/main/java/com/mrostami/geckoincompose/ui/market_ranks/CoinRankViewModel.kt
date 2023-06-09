package com.mrostami.geckoincompose.ui.market_ranks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.domain.usecases.InitRankedCoinsPagingUseCase
import com.mrostami.geckoincompose.domain.usecases.MarketRanksUseCase
import com.mrostami.geckoincompose.model.RankedCoin
import com.mrostami.geckoincompose.ui.base.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class CoinRankViewModel @Inject constructor(
    private val cryptoRanksUseCase: MarketRanksUseCase) : ViewModel() {

    private val _uiSate: MutableStateFlow<RanksUiState> =
        MutableStateFlow(RanksUiState.defaultState)

    fun rankedCoinsStateFlow(): Flow<PagingData<RankedCoin>> = cryptoRanksUseCase.invoke().cachedIn(viewModelScope)


    init {
        rankedCoinsStateFlow()
    }

    fun onNewEvent(event: CoinsRanksEvents) {
        reduce(event = event, oldState = _uiSate.value)
    }

    private fun reduce(event: CoinsRanksEvents, oldState: RanksUiState) {
        when(event) {
            is CoinsRanksEvents.RetryOnError -> {
                rankedCoinsStateFlow()
            }
            is CoinsRanksEvents.EmptyDataError -> {
                rankedCoinsStateFlow()
            }
        }
    }
}

sealed interface CoinsRanksEvents {
    object RetryOnError : CoinsRanksEvents
    object EmptyDataError : CoinsRanksEvents
}

sealed interface CoinsRanksEffects {
    object GoToCoinDetail : CoinsRanksEffects
}

@Immutable
data class RanksUiState(
    override val state: BaseUiState.State = BaseUiState.State.LOADING,
    override val errorMessage: String? = null,
    override val data: PagingData<RankedCoin>
) : BaseUiState {
    companion object {
        val defaultState = RanksUiState(data = PagingData.empty())
    }
}