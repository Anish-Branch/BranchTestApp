package com.example.branchdemoapp

import android.app.Application
import android.content.res.Configuration
import io.branch.referral.Branch

class CustomApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        // Branch logging for debugging
        Branch.enableLogging()

        // Branch object initialization
        Branch.getAutoInstance(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Handle configuration changes if needed
    }

    override fun onTerminate() {
        super.onTerminate()
        // Clean up any resources if needed
    }
}