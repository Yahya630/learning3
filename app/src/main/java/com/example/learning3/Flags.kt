package com.example.learning3

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Flags : AppCompatActivity() {

    private lateinit var flagImage: ImageView
    private lateinit var option1: Button
    private lateinit var option2: Button
    private lateinit var option3: Button
    private lateinit var scoreText: TextView
    private lateinit var gameOverLayout: View
    private lateinit var finalScoreText: TextView
    private lateinit var playAgainButton: Button

    private lateinit var correctSound: MediaPlayer
    private lateinit var wrongSound: MediaPlayer

    private val countries = listOf(
        "مصر"      to R.drawable.flag_egypt,
        "السعودية" to R.drawable.flag_saudi,
        "الإمارات"  to R.drawable.flag_uae,
        "الجزائر"   to R.drawable.flag_algeria,
        "المغرب"    to R.drawable.flag_morocco,
        "تونس"      to R.drawable.flag_tunisia,
        "العراق"    to R.drawable.flag_iraq,
        "الأردن"    to R.drawable.flag_jordan,
        "لبنان"     to R.drawable.flag_lebanon,
        "سوريا"     to R.drawable.flag_syria,
        "فلسطين"    to R.drawable.flag_palestine,
        "الكويت"    to R.drawable.flag_kuwait,
        "قطر"       to R.drawable.flag_qatar,
        "البحرين"   to R.drawable.flag_bahrain,
        "عمان"      to R.drawable.flag_oman,
        "اليمن"     to R.drawable.flag_yemen,
        "ليبيا"     to R.drawable.flag_libya,
        "السودان"   to R.drawable.flag_sudan,
        "موريتانيا" to R.drawable.flag_mauritania,
        "الصومال"   to R.drawable.flag_somalia,
        "جيبوتي"    to R.drawable.flag_djibouti,
        "جزر القمر" to R.drawable.flag_comoros
    )

    private var shuffledCountries = countries.shuffled()
    private var currentIndex = 0
    private var score = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_flags)

        flagImage       = findViewById(R.id.flagimage)
        option1         = findViewById(R.id.option1)
        option2         = findViewById(R.id.option2)
        option3         = findViewById(R.id.option3)
        scoreText       = findViewById(R.id.scoretext)
        gameOverLayout  = findViewById(R.id.gameoverlayout)
        finalScoreText  = findViewById(R.id.finalscoretext)
        playAgainButton = findViewById(R.id.playagainbutton)

        correctSound = MediaPlayer.create(this, R.raw.correct_sound)
        wrongSound   = MediaPlayer.create(this, R.raw.wrong_sound)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        setupQuestion()

        option1.setOnClickListener { checkAnswer(option1.text.toString()) }
        option2.setOnClickListener { checkAnswer(option2.text.toString()) }
        option3.setOnClickListener { checkAnswer(option3.text.toString()) }
        playAgainButton.setOnClickListener { resetGame() }
    }

    private fun setupQuestion() {
        if (currentIndex >= shuffledCountries.size) {
            showGameOver(); return
        }

        val (countryName, flagRes) = shuffledCountries[currentIndex]
        flagImage.setImageResource(flagRes)
        scoreText.text = "النقاط: $score"

        val options = mutableListOf(countryName).apply {
            addAll(countries.filter { it.first != countryName }.shuffled().take(2).map { it.first })
            shuffle()
        }

        option1.text = options[0]
        option2.text = options[1]
        option3.text = options[2]

        flagImage.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in))
    }

    private fun checkAnswer(selected: String) {
        val correctName = shuffledCountries[currentIndex].first
        val isCorrect = selected == correctName

        if (isCorrect) {
            correctSound.start()
            score++
        } else {
            wrongSound.start()
        }

        showCustomToast(isCorrect, correctName)
        currentIndex++
        setupQuestion()
    }

    private fun showCustomToast(isCorrect: Boolean, rightAnswer: String) {
        val layout = layoutInflater.inflate(R.layout.custom_toast, null)
        val toastImage = layout.findViewById<ImageView>(R.id.toast_image)
        val toastText = layout.findViewById<TextView>(R.id.toast_text)

        if (isCorrect) {
            toastImage.setImageResource(R.drawable.correct_confetti)
            toastText.text = "إجابة صحيحة!"
        } else {
            toastImage.setImageResource(R.drawable.wrong_dislike)
            toastText.text = "إجابة خاطئة!\nالإجابة الصحيحة: $rightAnswer"
        }

        Toast(this).apply {
            duration = Toast.LENGTH_SHORT
            view = layout
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    private fun showGameOver() {
        finalScoreText.text = "نقاطك النهائية: $score / ${countries.size}"
        gameOverLayout.visibility = View.VISIBLE
    }

    private fun resetGame() {
        score = 0
        currentIndex = 0
        shuffledCountries = countries.shuffled()
        gameOverLayout.visibility = View.GONE
        setupQuestion()
    }

    override fun onDestroy() {
        super.onDestroy()
        correctSound.release()
        wrongSound.release()
    }
}