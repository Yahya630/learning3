package com.example.learning3

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val buttonMap = mapOf(
            R.id.letterA to R.raw.y1,
            R.id.letterB to R.raw.y2,
            R.id.letterT to R.raw.y3,
            R.id.letterTh to R.raw.y4,
            R.id.letterJ to R.raw.y5,
            R.id.letterH to R.raw.y6,
            R.id.letterKh to R.raw.y7,
            R.id.letterD to R.raw.y8,
            R.id.letterThal to R.raw.y9,
            R.id.letterR to R.raw.y10,
            R.id.letterZ to R.raw.y11,
            R.id.letterS to R.raw.y12,
            R.id.letterSh to R.raw.y13,
            R.id.letterSad to R.raw.y14,
            R.id.letterDad to R.raw.y15,
            R.id.letterTaa to R.raw.y16,
            R.id.letterZaa to R.raw.y17,
            R.id.letterAin to R.raw.y18,
            R.id.letterGhain to R.raw.y19,
            R.id.letterF to R.raw.y20,
            R.id.letterQ to R.raw.y21,
            R.id.letterK to R.raw.y22,
            R.id.letterL to R.raw.y23,
            R.id.letterM to R.raw.y24,
            R.id.letterN to R.raw.y25,
            R.id.letterHaa to R.raw.y26,
            R.id.letterW to R.raw.y27,
            R.id.letterY to R.raw.y28
        )

        for ((buttonId, soundResId) in buttonMap) {
            val button = findViewById<Button>(buttonId)
            button.setOnClickListener {
                playSound(soundResId)
            }
        }
    }

    private fun playSound(soundResId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, soundResId)
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}