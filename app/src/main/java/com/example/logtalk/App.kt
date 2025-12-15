package com.example.logtalk

import android.app.Application
import com.example.logtalk.core.utils.Logger
import dagger.hilt.android.HiltAndroidApp
import com.example.logtalk.config.EnvManager
import com.example.logtalk.core.utils.model.DependencyContainer
import com.example.logtalk.data.AppDatabase

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Logger.init(BuildConfig.DEBUG)
        EnvManager.initialize { isSuccessful -> 
            if (isSuccessful) {
            } else {
            }
        }
    }
}