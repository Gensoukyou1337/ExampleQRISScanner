package com.ivan.qrisscanner.di

import com.ivan.qrisscanner.screens.history.QRHistoryViewModel
import com.ivan.qrisscanner.screens.scanner.QRScannerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<QRScannerViewModel> { QRScannerViewModel(get()) }
    viewModel<QRHistoryViewModel> { QRHistoryViewModel(get()) }
}