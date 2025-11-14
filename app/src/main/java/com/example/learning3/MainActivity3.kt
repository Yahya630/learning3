package com.example.learning3

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.progressindicator.LinearProgressIndicator

class MainActivity3 : AppCompatActivity() {
    private lateinit var progressBar: LinearProgressIndicator
    private lateinit var questionImage: ImageView
    private lateinit var optionsLayout: LinearLayout
    private var mediaPlayer: MediaPlayer? = null

    private var currentLevel = 0
    private val usedQuestions = mutableListOf<Question>()
    private val totalQuestions = 6
    private var currentQuestion: Question? = null

    private val questions = listOf(
        Question(
            questionImage = R.drawable.imagea,
            optionImages = listOf(R.drawable.image8, R.drawable.image7, R.drawable.image5),
            optionWords = listOf("جمل", "تمساح", "أسد"),
            optionLetters = listOf("ج", "ت", "أ"),
            correctAnswerIndex = 2,
            soundResId = R.raw.y1
        ),
        Question(
            questionImage = R.drawable.imageb,
            optionImages = listOf(R.drawable.image6, R.drawable.image17, R.drawable.image5),
            optionWords = listOf("بطة", "دب", "أسد"),
            optionLetters = listOf("ب", "د", "أ"),
            correctAnswerIndex = 0,
            soundResId = R.raw.y2
        ),
        Question(
            questionImage = R.drawable.imagec,
            optionImages = listOf(R.drawable.image7, R.drawable.image15, R.drawable.image10),
            optionWords = listOf("تمساح", "خاروف", "ضفدع"),
            optionLetters = listOf("ت", "خ", "ض"),
            correctAnswerIndex = 0,
            soundResId = R.raw.y3
        ),
        Question(
            questionImage = R.drawable.imaged,
            optionImages = listOf(R.drawable.image11, R.drawable.image12, R.drawable.image18),
            optionWords = listOf("فيل", "راكون", "ثعلب"),
            optionLetters = listOf("ف", "ر", "ث"),
            correctAnswerIndex = 2,
            soundResId = R.raw.y4
        ),
        Question(
            questionImage = R.drawable.imagef,
            optionImages = listOf(R.drawable.image14, R.drawable.image15, R.drawable.image8),
            optionWords = listOf("ظبي", "خاروف", "جمل"),
            optionLetters = listOf("ظ", "خ", "ج"),
            correctAnswerIndex = 2,
            soundResId = R.raw.y5
        ),
        Question(
            questionImage = R.drawable.imagey,
            optionImages = listOf(R.drawable.image17, R.drawable.image20, R.drawable.image19),
            optionWords = listOf("دب", "كتكوت", "صقر"),
            optionLetters = listOf("د", "ك", "ص"),
            correctAnswerIndex = 0,
            soundResId = R.raw.y8
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        progressBar = findViewById(R.id.progressBar)
        questionImage = findViewById(R.id.questionImage)
        optionsLayout = findViewById(R.id.optionsLayout)

        progressBar.max = 100

        loadQuestion()
    }

    private fun loadQuestion() {
        if (currentLevel >= totalQuestions) {
            Toast.makeText(this, "🎉 أحسنت! انتهيت من حل الأسئلة", Toast.LENGTH_LONG).show()
            Handler(Looper.getMainLooper()).postDelayed({ finish() }, 1500)
            return
        }

        progressBar.setProgressCompat(0, true)

        val remaining = questions.filter { it !in usedQuestions }
        if (remaining.isEmpty()) return
        currentQuestion = remaining.random().also { usedQuestions.add(it) }

        val q = currentQuestion!!
        q.questionImage?.let { questionImage.setImageResource(it) }
        optionsLayout.removeAllViews()

        for (i in 0 until 3) {
            val optionView = layoutInflater.inflate(R.layout.option_item, null) as LinearLayout
            val image = optionView.findViewById<ImageView>(R.id.optionImage)
            val word = optionView.findViewById<TextView>(R.id.optionWord)
            val letter = optionView.findViewById<TextView>(R.id.optionLetter)

            q.optionImages?.get(i)?.let { image.setImageResource(it) }
            word.text = q.optionWords?.get(i)
            letter.text = q.optionLetters?.get(i)

            optionView.setOnClickListener { checkAnswer(i) }

            letter.setOnClickListener {
                if (i == q.correctAnswerIndex) {
                    q.soundResId?.let { playSound(it) }
                } else {
                    playSound(R.raw.wrong_sound)
                }
            }

            optionsLayout.addView(optionView)
        }

        q.soundResId?.let { playSound(it) }
    }

    private fun checkAnswer(selectedIndex: Int) {
        val q = currentQuestion ?: return

        if (selectedIndex == q.correctAnswerIndex) {
            playSound(R.raw.correct_sound)
            currentLevel++

            val progressPercentage = (currentLevel * 100) / totalQuestions
            progressBar.setProgressCompat(progressPercentage, true)

            Handler(Looper.getMainLooper()).postDelayed({ loadQuestion() }, 1500)
        } else {
            playSound(R.raw.wrong_sound)
        }
    }

    private fun playSound(resId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, resId)
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}