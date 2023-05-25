package com.mrostami.geckoincompose.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.mrostami.geckoincompose.model.Coin

@Dao
interface AllCoinsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(coinsList: List<Coin>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoin(coin: Coin)

    @Query("SELECT * FROM Coin")
    suspend fun getAllCoins(): List<Coin>

    @Delete
    suspend fun deletCoin(coin: Coin)

    @Delete
    suspend fun deletCoins(coinsList: List<Coin>)

    @Query("DELETE FROM Coin")
    suspend fun deleteAllCoins()

    @Query("SELECT * FROM Coin WHERE symbol LIKE :input || '%' OR name LIKE :input || '%'")
    suspend fun searchCoins(input: String): List<Coin>

    @Query("SELECT * FROM Coin WHERE symbol LIKE :input || '%' OR name LIKE :input || '%' ORDER BY symbol ASC")
    fun searchPagedCoins(input: String): PagingSource<Int, Coin>

    @Query("SELECT * FROM Coin ORDER BY symbol ASC")
    fun getPagedCoins(): PagingSource<Int, Coin>
}