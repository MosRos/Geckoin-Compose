package com.mrostami.geckoincompose.ui.market_ranks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.domain.usecases.GetRankedCoinsUseCase
import com.mrostami.geckoincompose.domain.usecases.MarketRanksUseCase
import com.mrostami.geckoincompose.model.RankedCoin
import com.mrostami.geckoincompose.ui.base.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class CoinRankViewModel @Inject constructor(
    private val cryptoRanksUseCase: MarketRanksUseCase,
    private val getRankedCoinsUseCase: GetRankedCoinsUseCase
) : ViewModel() {

    val PAGE_SIZE = 50

    private val _uiSate: MutableStateFlow<RanksUiState> =
        MutableStateFlow(RanksUiState.defaultState)
    val uiState: StateFlow<RanksUiState> = _uiSate

//    fun rankedCoinsStateFlow(): Flow<PagingData<RankedCoin>> = cryptoRanksUseCase.invoke().cachedIn(viewModelScope)


    init {

    }


    fun onNewEvent(event: CoinsRanksEvents) {
        reduce(event = event, oldState = _uiSate.value)
    }

    private fun reduce(event: CoinsRanksEvents, oldState: RanksUiState) {
        when (event) {
            is CoinsRanksEvents.RetryOnError -> {
//                loadMoreItems()
            }

            is CoinsRanksEvents.EmptyDataError -> {
//                loadMoreItems()
            }

            is CoinsRanksEvents.LoadMore -> {
                loadMoreItems()
            }
        }
    }

    private fun loadMoreItems() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_uiSate.value.state == BaseUiState.State.LOADING) return@launch
            val offset = if (_uiSate.value.data.isEmpty()) 0 else _uiSate.value.data.size
            val items: ArrayList<RankedCoin> = ArrayList(_uiSate.value.data)
            _uiSate.emit(
                _uiSate.value.copy(
                    state = BaseUiState.State.LOADING
                )
            )
            getRankedCoinsUseCase.invoke(offset = offset, limit = PAGE_SIZE)
                .collectLatest { result ->
                    when (result) {
                        is com.mrostami.geckoincompose.domain.base.Result.Success -> {
                            items.addAll(result.data)
                            val sortedList = items.sortedBy { it.marketCapRank }.distinctBy { it.id }
                            _uiSate.emit(
                                _uiSate.value.copy(
                                    state = BaseUiState.State.SUCCESS,
                                    data = sortedList,
                                    offset = _uiSate.value.data.size
                                )
                            )
                        }

                        is Result.Loading -> {
                            _uiSate.emit(
                                _uiSate.value.copy(
                                    state = BaseUiState.State.LOADING
                                )
                            )
                        }

                        is Result.Error -> {
                            _uiSate.emit(
                                _uiSate.value.copy(
                                    state = BaseUiState.State.ERROR,
                                    errorMessage = result.message
                                )
                            )
                        }
                    }
                }
        }
    }
}

sealed interface CoinsRanksEvents {
    object RetryOnError : CoinsRanksEvents
    object EmptyDataError : CoinsRanksEvents
    object LoadMore : CoinsRanksEvents
}

sealed interface CoinsRanksEffects {
    object GoToCoinDetail : CoinsRanksEffects
}

@Immutable
data class RanksUiState(
    override val state: BaseUiState.State = BaseUiState.State.SUCCESS,
    override val errorMessage: String? = null,
    override val data: List<RankedCoin>,
    val offset: Int = 0
) : BaseUiState {
    companion object {
        val defaultState = RanksUiState(data = emptyList())
    }
}