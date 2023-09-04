package com.brlea.barley_break.ui.main.game

import android.media.MediaPlayer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.brlea.barley_break.R
import com.brlea.barley_break.ui.dialog.VictoryDialogFragment
import java.util.*
import kotlin.math.abs

class TileAdapter(
    private val recyclerView: RecyclerView,
    private val tileList: MutableList<Int>,
    private val dialogFragmentDismissListener: SceneActivity? = null
) : RecyclerView.Adapter<TileAdapter.ViewHolder>() {
    private var emptyPosition: Int = tileList.size - 1
    private var startTime: Long = 0 // Initialize the startTime property
    private val correctTilePositions: List<Int> = listOf(
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
        R.drawable.cub_15,
        R.drawable.as_16
    ) // Expected positions of tiles
    private var isMusicOn = true
    fun setStartTime(startTime: Long) {
        this.startTime = startTime
    }

    fun updateMusicState(isMusicOn: Boolean) {
        this.isMusicOn = isMusicOn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tileList[position])
    }

    override fun getItemCount(): Int {
        return tileList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imagePart)
        private val handler = Handler()
        private val playMusicHandler = Handler()
        private val updateTimeRunnable = object : Runnable {
            override fun run() {
                (itemView.context as SceneActivity).updateElapsedTime(startTime)
                handler.postDelayed(this, 100)
            }
        }

        init {
            handler.postDelayed(updateTimeRunnable, 100)
        }

        fun bind(tileImageResource: Int) {
            imageView.setImageResource(tileImageResource)
            itemView.setOnClickListener {
                if (canMove(bindingAdapterPosition)) {
                    swapTiles(bindingAdapterPosition, emptyPosition)
                    notifyItemChanged(bindingAdapterPosition)
                    notifyItemChanged(emptyPosition)
                    emptyPosition = bindingAdapterPosition

                    // Use Handler to delay the start of music playback
                    playMusicHandler.postDelayed({
                        if (isMusicOn) {
                            val newMediaPlayer = MediaPlayer.create(
                                recyclerView.context,
                                R.raw.stones
                            )
                            newMediaPlayer.setOnCompletionListener {
                                it.release() // Release the MediaPlayer once playback is complete
                            }
                            newMediaPlayer.start()
                        }
                    }, 100) // Adjust the delay time as needed
                }
            }
        }

        fun recycle() {
            handler.removeCallbacks(updateTimeRunnable)
            playMusicHandler.removeCallbacksAndMessages(null)
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