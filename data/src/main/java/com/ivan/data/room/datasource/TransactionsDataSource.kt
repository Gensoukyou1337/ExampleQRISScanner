package com.ivan.data.room.datasource

import com.ivan.data.room.transactions.TransactionEntity

interface TransactionsDataSource {
    fun insertTransaction(transactionEntity: TransactionEntity)
    fun getAllTransactions(): List<TransactionEntity>
}