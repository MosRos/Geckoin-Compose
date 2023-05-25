package com.mrostami.geckoincompose.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BitcoinPriceInfo(
    @PrimaryKey val id: String = "bitcoin",
    @Embedded var info: SimplePriceInfo
)
