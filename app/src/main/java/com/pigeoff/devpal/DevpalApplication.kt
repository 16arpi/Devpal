package com.pigeoff.devpal

import android.app.Application
import android.content.Intent
import android.content.ServiceConnection
import java.util.concurrent.Executor

class DevpalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}