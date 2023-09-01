package com.brlea.barley_break.ui.main.webview

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.brlea.barley_break.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class WebViewActivity : AppCompatActivity() {
    private lateinit var WebViewShow: WebView
    private var mainLink: String = "https://www.youtube.com"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        setRemoteLink()
    }

    private fun initWebView() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        WebViewShow = findViewById(R.id.WebViewShow)
        WebViewShow.webViewClient = WebViewClient()
        WebViewShow.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        WebViewShow.webViewClient = WebViewClient()
        WebViewShow.loadUrl(mainLink)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (WebViewShow.canGoBack()) {
                        WebViewShow.goBack()
                        return true
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // no close activity
    }

    private fun setRemoteLink() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.fetch().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                remoteConfig.activate().addOnSuccessListener {
                    mainLink = remoteConfig.getString("mainLink")
                    initWebView()
                }
            }
        }
    }
}