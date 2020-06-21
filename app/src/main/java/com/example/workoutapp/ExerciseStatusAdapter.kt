package com.example.workoutapp

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.exercise_status.view.*

class ExerciseStatusAdapter(
    private val context: Context,
    private val items: ArrayList<ExerciseModel>
) :
    RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.exercise_status,
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvItem.text = item.id.toString()

        if (item.isSelected) {
            holder.tvItem.setBackgroundResource(R.drawable.exercise_status_selected)
            holder.tvItem.setTextColor(Color.BLACK)
        } else if (item.isCompleted) {
            holder.tvItem.setBackgroundResource(R.drawable.item_circular_accent_background)
            holder.tvItem.setTextColor(Color.WHITE)
        } else {
            holder.tvItem.setBackgroundResource(R.drawable.exercise_status_background)
            holder.tvItem.setTextColor(Color.BLACK)
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItem: TextView = view.tv_item

    }
}

