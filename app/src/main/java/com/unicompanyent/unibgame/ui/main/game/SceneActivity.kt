package com.unicompanyent.unibgame.ui.main.game

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.unicompanyent.unibgame.R
import com.unicompanyent.unibgame.ui.dialog.StartDialogFragment
import com.unicompanyent.unibgame.ui.dialog.VictoryDialogFragment
import com.unicompanyent.unibgame.utils.RemoteConfigUtils
import kotlinx.android.synthetic.main.activity_scene.*
import java.util.concurrent.TimeUnit

class SceneActivity : AppCompatActivity(), MoveListener,
    StartDialogFragment.DialogFragmentDismissListener,
    VictoryDialogFragment.DialogFragmentDismissListener {
    private var moveCount = 0
    private var tileImagesShuffle = mutableListOf(
        R.drawable.cub_1,
        R.drawable.cub_2,
        R.drawable.cub_3,
        R.drawable.cub_4,
        R.drawable.cub_5,
        R.drawable.cub_6,
        R.drawable.cub_7,
        R.drawable.cub_8,
        R.drawable.cub_9,
        R.drawable.cub_10,
        R.drawable.cub_11,
        R.drawable.cub_12,
        R.drawable.cub_13,
        R.drawable.cub_14,
        R.drawable.cub_15
    )
    private var isMusicOn = true
    private lateinit var tileAdapter: TileAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scene)
        startDialog()
        controlPanel()
        initNotification()
        getNameUser()

    }

    private fun getMusicOn() {
        val intentValue = intent.getBooleanExtra("isMusic", isMusicOn)
        isMusicOn = intentValue
        tileAdapter.updateMusicState(isMusicOn)
    }

    private fun controlPanel() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        exit_Button.setOnClickListener {
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    finish()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })

            exit_Button.startAnimation(animation)
        }

        refresh_Button.setOnClickListener {
            refresh_Button.startAnimation(animation)
            onRestartClicked()
        }

        info_Button.setOnClickListener {
            info_Button.startAnimation(animation)
            RemoteConfigUtils.openPolicyLink(this)
        }

        back_Button.setOnClickListener {
            val go = Intent(this@SceneActivity, MenuActivity::class.java)
            startActivity(go)
            finish()
        }

        toggle_Button.setOnClickListener {
            isMusicOn = if (isMusicOn) {
                toggle_Button.startAnimation(animation)
                false
            } else {
                toggle_Button.startAnimation(animation)
                true
            }
            tileAdapter.updateMusicState(isMusicOn)
        }
    }

    private fun getNameUser() {
        val username = intent.getStringExtra("username")
        user_name_nick.text = username ?: "USERNAME"
    }

    private fun initRecycler() {
        tileImagesShuffle.shuffle()
        tileAdapter = TileAdapter(
            sceneGame,
            (tileImagesShuffle + R.drawable.as_16) as MutableList<Int>,
            this
        )
        tileAdapter.setStartTime(System.currentTimeMillis()) // Set the start time

        sceneGame.apply {
            layoutManager = GridLayoutManager(this@SceneActivity, 4)
            //addItemDecoration(GridSpacingItemDecoration(4, 8)) // Add separator
            this.adapter = tileAdapter
        }
    }

    private fun startDialog() {
        // Show the Start Game dialog
        val startGameDialog = StartDialogFragment()
        startGameDialog.setDialogFragmentDismissListener(this)
        startGameDialog.show(supportFragmentManager, "start_game_dialog")
    }

    override fun onDialogDismissed() {
        // The dialog has been dismissed, you can start the main game session here
        initRecycler()
        getMusicOn()
    }

    fun updateElapsedTime(startTime: Long) {
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - startTime

        val hours = TimeUnit.MILLISECONDS.toHours(elapsedTime)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60

        val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        time_value.text = timeString
    }

    override fun onMoveMade(moveCount: Int) {
        this.moveCount = moveCount
    }

    override fun onRestartClicked() {
        // Reset time, toggleButton
        moveCount = 0
        isMusicOn = true
        tileAdapter.updateMusicState(isMusicOn)
        time_value.text = getString(R.string.time)
        // Reset adapter and recycler view
        onDialogDismissed()
    }

    private fun hideSystemUI() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Hide navigation bar and status bar
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        // Allow full panels to be displayed during user interaction
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the navigation bar when the activity is first shown
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    private fun initNotification() {
        // Check if we already have notification permission
        if (!isNotificationPermissionGranted()) {
            // If there is no permission, then we ask the user
            requestNotificationPermission()
        }
    }

    private fun isNotificationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            "com.google.android.c2dm.permission.RECEIVE"
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestNotificationPermission() {
        val notificationPermissionCode = 123
        ActivityCompat.requestPermissions(
            this,
            arrayOf("com.google.android.c2dm.permission.RECEIVE"),
            notificationPermissionCode
        )
    }
}