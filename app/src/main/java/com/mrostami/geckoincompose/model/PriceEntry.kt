package com.mrostami.geckoincompose.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PriceEntry(
    @PrimaryKey var timeStamp: Long = 0L,
    var coinId: String = "bitcoin",
    var price: Double = 0.0
)
