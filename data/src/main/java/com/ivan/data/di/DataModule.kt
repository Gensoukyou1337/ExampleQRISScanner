package com.ivan.data.di

import androidx.room.Room
import com.ivan.data.room.AppDatabase
import com.ivan.data.room.datasource.TransactionsDataSource
import com.ivan.data.room.datasource.TransactionsDataSourceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "qris-scanner-database"
        ).build()
    }

    factory<TransactionsDataSource> { TransactionsDataSourceImpl(get()) }
}