package com.mrostami.geckoincompose.ui.home

import androidx.lifecycle.ViewModel
import com.mrostami.geckoincompose.domain.usecases.BitcoinChartInfoUseCase
import com.mrostami.geckoincompose.domain.usecases.BitcoinSimplePriceUseCase
import com.mrostami.geckoincompose.domain.usecases.GlobalMarketInfoUseCase
import com.mrostami.geckoincompose.domain.usecases.TrendCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val globalMarketInfoUseCase: GlobalMarketInfoUseCase,
    private val trendCoinsUseCase: TrendCoinsUseCase,
    private val bitcoinSimplePriceUseCase: BitcoinSimplePriceUseCase,
    private val bitcoinChartInfoUseCase: BitcoinChartInfoUseCase
) : ViewModel() {

}