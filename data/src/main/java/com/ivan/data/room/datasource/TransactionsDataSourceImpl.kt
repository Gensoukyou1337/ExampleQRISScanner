package com.ivan.data.room.datasource

import com.ivan.data.room.AppDatabase
import com.ivan.data.room.transactions.TransactionEntity

class TransactionsDataSourceImpl(
    private val appDatabase: AppDatabase
) : TransactionsDataSource {
    override fun insertTransaction(transactionEntity: TransactionEntity) {
        appDatabase.transactionDAO().insertTransactionData(transactionEntity)
    }

    override fun getAllTransactions(): List<TransactionEntity> {
        return appDatabase.transactionDAO().getAllTransactionData()
            .sortedByDescending { it.timestamp }
    }
}