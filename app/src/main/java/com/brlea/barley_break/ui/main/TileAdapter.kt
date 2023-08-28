package com.brlea.barley_break.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.brlea.barley_break.R
import java.util.*
import kotlin.math.abs

class TileAdapter(
    private val recyclerView: RecyclerView,
    private val tileList: MutableList<Int>,
    private val moveCounter: TextView,
    private val moveListener: MoveListener
) : RecyclerView.Adapter<TileAdapter.ViewHolder>() {

    private var emptyPosition: Int = tileList.size - 1
    private var moves = 0
    private var startTime: Long = 0 // Initialize the startTime property

    fun setStartTime(startTime: Long) {
        this.startTime = startTime
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

        fun bind(tileImageResource: Int) {
            imageView.setImageResource(tileImageResource)
            //
            itemView.setOnClickListener {
                if (canMove(position)) {
                    moves++
                    moveCounter.text = moves.toString()
                    swapTiles(position, emptyPosition)
                    notifyItemChanged(position)
                    notifyItemChanged(emptyPosition)
                    emptyPosition = position

                    // Update the elapsed time after each click
                    (itemView.context as SceneActivity).updateElapsedTime(startTime)
                    // Notify the activity of the move
                    moveListener.onMoveMade(moves)

                }
            }

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
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
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

        return (clickedRow == emptyRow && abs(clickedCol - emptyCol) == 1) ||
                (clickedCol == emptyCol && abs(clickedRow - emptyRow) == 1)
    }

    private fun swapTiles(fromPosition: Int, toPosition: Int) {
        Collections.swap(tileList, fromPosition, toPosition)
        // update elements in RecyclerView
        notifyItemChanged(fromPosition)
        notifyItemChanged(toPosition)
    }
}