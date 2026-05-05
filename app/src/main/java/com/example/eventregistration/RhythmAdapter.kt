package com.example.eventregistration

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

private const val TYPE_HEADER = 0
private const val TYPE_VARIATION = 1

sealed class ListItem {
    data class Header(val rhythm: Rhythm, val expanded: Boolean) : ListItem()
    data class VariationItem(val rhythmName: String, val variation: Variation) : ListItem()
}

class RhythmAdapter(
    private val rhythms: List<Rhythm>,
    private val onVariationSelected: (rhythmName: String, variation: Variation) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val expandedRhythms = mutableSetOf<String>()
    private var playingKey: String? = null // "rhythmName/variationLabel"
    private val items = mutableListOf<ListItem>()

    init { rebuildItems() }

    fun setPlaying(key: String?) {
        playingKey = key
        notifyDataSetChanged()
    }

    private fun rebuildItems() {
        items.clear()
        for (rhythm in rhythms) {
            val expanded = rhythm.name in expandedRhythms
            items.add(ListItem.Header(rhythm, expanded))
            if (expanded) {
                for (variation in rhythm.variations) {
                    items.add(ListItem.VariationItem(rhythm.name, variation))
                }
            }
        }
    }

    private fun toggleExpand(rhythmName: String) {
        if (rhythmName in expandedRhythms) expandedRhythms.remove(rhythmName)
        else expandedRhythms.add(rhythmName)
        rebuildItems()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int) = when (items[position]) {
        is ListItem.Header -> TYPE_HEADER
        is ListItem.VariationItem -> TYPE_VARIATION
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            HeaderViewHolder(inflater.inflate(R.layout.item_rhythm_header, parent, false))
        } else {
            VariationViewHolder(inflater.inflate(R.layout.item_rhythm_variation, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ListItem.Header -> (holder as HeaderViewHolder).bind(item)
            is ListItem.VariationItem -> (holder as VariationViewHolder).bind(item)
        }
    }

    override fun getItemCount() = items.size

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.rhythmName)
        private val arrow: ImageView = view.findViewById(R.id.arrowIcon)

        fun bind(item: ListItem.Header) {
            name.text = item.rhythm.name
            arrow.setImageResource(
                if (item.expanded) android.R.drawable.arrow_up_float
                else android.R.drawable.arrow_down_float
            )
            arrow.visibility = if (item.rhythm.hasSound) View.VISIBLE else View.INVISIBLE
            itemView.setOnClickListener {
                if (item.rhythm.hasSound) toggleExpand(item.rhythm.name)
            }
        }
    }

    inner class VariationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val label: TextView = view.findViewById(R.id.variationLabel)
        private val playButton: ImageButton = view.findViewById(R.id.playButton)

        fun bind(item: ListItem.VariationItem) {
            label.text = item.variation.label
            val key = "${item.rhythmName}/${item.variation.label}"
            val isPlaying = key == playingKey
            playButton.setImageResource(
                if (isPlaying) android.R.drawable.ic_media_pause
                else android.R.drawable.ic_media_play
            )
            val clickAction = { onVariationSelected(item.rhythmName, item.variation) }
            itemView.setOnClickListener { clickAction() }
            playButton.setOnClickListener { clickAction() }
        }
    }
}
