package com.example.learning3

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.learning3.R

class page1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page1)

        setupAnimatedButton(R.id.btn_shahada, R.raw.shahada)
        setupAnimatedButton(R.id.btn_salah, R.raw.salah)
        setupAnimatedButton(R.id.btn_zakat, R.raw.zakat)
        setupAnimatedButton(R.id.btn_sawm, R.raw.sawm)
        setupAnimatedButton(R.id.btn_hajj, R.raw.hajj)
        setupAnimatedButton(R.id.btn_iman_allah, R.raw.iman_allah)
        setupAnimatedButton(R.id.btn_iman_angels, R.raw.iman_angels)
        setupAnimatedButton(R.id.btn_iman_books, R.raw.iman_books)
        setupAnimatedButton(R.id.btn_iman_prophets, R.raw.iman_prophets)
        setupAnimatedButton(R.id.btn_iman_day, R.raw.iman_day)
        setupAnimatedButton(R.id.btn_iman_qadar, R.raw.iman_qadar)
    }

    private fun setupAnimatedButton(buttonId: Int, soundResId: Int) {
        val button = findViewById<Button>(buttonId)
        button.setOnClickListener {
            playSound(soundResId)
            animateButton(button)
        }
    }

    private fun animateButton(button: Button) {
        val animation = AlphaAnimation(0.6f, 1.0f)
        animation.duration = 150
        button.startAnimation(animation)
    }

    private fun playSound(soundResId: Int) {
        val mediaPlayer = MediaPlayer.create(this, soundResId)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            it.release()
        }
    }
}
