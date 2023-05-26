package com.mrostami.geckoincompose.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Entity(tableName = "Coin")
@Serializable
data class Coin(
    @SerialName("id")
    var id: String?,
    @SerialName("name")
    var name: String?, // 01coin
    @SerialName("symbol")
    @PrimaryKey
    var symbol: String
)