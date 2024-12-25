package com.jotangi.nantouparking

import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class MainActivity : AppCompatActivity() {
    private lateinit var mAppUpdateManager: AppUpdateManager
    private val RC_APP_UPDATE = 100
    private lateinit var remoteConfig: FirebaseRemoteConfig
    var latestVersion = ""
    val currentVersion = BuildConfig.VERSION_NAME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("RemoteConfig","Remote1" )

    }

    override fun onStart() {
        super.onStart()
        Log.d("RemoteConfig", "Remote2")
        remoteConfig()
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

    private fun remoteConfig() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 // For production, set it to a higher value
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        // Fetch and activate Remote Config
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("RemoteConfig", "Fetch and activate succeeded.")
                    // Fetch and activate succeeded
                    val updated = task.result
                    latestVersion = remoteConfig.getString("latest_app_version")
                    if (isNewVersionAvailable(currentVersion, latestVersion)) {
                        // Prompt the user to update the app
                        promptUpdateDialog()
                    } else {
                        // No update needed
                        Log.d("AppVersionCheck", "No update required.")
                    }
                } else {
                    // Fetch failed
                }
            }
    }
    private fun isNewVersionAvailable(currentVersion: String, latestVersion: String): Boolean {
        return latestVersion.compareTo(currentVersion) > 0
    }

    // Function to show a dialog or prompt to the user to update the app
    private fun promptUpdateDialog() {
        // Show your update dialog here (for example, AlertDialog)
        AlertDialog.Builder(this)
            .setTitle("版本更新提示")
            .setMessage("親愛的使用者您好，停車APP功能已進行優化，請前往商店進行程式更新，以維護您的權益，謝謝。")
            .setPositiveButton("更新") { _, _ ->
                // Redirect to Play Store for update
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
                )
                startActivity(intent)
            }
            .show()
    }
}