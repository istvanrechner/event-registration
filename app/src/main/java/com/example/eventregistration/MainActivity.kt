package com.example.eventregistration

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var playingKey: String? = null
    private var adapter: RhythmAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rhythms = listOf(
            Rhythm("Saidi", emptyList()),
            Rhythm("Baladi", emptyList()),
            Rhythm(
                "Karşilama", listOf(
                    Variation("All Variations", R.raw.karsilama_all_variations),
                    Variation("01", R.raw.karsilama_01),
                    Variation("02", R.raw.karsilama_02),
                    Variation("03", R.raw.karsilama_03),
                    Variation("04", R.raw.karsilama_04),
                    Variation("05", R.raw.karsilama_05),
                    Variation("Voice", R.raw.karsilama_voice),
                )
            )
        )

        adapter = RhythmAdapter(rhythms) { rhythmName, variation ->
            onVariationSelected(rhythmName, variation)
        }

        findViewById<RecyclerView>(R.id.rhythmList).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun onVariationSelected(rhythmName: String, variation: Variation) {
        val key = "$rhythmName/${variation.label}"

        if (playingKey == key) {
            stopPlayback()
            adapter?.setPlaying(null)
            return
        }

        stopPlayback()
        mediaPlayer = MediaPlayer.create(this, variation.soundRes!!).apply {
            isLooping = true
            start()
        }
        playingKey = key
        adapter?.setPlaying(key)
    }

    private fun stopPlayback() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        playingKey = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopPlayback()
    }
}
