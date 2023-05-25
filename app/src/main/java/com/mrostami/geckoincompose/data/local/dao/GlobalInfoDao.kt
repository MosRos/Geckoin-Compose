package com.mrostami.geckoincompose.data.local.dao

import androidx.room.*
import com.mrostami.geckoincompose.model.BitcoinPriceInfo
import com.mrostami.geckoincompose.model.GlobalMarketInfo
import com.mrostami.geckoincompose.model.PriceEntry
import com.mrostami.geckoincompose.model.TrendCoin

@Dao
interface GlobalInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putGlobalInfo(info: GlobalMarketInfo)

    @Query("SELECT * FROM GlobalMarketInfo WHERE id = :id")
    suspend fun getGlobalMarketInfo(id: Int) : GlobalMarketInfo?

    @Query("DELETE FROM GlobalMarketInfo")
    suspend fun clearGlobalMarketInfo()

    // Btc price info
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putBtcPriceInfo(info: BitcoinPriceInfo)

    @Query("SELECT * FROM BitcoinPriceInfo WHERE id = :id")
    suspend fun getBtcPriceInfo(id: String) : BitcoinPriceInfo?

    @Query("DELETE FROM BitcoinPriceInfo")
    suspend fun clearBtcPriceInfo()

    // Price Chart info

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putPriceEntry(entry: PriceEntry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putPriceEntries(entries: List<PriceEntry>)

    @Query("SELECT * FROM PriceEntry WHERE coinId = :id")
    suspend fun getPriceChartEntries(id: String) : List<PriceEntry>

    @Query("DELETE FROM PriceEntry WHERE coinId = :coinId")
    suspend fun deletePriceChartEntries(coinId: String)

    @Query("DELETE FROM PriceEntry")
    suspend fun clearAllCoinsPriceChartInfo()

    // Trend Coins
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrendCoin(coin: TrendCoin)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrendCoins(coinsList: List<TrendCoin>)

    @Query("SELECT * FROM TrendCoin")
    suspend fun getTrendCoins(): List<TrendCoin>

    @Delete
    suspend fun deleteTrendCoin(coin: TrendCoin)

    @Delete
    suspend fun deleteTrendCoins(coinsList: List<TrendCoin>)

    @Query("DELETE FROM TrendCoin")
    suspend fun clearAllTrendCoins()
}