package com.brlea.barley_break.ui.main

import android.app.Application
import com.appsflyer.AppsFlyerLib
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel

const val ONESIGNAL_APP_ID = "8f98ee91-4b98-4be4-abf3-e258944cf92b"
const val APPSFlYER_ID = "HE5itBfhEBq4SV8CBcnJiG"
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initOneSignal()
        FirebaseApp.initializeApp(this)
        initRemoteConfig()
        initAppsFlyer()
    }

    private fun initOneSignal() {
        // Verbose Logging set to help debug issues, remove before releasing your app.
        OneSignal.Debug.logLevel = LogLevel.VERBOSE
        // OneSignal Initialization
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID)
    }

    private fun initAppsFlyer() {
        AppsFlyerLib.getInstance().init(APPSFlYER_ID, null, this)
        AppsFlyerLib.getInstance().start(this)
    }

    private fun initRemoteConfig() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(30)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
    }
}