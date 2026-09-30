package com.tuantuan.journal

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TuantuanApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Hilt 自动处理依赖注入
    }
}