package com.brlea.barley_break.ui.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.telephony.TelephonyManager
import android.view.View
import com.brlea.barley_break.R
import com.brlea.barley_break.ui.main.game.SceneActivity
import com.brlea.barley_break.ui.main.webview.WebViewActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadingNextActivity()
    }

    private fun loadingNextActivity() {
        Handler().postDelayed({
            if (isExistsAndActiveSim(this)) {
                // run WebViewActivity
                val go = Intent(this@MainActivity, WebViewActivity::class.java)
                startActivity(go)
                finish()
            } else {
                // run SceneActivity
                val go = Intent(this@MainActivity, SceneActivity::class.java)
                startActivity(go)
                finish()
            }
        }, 3 * 1000.toLong())
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
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