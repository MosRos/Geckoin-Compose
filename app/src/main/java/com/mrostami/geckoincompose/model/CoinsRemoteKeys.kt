/*
 * *
 *  * Created by Moslem Rostami on 6/16/20 1:43 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/16/20 1:43 PM
 *
 */

package com.mrostami.geckoincompose.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity
data class CoinsRemoteKeys(
    @SerializedName("coin_id")
    @PrimaryKey
    var coin_Id: String = "fff000xxxdos",
    @SerializedName("id")
    var prevKey: Int?,
    var nextKey: Int?
)