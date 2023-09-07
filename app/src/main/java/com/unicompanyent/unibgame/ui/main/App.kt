package com.unicompanyent.unibgame.ui.main

import android.app.Application
import android.util.Log
import com.appsflyer.AppsFlyerLib

import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel

const val ONESIGNAL_APP_ID = "cbcadf78-1102-4103-b994-cb30ef11e387"
const val APPSFlYER_ID = ""

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initOneSignal()
        FirebaseApp.initializeApp(this)
        initRemoteConfig()
        initAppsFlyer()
        //initFacebook()
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

    /*private fun initFacebook() {
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
        //check Initialize
        if (FacebookSdk.isInitialized()) {
            Log.d("FacebookSDK", "Facebook SDK Initialize successfully")
        } else {
            Log.e("FacebookSDK", "Error initialize Facebook SDK")
        }
    }*/
}