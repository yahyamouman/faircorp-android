package com.faircorp.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.faircorp.OnDoorSelectedListener
import com.faircorp.R
import com.faircorp.model.DoorDto

class DoorsAdapter(val listener: OnDoorSelectedListener) : RecyclerView.Adapter<DoorsAdapter.DoorViewHolder>() { // (1)

    inner class DoorViewHolder(view: View) : RecyclerView.ViewHolder(view) { // (2)
        val name: TextView = view.findViewById(R.id.door_name)
        val room: TextView = view.findViewById(R.id.txt_door_room)
        val status: TextView = view.findViewById(R.id.txt_door_status)
    }

    private val items = mutableListOf<DoorDto>() // (3)

    fun update(doors: List<DoorDto>) {  // (4)
        items.clear()
        items.addAll(doors)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size // (5)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoorViewHolder { // (6)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_doors_item, parent, false)
        return DoorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoorViewHolder, position: Int) {  // (7)
        val door = items[position]
        holder.apply {
            name.text = door.name
            status.text = door.doorStatus.toString()
            room.text = door.roomName
            itemView.setOnClickListener { listener.onDoorSelected(door.id) }
        }
    }

    override fun onViewRecycled(holder: DoorViewHolder) { // (2)
        super.onViewRecycled(holder)
        holder.apply {
            itemView.setOnClickListener(null)
        }

    }
}