package com.mrostami.geckoincompose.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EthereumPriceInfo(
    @PrimaryKey val id: String = "ethereum",
    @Embedded var info: SimplePriceInfo
)