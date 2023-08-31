package com.brlea.barley_break.ui.main.webview

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
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
        WebViewShow = findViewById(R.id.WebViewShow)
        WebViewShow.webViewClient = WebViewClient()
        WebViewShow.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }
        WebViewShow.loadUrl(mainLink)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.getAction() === KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (WebViewShow.canGoBack()) {
                        WebViewShow.goBack()
                    } else {
                        finish()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun setRemoteLink() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.fetch().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                remoteConfig.activate()
                mainLink = remoteConfig.getString("mainLink")
                initWebView()
            }
        }
    }
}