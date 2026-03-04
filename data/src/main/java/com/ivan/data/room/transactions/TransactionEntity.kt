package com.ivan.data.room.transactions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TransactionEntity(
    @PrimaryKey val timestamp: Long,
    @ColumnInfo("id") val id: String,
    @ColumnInfo("merchant_name") val merchantName: String,
    @ColumnInfo("merchant_location") val merchantLocation: String,
    @ColumnInfo("transaction_amount") val transactionAmount: Long,
)

fun String.parseAsTransactionEntity(): TransactionEntity {
    try {
        val parameters = this.split("|")
        return TransactionEntity(
            timestamp = System.currentTimeMillis(),
            id = parameters[0],
            merchantName = parameters[1],
            merchantLocation = parameters[2],
            transactionAmount = parameters[3].toLong(),
        )
    } catch (e: Exception) {
        throw IllegalArgumentException("String has invalid format")
    }
}