package com.brlea.barley_break.ui.main

import android.media.MediaPlayer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.brlea.barley_break.R
import com.brlea.barley_break.ui.dialog.StartDialogFragment
import com.brlea.barley_break.ui.dialog.VictoryDialogFragment
import java.util.*
import kotlin.math.abs

class TileAdapter(
    private val recyclerView: RecyclerView,
    private val tileList: MutableList<Int>,
    private val moveCounter: TextView,
    private val timeTitle: TextView,
    private val moveListener: MoveListener,
    private val dialogFragmentDismissListener: StartDialogFragment.DialogFragmentDismissListener
) : RecyclerView.Adapter<TileAdapter.ViewHolder>() {

    private var emptyPosition: Int = tileList.size - 1
    private var moves = 0
    private var startTime: Long = 0 // Initialize the startTime property
    private val correctTilePositions: List<Int> = listOf(
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
    ) // Expected positions of tiles
    private var mediaPlayer: MediaPlayer = MediaPlayer.create(recyclerView.context, R.raw.stones)
    private var isMusicOn = true
    fun setStartTime(startTime: Long) {
        this.startTime = startTime
    }

    fun updateMusicState(isMusicOn: Boolean) {
        this.isMusicOn = isMusicOn
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tileList[position])
        moveCounter.text = moves.toString()
    }

    override fun getItemCount(): Int {
        return tileList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imagePart)
        private val handler = Handler()
        private val updateTimeRunnable = object : Runnable {
            override fun run() {
                (itemView.context as SceneActivity).updateElapsedTime(startTime)
                handler.postDelayed(this, 1000)
            }
        }

        init {
            handler.postDelayed(updateTimeRunnable, 1000)
        }

        fun bind(tileImageResource: Int) {
            imageView.setImageResource(tileImageResource)
            itemView.setOnClickListener {
                if (canMove(bindingAdapterPosition)) {
                    moves++
                    moveCounter.text = moves.toString()
                    swapTiles(bindingAdapterPosition, emptyPosition)
                    notifyItemChanged(bindingAdapterPosition)
                    notifyItemChanged(emptyPosition)
                    emptyPosition = bindingAdapterPosition

                    // Notify the activity of the move
                    moveListener.onMoveMade(moves)
                    animateTitleWithTimeAndMoves()

                    if (isMusicOn) {
                        mediaPlayer.start()
                    }
                }
            }
        }

        fun recycle() {
            handler.removeCallbacks(updateTimeRunnable)
        }

        private fun animateTitleWithTimeAndMoves() {
            // Animate the title time view
            timeTitle.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(500)
                .setInterpolator(OvershootInterpolator())
                .withEndAction {
                    // After scaling up, animate back to the original size
                    timeTitle.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .start()
                }
                .start()

            // Animate the move count view
            moveCounter.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(500)
                .setInterpolator(OvershootInterpolator())
                .withEndAction {
                    // After scaling up, animate back to the original size
                    moveCounter.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .start()
                }
                .start()
        }
    }

    init {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
            0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.bindingAdapterPosition
                val toPosition = target.bindingAdapterPosition
                if (canMove(fromPosition) && canMove(toPosition)) {
                    swapTiles(fromPosition, toPosition)
                    emptyPosition = toPosition
                }
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun canMove(clickedPosition: Int): Boolean {
        val gridSize = 4
        val clickedRow = clickedPosition / gridSize
        val clickedCol = clickedPosition % gridSize
        val emptyRow = emptyPosition / gridSize
        val emptyCol = emptyPosition % gridSize

        // Check if the clicked position is adjacent to the empty position
        return (abs(clickedRow - emptyRow) + abs(clickedCol - emptyCol) == 1)
    }

    private fun swapTiles(fromPosition: Int, toPosition: Int) {
        Collections.swap(tileList, fromPosition, toPosition)
        // update elements in RecyclerView
        notifyItemChanged(fromPosition)
        notifyItemChanged(toPosition)

        if (checkPuzzleCompletion()) {
            val victoryDialog = VictoryDialogFragment()
            victoryDialog.setDialogFragmentDismissListener(dialogFragmentDismissListener as SceneActivity)
            victoryDialog.show(
                (recyclerView.context as AppCompatActivity).supportFragmentManager,
                "victory_dialog"
            )
        }
    }

    private fun checkPuzzleCompletion(): Boolean {
        for (i in 0 until tileList.size) {
            val currentTile = tileList[i]
            val correctTilePosition = correctTilePositions.indexOf(currentTile)
            if (i != correctTilePosition) {
                return false // At least one tile is not in the correct position
            }
        }
        return true // All tiles are in the correct position
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.recycle()
    }
}