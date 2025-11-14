package com.example.learning3

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class languagaaa : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_languagaaa)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonMap = mapOf(
            R.id.letterA to R.raw.d1,
            R.id.letterB to R.raw.d2,
            R.id.letterT to R.raw.d3,
            R.id.letterTh to R.raw.d4,
            R.id.letterJ to R.raw.d5,
            R.id.letterH to R.raw.d6,
            R.id.letterKh to R.raw.d7,
            R.id.letterD to R.raw.d8,
            R.id.letterThal to R.raw.d9,
            R.id.letterR to R.raw.d10,
            R.id.letterZ to R.raw.d11,
            R.id.letterS to R.raw.d12,
            R.id.letterSh to R.raw.d13,
            R.id.letterSad to R.raw.d14,
            R.id.letterDad to R.raw.d15,
            R.id.letterTaa to R.raw.d16,
            R.id.letterZaa to R.raw.d17,
            R.id.letterAin to R.raw.d18,
            R.id.letterGhain to R.raw.d19,
            R.id.letterF to R.raw.d20,
            R.id.letterQ to R.raw.d21,
            R.id.letterK to R.raw.d22,
            R.id.letterL to R.raw.d23,
            R.id.letterM to R.raw.d24,
            R.id.letterN to R.raw.d25,
            R.id.letterHaa to R.raw.d26
        )

        for ((buttonId, soundResId) in buttonMap) {
            val button = findViewById<Button?>(buttonId)
            if (button != null) {
                button.setOnClickListener {
                    playSound(soundResId)
                }
            } else {
                println("Button with ID $buttonId not found in layout!")
            }
        }
    }

    private fun playSound(soundResId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, soundResId)
        mediaPlayer?.setOnCompletionListener {
            it.release()
        }
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}