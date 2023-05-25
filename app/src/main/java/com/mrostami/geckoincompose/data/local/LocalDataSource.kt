package com.mrostami.geckoincompose.data.local

import androidx.appcompat.app.AppCompatDelegate
import androidx.paging.PagingSource
import com.mrostami.geckoincompose.data.local.preferences.DataStoreHelper
import com.mrostami.geckoincompose.data.local.preferences.PreferencesHelper
import com.mrostami.geckoincompose.data.local.dao.AllCoinsDao
import com.mrostami.geckoincompose.data.local.dao.CryptoRanksDao
import com.mrostami.geckoincompose.data.local.dao.GlobalInfoDao
import com.mrostami.geckoincompose.data.local.dao.PreferencesDao
import com.mrostami.geckoincompose.data.local.dao.RemoteKeysDao
import com.mrostami.geckoincompose.model.BitcoinPriceInfo
import com.mrostami.geckoincompose.model.Coin
import com.mrostami.geckoincompose.model.CoinsRemoteKeys
import com.mrostami.geckoincompose.model.GlobalMarketInfo
import com.mrostami.geckoincompose.model.PriceEntry
import com.mrostami.geckoincompose.model.RankedCoin
import com.mrostami.geckoincompose.model.TrendCoin
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val cryptoDataBase: CryptoDataBase,
    private val preferencesHelper: PreferencesHelper,
    private val dataStoreHelper: DataStoreHelper
) : PreferencesDao, GlobalInfoDao, CryptoRanksDao, RemoteKeysDao, AllCoinsDao {

    private val globalInfoDao: GlobalInfoDao by lazy { cryptoDataBase.globalInfoDao() }
    private val cryptoMarketDao by lazy { cryptoDataBase.ranksDao() }
    private val remoteKeysDao by lazy { cryptoDataBase.remoteKeysDao() }
    private val allCoinsDao by lazy { cryptoDataBase.allCoinsDao() }

    // Preferences setter and getter
    override suspend fun changeTheme(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
        preferencesHelper.selectedThemeMode = mode
        dataStoreHelper.changeTheme(mode)
    }

    override suspend fun getThemeMode(): Flow<Int> {
        AppCompatDelegate.setDefaultNightMode(preferencesHelper.selectedThemeMode)
        return dataStoreHelper.getThemeMode()
    }

    override fun getAuthToken(): String? {
        return preferencesHelper.authToken
    }

    override fun setAuthToken(token: String) {
        preferencesHelper.authToken = token
    }

    override fun setLastSyncDate(time: Long) {
        preferencesHelper.lastDbSyncDate = time
    }

    override fun getLastSyncDate(): Long {
        return preferencesHelper.lastDbSyncDate
    }

    //Global Info DRUDs
    override suspend fun putGlobalInfo(info: GlobalMarketInfo) = globalInfoDao.putGlobalInfo(info)
    override suspend fun getGlobalMarketInfo(id: Int): GlobalMarketInfo? =
        globalInfoDao.getGlobalMarketInfo(id = 1)

    override suspend fun clearGlobalMarketInfo() = globalInfoDao.clearGlobalMarketInfo()
    override suspend fun putBtcPriceInfo(info: BitcoinPriceInfo) =
        globalInfoDao.putBtcPriceInfo(info)

    override suspend fun getBtcPriceInfo(id: String): BitcoinPriceInfo? =
        globalInfoDao.getBtcPriceInfo(id)

    override suspend fun clearBtcPriceInfo() = globalInfoDao.clearBtcPriceInfo()
    override suspend fun putPriceEntry(entry: PriceEntry) = globalInfoDao.putPriceEntry(entry)
    override suspend fun putPriceEntries(entries: List<PriceEntry>) =
        globalInfoDao.putPriceEntries(entries)

    override suspend fun getPriceChartEntries(id: String): List<PriceEntry> =
        globalInfoDao.getPriceChartEntries(id = id)

    override suspend fun deletePriceChartEntries(coinId: String) =
        globalInfoDao.deletePriceChartEntries(coinId = coinId)

    override suspend fun clearAllCoinsPriceChartInfo() = globalInfoDao.clearAllCoinsPriceChartInfo()
    override suspend fun insertTrendCoin(coin: TrendCoin) = globalInfoDao.insertTrendCoin(coin)
    override suspend fun insertTrendCoins(coinsList: List<TrendCoin>) =
        globalInfoDao.insertTrendCoins(coinsList)

    override suspend fun getTrendCoins(): List<TrendCoin> = globalInfoDao.getTrendCoins()
    override suspend fun deleteTrendCoin(coin: TrendCoin) = globalInfoDao.deleteTrendCoin(coin)
    override suspend fun deleteTrendCoins(coinsList: List<TrendCoin>) =
        globalInfoDao.deleteTrendCoins(coinsList)

    override suspend fun clearAllTrendCoins() = globalInfoDao.clearAllTrendCoins()

    // RankedCoins
    override suspend fun insertRankedCoins(coinsList: List<RankedCoin>) =
        cryptoMarketDao.insertRankedCoins(coinsList)

    override suspend fun insertRankedCoin(rankedCoin: RankedCoin) =
        cryptoMarketDao.insertRankedCoin(rankedCoin)

    override suspend fun getAllRankedCoins(): List<RankedCoin> = cryptoMarketDao.getAllRankedCoins()
    override suspend fun getRankedCoinsList(): List<RankedCoin> =
        cryptoMarketDao.getRankedCoinsList()

    override fun getPagedRankedCoins(): PagingSource<Int, RankedCoin> =
        cryptoMarketDao.getPagedRankedCoins()

    override suspend fun deleteRankedCoin(rankedCoin: RankedCoin) =
        cryptoMarketDao.deleteRankedCoin(rankedCoin)

    override suspend fun deleteRankedCoins(coinsList: List<RankedCoin>) =
        cryptoMarketDao.deleteRankedCoins(coinsList)

    override suspend fun deleteAllRankedCoins() = cryptoMarketDao.deleteAllRankedCoins()
    override suspend fun insertAllRemoteKeys(remoteKeys: List<CoinsRemoteKeys>) =
        remoteKeysDao.insertAllRemoteKeys(remoteKeys)

    override suspend fun remoteKeysCoinId(coinId: String): CoinsRemoteKeys? =
        remoteKeysDao.remoteKeysCoinId(coinId)

    override suspend fun clearCoinsRemoteKeys() = remoteKeysDao.clearCoinsRemoteKeys()

    //All coins & search coins
    override suspend fun insertCoins(coinsList: List<Coin>) = allCoinsDao.insertCoins(coinsList)
    override suspend fun insertCoin(coin: Coin) = allCoinsDao.insertCoin(coin)
    override suspend fun getAllCoins(): List<Coin> = allCoinsDao.getAllCoins()
    override suspend fun deletCoin(coin: Coin) = allCoinsDao.deletCoin(coin)
    override suspend fun deletCoins(coinsList: List<Coin>) = allCoinsDao.deletCoins(coinsList)
    override suspend fun deleteAllCoins() = allCoinsDao.deleteAllCoins()
    override suspend fun searchCoins(input: String): List<Coin> = allCoinsDao.searchCoins(input)
    override fun searchPagedCoins(input: String): PagingSource<Int, Coin> =
        allCoinsDao.searchPagedCoins(input)

    override fun getPagedCoins(): PagingSource<Int, Coin> = allCoinsDao.getPagedCoins()
}