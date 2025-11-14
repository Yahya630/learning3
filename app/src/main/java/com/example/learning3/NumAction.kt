package com.example.learning3

import android.media.MediaPlayer
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NumAction : AppCompatActivity() {
    private val numberSounds = mapOf(
        R.id.btnNumber1 to R.raw.one,
        R.id.btnNumber2 to R.raw.two,
        R.id.btnNumber3 to R.raw.three,
        R.id.btnNumber4 to R.raw.four,
        R.id.btnNumber5 to R.raw.five,
        R.id.btnNumber6 to R.raw.six,
        R.id.btnNumber7 to R.raw.seven,
        R.id.btnNumber8 to R.raw.eight,
        R.id.btnNumber9 to R.raw.nine,
        R.id.btnNumber10 to R.raw.ten
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_num_action)

       

        numberSounds.keys.forEach { buttonId ->
            val button = findViewById<Button>(buttonId)
            button.setOnClickListener {
                val soundId = numberSounds[buttonId]!!
                playSound(soundId)
                applyAnimation(button)
            }
        }
    }

    private fun playSound(soundResId: Int) {
        val mediaPlayer = MediaPlayer.create(this, soundResId)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener { it.release() }
    }

    private fun applyAnimation(button: android.view.View) {
        val animation = ScaleAnimation(
            1f, 1.2f, 1f, 1.2f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        animation.duration = 100
        animation.repeatMode = Animation.REVERSE
        animation.repeatCount = 1
        button.startAnimation(animation)
    }
}