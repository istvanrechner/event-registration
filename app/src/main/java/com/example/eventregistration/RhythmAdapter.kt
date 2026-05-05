package com.example.eventregistration

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RhythmAdapter(
    private val rhythms: List<Rhythm>,
    private val onSelect: (Rhythm) -> Unit
) : RecyclerView.Adapter<RhythmAdapter.ViewHolder>() {

    private var playingName: String? = null

    fun setPlaying(name: String?) {
        playingName = name
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.rhythmName)
        val playButton: ImageButton = view.findViewById(R.id.playButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rhythm, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rhythm = rhythms[position]
        holder.name.text = rhythm.name

        val isPlaying = rhythm.name == playingName
        holder.playButton.setImageResource(
            if (isPlaying) android.R.drawable.ic_media_pause
            else android.R.drawable.ic_media_play
        )
        holder.playButton.alpha = if (rhythm.soundRes != null) 1f else 0.4f

        holder.itemView.setOnClickListener { onSelect(rhythm) }
        holder.playButton.setOnClickListener { onSelect(rhythm) }
    }

    override fun getItemCount() = rhythms.size
}
