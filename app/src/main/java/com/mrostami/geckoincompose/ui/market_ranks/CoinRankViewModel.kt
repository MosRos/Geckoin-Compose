package com.mrostami.geckoincompose.ui.market_ranks

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.domain.usecases.MarketRanksUseCase
import com.mrostami.geckoincompose.model.RankedCoin
import com.mrostami.geckoincompose.ui.base.BaseUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class CoinRankViewModel @Inject constructor(
    private val cryptoRanksUseCase: MarketRanksUseCase
) : ViewModel() {

    private val _uiSate: MutableStateFlow<RanksUiModel> = MutableStateFlow(RanksUiModel.defaultState)
    val rankedCoinsState: MutableStateFlow<PagingData<RankedCoin>> = MutableStateFlow(PagingData.empty())
    fun rankedCoinsStateFlow() : Flow<PagingData<RankedCoin>> = cryptoRanksUseCase.invoke().cachedIn(viewModelScope)
    fun getPagedRankedCoins2(refresh: Boolean = false) {
        viewModelScope.launch {
            cryptoRanksUseCase.invoke().collectLatest {

            }
//                .collectLatest { result ->
//                when(result) {
//                    is Result.Success -> {
//                        rankedCoinsState.emit(result.data)
//                        _uiSate.emit(
//                            RanksUiModel(
//                                state = BaseUiModel.State.SUCCESS,
//                                errorMessage = null,
//                                data = result.data
//                            )
//                        )
//                    }
//                    is Result.Error -> {
//                        _uiSate.emit(
//                            RanksUiModel(
//                                state = BaseUiModel.State.ERROR,
//                                errorMessage = result.message,
//                                data = PagingData.empty()
//                            )
//                        )
//                    }
//                    is Result.Loading -> {
//                        _uiSate.emit(
//                            RanksUiModel.defaultState
//                        )
//                    }
//                }
//            }
        }
    }

    init {
        rankedCoinsStateFlow()
//        getPagedRankedCoins(refresh = true)
    }

    fun onNewEvent(event: CoinsRanksEvents) {
        reduce(events = event, oldState = _uiSate.value)
    }
    private fun reduce(events: CoinsRanksEvents, oldState: RanksUiModel) {

    }
}

sealed interface CoinsRanksEvents {
    object RetryOnError : CoinsRanksEvents
    object EmptyDataError: CoinsRanksEvents
}

sealed interface CoinsRanksEffects {
    object GoToCoinDetail : CoinsRanksEffects
}

@Immutable
data class RanksUiModel(
    override val state: BaseUiModel.State = BaseUiModel.State.LOADING,
    override val errorMessage: String? = null,
    override val data: PagingData<RankedCoin>
) : BaseUiModel {
    companion object {
        val defaultState = RanksUiModel(data = PagingData.empty())
    }
}