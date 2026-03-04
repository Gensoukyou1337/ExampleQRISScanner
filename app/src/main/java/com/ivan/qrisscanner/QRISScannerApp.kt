package com.ivan.qrisscanner

import android.app.Application
import com.ivan.domain.di.domainModule
import com.ivan.qrisscanner.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class QRISScannerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@QRISScannerApp)
            modules(appModule, domainModule)
        }
    }
}