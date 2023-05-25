package com.mrostami.geckoincompose.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "Coin")
data class Coin(
    @SerializedName("id")
    var id: String?,
    @SerializedName("name")
    var name: String?, // 01coin
    @SerializedName("symbol")
    @PrimaryKey
    var symbol: String
)