package com.mrostami.geckoincompose.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mrostami.geckoincompose.data.local.dao.AllCoinsDao
import com.mrostami.geckoincompose.data.local.dao.CryptoRanksDao
import com.mrostami.geckoincompose.data.local.dao.GlobalInfoDao
import com.mrostami.geckoincompose.data.local.dao.RemoteKeysDao
import com.mrostami.geckoincompose.model.BitcoinPriceInfo
import com.mrostami.geckoincompose.model.Coin
import com.mrostami.geckoincompose.model.CoinsRemoteKeys
import com.mrostami.geckoincompose.model.EthereumPriceInfo
import com.mrostami.geckoincompose.model.GlobalMarketInfo
import com.mrostami.geckoincompose.model.PriceEntry
import com.mrostami.geckoincompose.model.RankedCoin
import com.mrostami.geckoincompose.model.TrendCoin

@Database(
    entities = [
        Coin::class,
        RankedCoin::class,
        CoinsRemoteKeys::class,
        GlobalMarketInfo::class,
        TrendCoin::class,
        BitcoinPriceInfo::class,
        EthereumPriceInfo::class,
        PriceEntry::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class CryptoDataBase : RoomDatabase() {

    abstract fun globalInfoDao(): GlobalInfoDao
    abstract fun ranksDao() : CryptoRanksDao
    abstract fun remoteKeysDao() : RemoteKeysDao
    abstract fun allCoinsDao() : AllCoinsDao

    companion object {
        @Volatile
        private var INSTANCE: CryptoDataBase? = null
        private const val DB_NAME = "geckoin_db"

        fun getInstance(context: Context): CryptoDataBase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, CryptoDataBase::class.java, DB_NAME)
                .build()
    }
}