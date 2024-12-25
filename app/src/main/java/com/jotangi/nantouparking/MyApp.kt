package com.jotangi.nantouparking

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.ktx.Firebase

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)?.let {
            Log.d("MyApp", "Firebase initialized successfully")
        } ?: run {
            Log.e("MyApp", "Failed to initialize Firebase")
        }

        // Initialize Firebase Crashlytics
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        // Initialize Firebase Remote Config
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

        // Set default Remote Config parameters (optional)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }
}