package com.ivan.domain.repository

import com.ivan.domain.displayentity.TransactionDisplayableEntity

interface TransactionRepository {
    fun insertTransactionData(rawString: String): Boolean
    fun queryAllTransactionData(): List<TransactionDisplayableEntity>
}