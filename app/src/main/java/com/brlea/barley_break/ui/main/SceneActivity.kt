package com.brlea.barley_break.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brlea.barley_break.R
import com.brlea.barley_break.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.activity_scene.*
import java.util.concurrent.TimeUnit

class SceneActivity : AppCompatActivity(), MoveListener {

    private var moveCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scene)
        initRecycler()
    }

    private fun initRecycler() {
        val tileImages = mutableListOf(
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
            R.drawable.as_15,
            R.drawable.as_16
        )
        val adapter = TileAdapter(sceneGame, tileImages, move_title, this)
        adapter.setStartTime(System.currentTimeMillis()) // Set the start time

        sceneGame.apply {
            layoutManager = GridLayoutManager(this@SceneActivity, 4)
            addItemDecoration(GridSpacingItemDecoration(4, 8)) // Add separator
            this.adapter = adapter
        }
    }

    fun updateElapsedTime(startTime: Long) {
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - startTime

        val hours = TimeUnit.MILLISECONDS.toHours(elapsedTime)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60

        val timeString = String.format("Time: %02d:%02d:%02d", hours, minutes, seconds)
        time_title.text = timeString
    }

    override fun onMoveMade(moveCount: Int) {
        this.moveCount = moveCount
        val message = getString(R.string.move) + moveCount
        move_title.text = message
    }
}