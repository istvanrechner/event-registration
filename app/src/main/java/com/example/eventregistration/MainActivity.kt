package com.example.eventregistration

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var playingRhythm: String? = null
    private var adapter: RhythmAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rhythms = listOf(
            Rhythm("Saidi", null),
            Rhythm("Baladi", null),
            Rhythm("Karşilama", R.raw.karsilama)
        )

        adapter = RhythmAdapter(rhythms) { rhythm ->
            onRhythmSelected(rhythm)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rhythmList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun onRhythmSelected(rhythm: Rhythm) {
        if (rhythm.soundRes == null) {
            Toast.makeText(this, "${rhythm.name}: coming soon", Toast.LENGTH_SHORT).show()
            return
        }

        if (playingRhythm == rhythm.name) {
            stopPlayback()
            adapter?.setPlaying(null)
            return
        }

        stopPlayback()
        mediaPlayer = MediaPlayer.create(this, rhythm.soundRes).apply {
            isLooping = true
            start()
        }
        playingRhythm = rhythm.name
        adapter?.setPlaying(rhythm.name)
    }

    private fun stopPlayback() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        playingRhythm = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopPlayback()
    }
}
