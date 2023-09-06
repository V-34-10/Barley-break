package com.brlea.barley_break.ui.main.game

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.brlea.barley_break.R
import com.brlea.barley_break.utils.RemoteConfigUtils
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.control_panel_menu.*

class MenuActivity : AppCompatActivity() {
    private var isMusicOn = true
    private var isActivityRun = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        controlPanel()
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
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
            RemoteConfigUtils.openPolicyLink(this)
        }

        toggleButton.setOnClickListener {
            isMusicOn = if (isMusicOn) {
                toggleButton.startAnimation(animation)
                false
            } else {
                toggleButton.startAnimation(animation)
                true
            }
        }

        playButton.setOnClickListener {
            if (!isActivityRun) {
                playButton.startAnimation(animation)
                isActivityRun = true
                loadingNextActivity()
            }
        }
    }

    private fun loadingNextActivity() {
        val user = username_layout.text.toString()
        // run SceneActivity
        val go = Intent(this@MenuActivity, SceneActivity::class.java)
        go.putExtra("username", user)
        go.putExtra("isMusic", isMusicOn)
        startActivity(go)
        finish()
    }
}