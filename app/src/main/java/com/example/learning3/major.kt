package com.example.learning3

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.random.Random

class major : AppCompatActivity() {

    private lateinit var q1Text: TextView
    private lateinit var q1Answer: EditText
    private lateinit var q2Text: TextView
    private lateinit var colorButtonsLayout: LinearLayout
    private lateinit var q3Text: TextView
    private lateinit var arA: Button
    private lateinit var arB: Button
    private lateinit var q4Text: TextView
    private lateinit var enA: Button
    private lateinit var enB: Button
    private lateinit var submitBtn: Button

    private var correctColor = ""
    private var correctArabic = ""
    private var correctEnglish = ""
    private var correctMathAnswer = 0
    private var selectedColor = ""

    private var selectedColorButton: Button? = null
    private var selectedMathButton: Button? = null
    private var selectedArabic = ""
    private var selectedEnglish = ""

    private lateinit var backgroundMusic: MediaPlayer
    private lateinit var correctSound: MediaPlayer
    private lateinit var wrongSound: MediaPlayer

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_major)

        /* ------------------ الأصوات ------------------ */
        backgroundMusic = MediaPlayer.create(this, R.raw.background_music).apply {
            isLooping = true
            start()
        }
        correctSound = MediaPlayer.create(this, R.raw.correct_sound)
        wrongSound   = MediaPlayer.create(this, R.raw.wrong_sound)

        /* ------------------ ربط الـ Views ------------------ */
        q1Text            = findViewById(R.id.q1Text)
        q2Text            = findViewById(R.id.q2Text)
        colorButtonsLayout = findViewById(R.id.colorButtonsLayout)
        q3Text            = findViewById(R.id.q3Text)
        arA               = findViewById(R.id.arA)
        arB               = findViewById(R.id.arB)
        q4Text            = findViewById(R.id.q4Text)
        enA               = findViewById(R.id.enA)
        enB               = findViewById(R.id.enB)
        submitBtn         = findViewById(R.id.submitBtn)

        val option1: Button = findViewById(R.id.q1Option1)
        val option2: Button = findViewById(R.id.q1Option2)
        val option3: Button = findViewById(R.id.q1Option3)
        val mathButtons     = listOf(option1, option2, option3)

        generateQuestions()

        /* ------------------ اختيارات الحروف ------------------ */
        arA.setOnClickListener {
            arA.setBackgroundColor(Color.GREEN)
            arB.setBackgroundColor(Color.parseColor("#FFECB3"))
            selectedArabic = arA.text.toString()
        }
        arB.setOnClickListener {
            arB.setBackgroundColor(Color.GREEN)
            arA.setBackgroundColor(Color.parseColor("#FFECB3"))
            selectedArabic = arB.text.toString()
        }

        enA.setOnClickListener {
            enA.setBackgroundColor(Color.GREEN)
            enB.setBackgroundColor(Color.parseColor("#FFECB3"))
            selectedEnglish = enA.text.toString()
        }
        enB.setOnClickListener {
            enB.setBackgroundColor(Color.GREEN)
            enA.setBackgroundColor(Color.parseColor("#FFECB3"))
            selectedEnglish = enB.text.toString()
        }

        /* ------------------ زر التحقق ------------------ */
        submitBtn.setOnClickListener {
            val userMath = selectedMathButton?.text?.toString()?.toIntOrNull()
            var hasError = false

            // التحقق من كل الإجابات
            if (userMath != correctMathAnswer) {
                hasError = true
                mathButtons.forEach {
                    it.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.holo_red_light))
                }
            }
            if (selectedColor != correctColor) {
                hasError = true
                for (i in 0 until colorButtonsLayout.childCount) {
                    (colorButtonsLayout.getChildAt(i) as? Button)?.background =
                        createCircularDrawable("#FF0000")
                }
            }
            if (selectedArabic != correctArabic) {
                hasError = true
                arA.setBackgroundColor(Color.RED)
                arB.setBackgroundColor(Color.RED)
            }
            if (selectedEnglish != correctEnglish) {
                hasError = true
                enA.setBackgroundColor(Color.RED)
                enB.setBackgroundColor(Color.RED)
            }

            /* ---------- الإجابات صحيحة ---------- */
            if (!hasError) {
                if (wrongSound.isPlaying) { wrongSound.stop(); wrongSound.prepare() }

                /* 🔇 إيقاف موسيقى الخلفية قبل الانتقال */
                if (backgroundMusic.isPlaying) {
                    backgroundMusic.stop()
                    backgroundMusic.release()
                }

                correctSound.start()

                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))
                    finish() // غلق هذه الصفحة
                }, 2000)

                /* ---------- هناك خطأ ---------- */
            } else {
                if (wrongSound.isPlaying) { wrongSound.stop(); wrongSound.prepare() }
                wrongSound.start()
                Toast.makeText(this, "\u274C هناك خطأ في الإجابات، حاول مرة أخرى!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /* ------------------ بقية الدوال كما هي ------------------ */

    private fun generateQuestions() {
        val option1: Button = findViewById(R.id.q1Option1)
        val option2: Button = findViewById(R.id.q1Option2)
        val option3: Button = findViewById(R.id.q1Option3)
        val mathButtons = listOf(option1, option2, option3)

        val isAddition = Random.nextBoolean()
        val a: Int
        val b: Int
        if (isAddition) {
            a = Random.nextInt(1, 11)
            b = Random.nextInt(1, 11)
            correctMathAnswer = a + b
        } else {
            b = Random.nextInt(1, 11)
            a = Random.nextInt(b, b + 11)
            correctMathAnswer = a - b
        }

        val operator = if (isAddition) "+" else "-"
        q1Text.text = "\uD83E\uDDEE كم ناتج $a $operator $b ؟"

        val options = mutableSetOf(correctMathAnswer)
        while (options.size < 3) {
            val wrongOption = correctMathAnswer + Random.nextInt(-3, 4)
            if (wrongOption != correctMathAnswer && wrongOption >= 0) options.add(wrongOption)
        }
        val shuffledOptions = options.shuffled()
        mathButtons.forEachIndexed { i, btn -> btn.text = shuffledOptions[i].toString() }

        mathButtons.forEach { btn ->
            btn.setOnClickListener {
                mathButtons.forEach { it.setBackgroundColor(Color.parseColor("#FFECB3")) }
                selectedMathButton = btn
                btn.setBackgroundColor(Color.GREEN)
            }
        }

        val colors = listOf("أحمر","أزرق","أصفر","أخضر","برتقالي","وردي","أسود","أبيض","رمادي","بني")
        val colorHexMap = mapOf(
            "أحمر" to "#F44336","أزرق" to "#2196F3","أصفر" to "#FFEB3B","أخضر" to "#4CAF50",
            "برتقالي" to "#FF9800","وردي" to "#E91E63","أسود" to "#000000","أبيض" to "#FFFFFF",
            "رمادي" to "#9E9E9E","بني" to "#795548"
        )

        val targetColor = colors.random()
        correctColor = colorHexMap[targetColor] ?: "#F44336"
        q2Text.text = "\uD83C\uDFA8 اختر اللون: $targetColor"

        colorButtonsLayout.removeAllViews()
        val colorOptions = colors.shuffled().take(5).toMutableList()
        if (targetColor !in colorOptions) colorOptions[Random.nextInt(5)] = targetColor

        for (colorName in colorOptions) {
            val btn = Button(this).apply {
                layoutParams = LinearLayout.LayoutParams(0, 140, 1f).apply { setMargins(8,8,8,8) }
                background = createCircularDrawable(colorHexMap[colorName] ?: "#000000")
                text = ""
                tag = colorHexMap[colorName]
                setOnClickListener {
                    selectedColor = colorHexMap[colorName] ?: ""
                    selectedColorButton?.let {
                        val prevCol = it.tag?.toString() ?: "#000000"
                        it.background = createCircularDrawable(prevCol)
                        it.animate().scaleX(1f).scaleY(1f).duration = 150
                    }
                    background = createCircularDrawable(colorHexMap[colorName] ?: "#000000")
                    animate().scaleX(1.2f).scaleY(1.2f).duration = 150
                    selectedColorButton = this
                }
            }
            colorButtonsLayout.addView(btn)
        }

        val arabicLetters  = listOf("ا","ب","ت","ث","ج","ح","خ","د","ذ","ر","ز","س","ش","ص","ض","ط","ظ","ع","غ","ف","ق","ك","ل","م","ن","ه","و","ي")
        correctArabic = arabicLetters.random()
        val wrongArabic = arabicLetters.filter { it != correctArabic }.random()
        q3Text.text = "\uD83D\uDD20 اختر الحرف: $correctArabic"
        if (Random.nextBoolean()) { arA.text = correctArabic; arB.text = wrongArabic } else { arA.text = wrongArabic; arB.text = correctArabic }

        val englishLetters = ('A'..'Z').map { it.toString() }
        correctEnglish = englishLetters.random()
        val wrongEnglish = englishLetters.filter { it != correctEnglish }.random()
        q4Text.text = "\uD83D\uDD21 اختر الحرف: $correctEnglish"
        if (Random.nextBoolean()) { enA.text = correctEnglish; enB.text = wrongEnglish } else { enA.text = wrongEnglish; enB.text = correctEnglish }
    }

    private fun createCircularDrawable(colorHex: String): GradientDrawable =
        GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(Color.parseColor(colorHex))
            setStroke(6, Color.BLACK)
        }

    /* ---------- تأكد من تحرير الـ MediaPlayer عند الخروج ---------- */
    override fun onDestroy() {
        super.onDestroy()
        if (::backgroundMusic.isInitialized) backgroundMusic.release()
        if (::correctSound.isInitialized)    correctSound.release()
        if (::wrongSound.isInitialized)      wrongSound.release()
    }
}
