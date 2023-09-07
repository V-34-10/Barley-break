package com.unicompanyent.unibgame.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

object RemoteConfigUtils {
    fun openPolicyLink(context: Context) {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.fetch().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                remoteConfig.activate().addOnSuccessListener {
                    val policyLink = remoteConfig.getString("uni_privacy_policy_key")
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(policyLink))
                    context.startActivity(intent)
                }
            }
        }
    }
}