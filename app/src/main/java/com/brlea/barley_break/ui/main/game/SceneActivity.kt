package com.brlea.barley_break.ui.main.game

import android.content.pm.PackageManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.brlea.barley_break.R
import com.brlea.barley_break.ui.dialog.InfoDialogFragment
import com.brlea.barley_break.ui.dialog.StartDialogFragment
import com.brlea.barley_break.ui.dialog.VictoryDialogFragment
import kotlinx.android.synthetic.main.activity_scene.*
import java.util.*
import java.util.concurrent.TimeUnit

class SceneActivity : AppCompatActivity(), MoveListener,
    StartDialogFragment.DialogFragmentDismissListener,
    VictoryDialogFragment.DialogFragmentDismissListener {

    private var moveCount = 0
    private lateinit var mediaPlayer: MediaPlayer
    private val musicTracks =
        listOf(R.raw.asia, R.raw.dream, R.raw.wow, R.raw.happylife, R.raw.electrodoodle)
    private var currentMusicIndex = 0
    private var tileImagesOriginal = mutableListOf(
        R.drawable.as_1,
        R.drawable.as_2,
        R.drawable.as_3,
        R.drawable.as_4,
        R.drawable.as_5,
        R.drawable.as_6,
        R.drawable.as_7,
        R.drawable.as_8,
        R.drawable.as_9,
        R.drawable.as_10,
        R.drawable.as_11,
        R.drawable.as_12,
        R.drawable.as_13,
        R.drawable.as_14,
        R.drawable.as_15
    )
    private var tileImagesShuffle = mutableListOf(tileImagesOriginal)
    private var isMusicOn = true
    private lateinit var tileAdapter: TileAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scene)
        startDialog()
        controlPanel()
        initNotification()
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

        refreshButton.setOnClickListener {
            refreshButton.startAnimation(animation)
            onRestartClicked()
        }

        infoButton.setOnClickListener {
            infoButton.startAnimation(animation)
            val infoGameDialog = InfoDialogFragment()
            infoGameDialog.show(supportFragmentManager, "info_game_dialog")
        }

        finishedButton.setOnClickListener {
            finishedButton.startAnimation(animation)
            tileAdapter = TileAdapter(
                sceneGame,
                (tileImagesOriginal + R.drawable.as_16) as MutableList<Int>,
                move_value,
                this,
                this
            )
            tileAdapter.setStartTime(System.currentTimeMillis()) // Set the start time

            sceneGame.apply {
                layoutManager = GridLayoutManager(this@SceneActivity, 4)
                this.adapter = tileAdapter
            }
        }

        toggleButton.setOnClickListener {
            isMusicOn = if (isMusicOn) {
                toggleButton.startAnimation(animation)
                /*mediaPlayer.stop()
                mediaPlayer = MediaPlayer.create(this, getRandomMusicTrack())*/
                toggleButton.setIconResource(R.drawable.ic_baseline_music_off_24)
                false
            } else {
                toggleButton.startAnimation(animation)
                //mediaPlayer.start()
                toggleButton.setIconResource(R.drawable.ic_baseline_music_note_24)
                true
            }
            tileAdapter.updateMusicState(isMusicOn)
        }
    }

    private fun initRecycler() {
        tileImagesShuffle.shuffle()
        tileAdapter = TileAdapter(
            sceneGame,
            (tileImagesShuffle + R.drawable.as_16) as MutableList<Int>,
            move_value,
            this,
            this
        )
        tileAdapter.setStartTime(System.currentTimeMillis()) // Set the start time

        sceneGame.apply {
            layoutManager = GridLayoutManager(this@SceneActivity, 4)
            //addItemDecoration(GridSpacingItemDecoration(4, 8)) // Add separator
            this.adapter = tileAdapter
        }
    }

    private fun initMedia() {
        // Initialize the MediaPlayer with the background music
        mediaPlayer = MediaPlayer.create(this, getRandomMusicTrack())
        mediaPlayer.isLooping = true
        mediaPlayer.start()
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
        //initMedia()
    }

    private fun getRandomMusicTrack(): Int {
        val randomIndex = Random().nextInt(musicTracks.size)
        currentMusicIndex = randomIndex
        return musicTracks[randomIndex]
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
        val formattedMoveTitle =
            getString(R.string.move_title_format, getString(R.string.move), moveCount)
        move_value.text = formattedMoveTitle
    }

    override fun onDestroy() {
        super.onDestroy()
        //mediaPlayer.release()
    }

    override fun onRestartClicked() {
        // Release the media player
        //mediaPlayer.release()

        // Reset time, move, toggleButton
        moveCount = 0
        val formattedMoveCount = getString(R.string.move_format, moveCount)
        move_value.text = formattedMoveCount
        time_value.text = getString(R.string.time)
        toggleButton.setIconResource(R.drawable.ic_baseline_music_note_24)
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
        // Перевіряємо, чи маємо ми дозвіл на сповіщення
        return ContextCompat.checkSelfPermission(
            this,
            "com.google.android.c2dm.permission.RECEIVE"
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestNotificationPermission() {
        val notificationPermissionCode = 123
        // Запитуємо дозвіл на сповіщення
        ActivityCompat.requestPermissions(
            this,
            arrayOf("com.google.android.c2dm.permission.RECEIVE"),
            notificationPermissionCode
        )
    }
}