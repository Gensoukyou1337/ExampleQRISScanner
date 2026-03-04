package com.ivan.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ivan.data.room.transactions.TransactionDAO
import com.ivan.data.room.transactions.TransactionEntity

@Database(entities = [TransactionEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDAO(): TransactionDAO
}