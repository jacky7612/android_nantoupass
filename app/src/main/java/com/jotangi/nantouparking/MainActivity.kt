package com.jotangi.nantouparking

import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class MainActivity : AppCompatActivity() {
    private lateinit var mAppUpdateManager: AppUpdateManager
    private val RC_APP_UPDATE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        checkUpdate()
    }

    private fun checkUpdate() {
        mAppUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        val appUpdateInfoTask: Task<AppUpdateInfo> = mAppUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() === UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Request the update.
                startUpdateFlow(appUpdateInfo)
            } else {

            }
        }
    }

    private fun startUpdateFlow(appUpdateInfo: AppUpdateInfo) {
        try {
            mAppUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.IMMEDIATE,
                this,
                RC_APP_UPDATE
            )
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }
}