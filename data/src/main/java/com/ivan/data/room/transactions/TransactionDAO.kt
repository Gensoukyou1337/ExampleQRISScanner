package com.ivan.data.room.transactions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionDAO {
    @Insert
    fun insertTransactionData(transactionData: TransactionEntity)

    @Query("SELECT * FROM TransactionEntity")
    fun getAllTransactionData(): List<TransactionEntity>
}