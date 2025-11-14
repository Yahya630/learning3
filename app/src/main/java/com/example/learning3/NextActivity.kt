package com.example.learning3

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NextActivity : AppCompatActivity() {
    private lateinit var letterImage: ImageView
    private lateinit var soundButton: ImageButton
    private lateinit var clearButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var drawingView: DrawingView

    private val letters = listOf(
        Pair(R.drawable.fcba, R.raw.fcba),
        Pair(R.drawable.fcbb, R.raw.fcbb),
        Pair(R.drawable.fcbc, R.raw.fcbc),
        Pair(R.drawable.fcbd, R.raw.fcbd),
        Pair(R.drawable.fcbe, R.raw.fcbe),
        Pair(R.drawable.fcbf, R.raw.fcbf),
        Pair(R.drawable.fcbg, R.raw.fcbg),
        Pair(R.drawable.fcbh, R.raw.fcbh),
        Pair(R.drawable.fcbi, R.raw.fcbi),
        Pair(R.drawable.fcbj, R.raw.fcbj),
        Pair(R.drawable.fcbk, R.raw.fcbk),
        Pair(R.drawable.fcbl, R.raw.fcbl),
        Pair(R.drawable.fcbm, R.raw.fcbm),
        Pair(R.drawable.fcbn, R.raw.fcbn),
        Pair(R.drawable.fcbo, R.raw.fcbo),
        Pair(R.drawable.fcbp, R.raw.fcbp),
        Pair(R.drawable.fcbq, R.raw.fcbq),
        Pair(R.drawable.fcbr, R.raw.fcbr),
        Pair(R.drawable.fcbs, R.raw.fcbs),
        Pair(R.drawable.fcbt, R.raw.fcbt)
    )

    private var currentIndex = 0
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)

        letterImage = findViewById(R.id.letterImage)
        soundButton = findViewById(R.id.playSoundButton)
        clearButton = findViewById(R.id.clearButton)
        backButton = findViewById(R.id.backButton)
        nextButton = findViewById(R.id.nextButton)
        prevButton = findViewById(R.id.prevButton)

        val drawingContainer: FrameLayout = findViewById(R.id.drawingContainer)
        drawingView = DrawingView(this)
        drawingContainer.addView(
            drawingView,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        showLetter()

        nextButton.setOnClickListener { goNext() }
        prevButton.setOnClickListener { goPrev() }
        soundButton.setOnClickListener { playSound() }
        clearButton.setOnClickListener { drawingView.clearDrawing() }
        backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun goNext() {
        if (currentIndex < letters.size - 1) {
            currentIndex++
            showLetter()
        } else {
            Toast.makeText(this, "وصلت لآخر حرف", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goPrev() {
        if (currentIndex > 0) {
            currentIndex--
            showLetter()
        } else {
            Toast.makeText(this, "هذا أول حرف", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLetter() {
        val (imageRes, _) = letters[currentIndex]
        letterImage.setImageResource(imageRes)
        drawingView.clearDrawing()
        playSound()
    }

    private fun playSound() {
        mediaPlayer?.release()
        val (_, soundRes) = letters[currentIndex]
        mediaPlayer = MediaPlayer.create(this, soundRes).apply { start() }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}