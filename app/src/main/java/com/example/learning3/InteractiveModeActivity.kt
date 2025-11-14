package com.example.learning3

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import kotlin.random.Random

class InteractiveModeActivity : AppCompatActivity() {

    private val TAG = "InteractiveModeActivity"
    private val numberSounds = mapOf(
        1 to R.raw.one,
        2 to R.raw.two,
        3 to R.raw.three,
        4 to R.raw.four,
        5 to R.raw.five,
        6 to R.raw.six,
        7 to R.raw.seven,
        8 to R.raw.eight,
        9 to R.raw.nine,
        10 to R.raw.ten
    )

    private val numberButtons = mapOf(
        R.id.btnNumber1 to 1,
        R.id.btnNumber2 to 2,
        R.id.btnNumber3 to 3,
        R.id.btnNumber4 to 4,
        R.id.btnNumber5 to 5,
        R.id.btnNumber6 to 6,
        R.id.btnNumber7 to 7,
        R.id.btnNumber8 to 8,
        R.id.btnNumber9 to 9,
        R.id.btnNumber10 to 10
    )

    private var currentNumber: Int? = null
    private var isAnswering: Boolean = false
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interactive_mode)
        Log.d(TAG, "onCreate: InteractiveModeActivity started")

        val btnStart = findViewById<Button>(R.id.btnStart)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val lottieAnimation = findViewById<LottieAnimationView>(R.id.lottieAnimation)

        btnStart.setOnClickListener {
            if (!isAnswering) {
                startNewQuestion(tvResult, lottieAnimation)
                applyAnimation(btnStart)
                Log.d(TAG, "btnStart: Started new question")
            }
        }

        // معالجات النقر لأزرار الأرقام
        numberButtons.keys.forEach { buttonId ->
            val button = findViewById<Button>(buttonId)
            button.setOnClickListener {
                val selectedNumber = numberButtons[buttonId]!!
                Log.d(TAG, "Button clicked: Selected number $selectedNumber, Current number $currentNumber")
                checkAnswer(selectedNumber, tvResult, lottieAnimation, button)
                applyAnimation(button)
            }
        }

        // بدء السؤال الأول تلقائيًا
        startNewQuestion(tvResult, lottieAnimation)
    }

    private fun startNewQuestion(tvResult: TextView, lottieAnimation: LottieAnimationView) {
        try {
            if (isAnswering) {
                Log.d(TAG, "startNewQuestion: Already answering, ignoring")
                return
            }
            isAnswering = true
            val randomNumber = Random.nextInt(1, 11)
            currentNumber = randomNumber
            playSound(numberSounds[randomNumber]!!)
            tvResult.text = "استمع واختر الرقم!"
            lottieAnimation.visibility = android.view.View.GONE
            Log.d(TAG, "startNewQuestion: Played sound for number $randomNumber")
        } catch (e: Exception) {
            Log.e(TAG, "startNewQuestion: Error occurred: ${e.message}", e)
            isAnswering = false
        }
    }

    private fun checkAnswer(selectedNumber: Int, tvResult: TextView, lottieAnimation: LottieAnimationView, clickedButton: Button) {
        try {
            if (!isAnswering) {
                Log.d(TAG, "checkAnswer: Not answering, ignoring")
                return
            }
            if (selectedNumber == currentNumber) {
                tvResult.text = "صحيح!"
                playSound(R.raw.correct_sound)
                showAnimation(lottieAnimation, "correct_animation.json", clickedButton, tvResult, isCorrect = true)
                Log.d(TAG, "checkAnswer: Correct answer")
            } else {
                tvResult.text = "حاول مرة أخرى!"
                playSound(R.raw.wrong_sound)
                showAnimation(lottieAnimation, "wrong_animation.json", clickedButton, tvResult, isCorrect = false)
                Log.d(TAG, "checkAnswer: Wrong answer")
            }
        } catch (e: Exception) {
            Log.e(TAG, "checkAnswer: Error occurred: ${e.message}", e)
        }
    }

    private fun showAnimation(lottieAnimation: LottieAnimationView, animationFileName: String, clickedButton: Button, tvResult: TextView, isCorrect: Boolean) {
        try {
            // إخفاء الرسم المتحرك وتنظيف الحالة
            lottieAnimation.visibility = android.view.View.GONE
            lottieAnimation.clearAnimation()

            // رفع الرسم المتحرك إلى الأمام
            lottieAnimation.bringToFront()

            // تحديد موقع الزر
            val location = IntArray(2)
            clickedButton.getLocationOnScreen(location)
            val buttonX = location[0]
            val buttonY = location[1]
            val buttonWidth = clickedButton.width
            val buttonHeight = clickedButton.height

            // حساب الموقع: مركز الزر لـ X، فوق الزر لـ Y
            val centerX = buttonX + buttonWidth / 2
            val centerY = buttonY - lottieAnimation.height - 10 // 10dp مسافة فوق الزر

            // إعداد LottieAnimationView
            lottieAnimation.x = (centerX - lottieAnimation.width / 2).toFloat()
            lottieAnimation.y = centerY.toFloat()
            Log.d(TAG, "showAnimation: Positioning at x=${lottieAnimation.x}, y=${lottieAnimation.y}")

            // إعداد الرسم المتحرك
            lottieAnimation.setAnimation(animationFileName)
            lottieAnimation.visibility = android.view.View.VISIBLE

            // تطبيق تأثير زوم
            val scaleAnimation = ScaleAnimation(
                1f, 1.5f, 1f, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            )
            scaleAnimation.duration = 300
            scaleAnimation.repeatMode = Animation.REVERSE
            scaleAnimation.repeatCount = 1
            scaleAnimation.fillAfter = false

            lottieAnimation.startAnimation(scaleAnimation)
            lottieAnimation.playAnimation()

            // إخفاء الرسم المتحرك بعد انتهاء التشغيل
            lottieAnimation.addAnimatorListener(object : android.animation.Animator.AnimatorListener {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    lottieAnimation.visibility = android.view.View.GONE
                    lottieAnimation.clearAnimation()
                    lottieAnimation.x = (resources.displayMetrics.widthPixels - lottieAnimation.width) / 2f
                    lottieAnimation.y = tvResult.y + tvResult.height + 10f
                    Log.d(TAG, "showAnimation: Animation ended, Lottie hidden")

                    // إذا كانت الإجابة صحيحة، ابدأ سؤالًا جديدًا
                    if (isCorrect) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            isAnswering = false
                            startNewQuestion(tvResult, lottieAnimation)
                        }, 1000)
                    }
                }
                override fun onAnimationStart(animation: android.animation.Animator) {
                    Log.d(TAG, "showAnimation: Animation started")
                }
                override fun onAnimationCancel(animation: android.animation.Animator) {}
                override fun onAnimationRepeat(animation: android.animation.Animator) {}
            })
        } catch (e: Exception) {
            Log.e(TAG, "showAnimation: Error occurred while loading $animationFileName: ${e.message}", e)
            lottieAnimation.visibility = android.view.View.GONE
            isAnswering = false
        }
    }

    private fun playSound(soundResId: Int) {
        try {
            // إيقاف وتحرير MediaPlayer الحالي إن وجد
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null

            // إنشاء MediaPlayer جديد
            mediaPlayer = MediaPlayer.create(this, soundResId)
            mediaPlayer?.start()
            mediaPlayer?.setOnCompletionListener { it.release(); mediaPlayer = null }
            Log.d(TAG, "playSound: Playing sound resource $soundResId")
        } catch (e: Exception) {
            Log.e(TAG, "playSound: Error playing sound: ${e.message}", e)
            mediaPlayer?.release()
            mediaPlayer = null
        }
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
        Log.d(TAG, "applyAnimation: Applied scale animation to button")
    }

    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed: Back button pressed")
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        // تحرير MediaPlayer عند تدمير النشاط
        mediaPlayer?.release()
        mediaPlayer = null
        Log.d(TAG, "onDestroy: MediaPlayer released")
    }
}