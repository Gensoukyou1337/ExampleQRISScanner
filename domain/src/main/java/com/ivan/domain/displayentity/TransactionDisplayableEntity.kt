package com.ivan.domain.displayentity

import com.ivan.data.room.transactions.TransactionEntity

data class TransactionDisplayableEntity(
    val id: String,
    val merchantName: String,
    val merchantLocation: String,
    val transactionAmount: Long
)

fun TransactionEntity.asDisplayableEntity() = TransactionDisplayableEntity(
    this.id,
    this.merchantName,
    this.merchantLocation,
    this.transactionAmount
)