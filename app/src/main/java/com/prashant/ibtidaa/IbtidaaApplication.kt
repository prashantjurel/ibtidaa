package com.prashant.ibtidaa

import android.app.Application
import timber.log.Timber

class IbtidaaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}