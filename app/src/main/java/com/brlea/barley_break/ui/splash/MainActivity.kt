package com.brlea.barley_break.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.brlea.barley_break.R
import com.brlea.barley_break.ui.main.SceneActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadingNextActivity()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    private fun loadingNextActivity() {
        Handler().postDelayed({
            val go = Intent(this@MainActivity, SceneActivity::class.java)
            startActivity(go)
            finish()
        }, 3 * 1000.toLong())
    }
}