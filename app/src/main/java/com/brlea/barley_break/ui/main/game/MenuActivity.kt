package com.brlea.barley_break.ui.main.game

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.GridLayoutManager
import com.brlea.barley_break.R
import com.brlea.barley_break.ui.dialog.InfoDialogFragment
import com.brlea.barley_break.ui.main.webview.WebViewActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.activity_scene.*
import kotlinx.android.synthetic.main.control_panel_menu.*
import kotlinx.android.synthetic.main.control_panel_menu.view.*

class MenuActivity : AppCompatActivity() {
    private var isMusicOn = true
    private lateinit var tileAdapter: TileAdapter
    private lateinit var policyLink: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        controlPanel()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun controlPanel() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        exitButton.setOnClickListener {
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    finish()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })

            exitButton.startAnimation(animation)
        }

        infoButton.setOnClickListener {
            infoButton.startAnimation(animation)
            setRemoteLink()
        }

        toggleButton.setOnClickListener {
            isMusicOn = if (isMusicOn) {
                toggleButton.startAnimation(animation)
                false
            } else {
                toggleButton.startAnimation(animation)
                true
            }
            tileAdapter.updateMusicState(isMusicOn)
        }

        playButton.setOnClickListener{
            playButton.startAnimation(animation)
            loadingNextActivity()
        }
    }

    private fun loadingNextActivity() {
        val user = username_layout.text.toString()
        Handler().postDelayed({
            // run SceneActivity
            val go = Intent(this@MenuActivity, SceneActivity::class.java)
            go.putExtra("username", user)
            startActivity(go)
            finish()
        }, 3 * 1000.toLong())
    }

    private fun setRemoteLink() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.fetch().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                remoteConfig.activate().addOnSuccessListener {
                    policyLink = remoteConfig.getString("policy_link")
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(policyLink))
                    startActivity(intent)
                }
            }
        }
    }
}