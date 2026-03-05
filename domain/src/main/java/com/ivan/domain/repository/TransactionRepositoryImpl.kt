package com.ivan.domain.repository

import android.util.Log
import com.ivan.data.room.datasource.TransactionsDataSource
import com.ivan.data.room.transactions.parseAsTransactionEntity
import com.ivan.domain.displayentity.TransactionDisplayableEntity
import com.ivan.domain.displayentity.asDisplayableEntity

class TransactionRepositoryImpl(
    private val dataSource: TransactionsDataSource
) : TransactionRepository {
    override fun insertTransactionData(rawString: String): Boolean {
        return try {
            dataSource.insertTransaction(rawString.parseAsTransactionEntity())
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun queryAllTransactionData(): List<TransactionDisplayableEntity> {
        return dataSource.getAllTransactions().map { it.asDisplayableEntity() }
    }
}