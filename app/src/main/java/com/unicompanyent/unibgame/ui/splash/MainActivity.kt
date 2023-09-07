package com.unicompanyent.unibgame.ui.splash

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.telephony.TelephonyManager
import android.view.View
import com.unicompanyent.unibgame.R
import com.unicompanyent.unibgame.ui.main.game.MenuActivity
import com.unicompanyent.unibgame.ui.main.webview.WebViewActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadingNextActivity()
    }

    private fun loadingNextActivity() {
        Handler().postDelayed({
            if (isExistsAndActiveSim(this) && isInternetAvailable()) {
                // run WebViewActivity
                val go = Intent(this@MainActivity, WebViewActivity::class.java)
                startActivity(go)
                finish()
            } else {
                // run MenuActivity
                val go = Intent(this@MainActivity, MenuActivity::class.java)
                startActivity(go)
                finish()
            }
        }, 3 * 1000.toLong())
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun isExistsAndActiveSim(context: Context): Boolean {
        return try {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            telephonyManager.simState == TelephonyManager.SIM_STATE_READY
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}