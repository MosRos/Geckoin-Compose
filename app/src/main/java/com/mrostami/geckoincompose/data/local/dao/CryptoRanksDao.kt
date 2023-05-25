/*
 * *
 *  * Created by Moslem Rostami on 7/15/20 9:39 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 7/15/20 4:30 PM
 *
 */

package com.mrostami.geckoincompose.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.mrostami.geckoincompose.model.RankedCoin

@Dao
interface CryptoRanksDao {

    // For RankedCoins
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRankedCoins(coinsList: List<RankedCoin>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRankedCoin(rankedCoin: RankedCoin)

    @Query("SELECT * FROM RankedCoin ORDER BY marketCapRank ASC")
    suspend fun getAllRankedCoins(): List<RankedCoin>

    @Query("SELECT * FROM RankedCoin")
    suspend fun getRankedCoinsList(): List<RankedCoin>

    @Query("SELECT * FROM RankedCoin ORDER BY marketCapRank ASC")
    fun getPagedRankedCoins(): PagingSource<Int, RankedCoin>

    @Delete
    suspend fun deleteRankedCoin(rankedCoin: RankedCoin)

    @Delete
    suspend fun deleteRankedCoins(coinsList: List<RankedCoin>)

    @Query("DELETE FROM RankedCoin")
    suspend fun deleteAllRankedCoins()
}