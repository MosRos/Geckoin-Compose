package com.mrostami.geckoincompose.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PriceEntry(
    @PrimaryKey val timeStamp: Long = 0L,
    val coinId: String = "bitcoin",
    var price: Double = 0.0
)
