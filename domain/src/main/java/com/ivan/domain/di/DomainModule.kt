package com.ivan.domain.di

import com.ivan.data.di.dataModule
import com.ivan.domain.repository.TransactionRepository
import com.ivan.domain.repository.TransactionRepositoryImpl
import org.koin.dsl.module

val domainModule = module {
    includes(dataModule)

    factory<TransactionRepository> { TransactionRepositoryImpl(get()) }
}