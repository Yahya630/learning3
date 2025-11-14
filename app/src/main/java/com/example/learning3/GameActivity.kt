package com.example.learning3

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.learning3.databinding.ActivityGameBinding
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private var mediaPlayer: MediaPlayer? = null
    private var correctSound: MediaPlayer? = null
    private var wrongSound: MediaPlayer? = null
    private var questions: MutableList<Question> = mutableListOf()
    private var currentQuestionIndex = 0
    private var score = 0
    private var gameMode: String? = null
    private val totalQuestions = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("GameActivity", "onCreate started")
        try {
            binding = ActivityGameBinding.inflate(layoutInflater)
            setContentView(binding.root)
            Log.d("GameActivity", "Binding and content view set successfully")
        } catch (e: Exception) {
            Log.e("GameActivity", "Error setting up binding: ${e.message}", e)
            finish()
            return
        }

        gameMode = intent.getStringExtra("GAME_MODE") ?: "MULTIPLICATION"
        Log.d("GameActivity", "Game mode: $gameMode")

        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.background_music)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
            Log.d("GameActivity", "Background music started")
        } catch (e: Exception) {
            Log.e("GameActivity", "Error loading background music: ${e.message}", e)
        }

        try {
            correctSound = MediaPlayer.create(this, R.raw.correct_sound)
            wrongSound = MediaPlayer.create(this, R.raw.wrong_sound)
            Log.d("GameActivity", "Sound effects loaded")
        } catch (e: Exception) {
            Log.e("GameActivity", "Error loading sound effects: ${e.message}", e)
        }

        generateQuestions()
        displayQuestion()

        binding.option1.setOnClickListener {
            try {
                val answer = binding.option1.text.toString().toIntOrNull() ?: return@setOnClickListener
                checkAnswer(answer)
            } catch (e: Exception) {
                Log.e("GameActivity", "Error processing option1 click: ${e.message}", e)
            }
        }
        binding.option2.setOnClickListener {
            try {
                val answer = binding.option2.text.toString().toIntOrNull() ?: return@setOnClickListener
                checkAnswer(answer)
            } catch (e: Exception) {
                Log.e("GameActivity", "Error processing option2 click: ${e.message}", e)
            }
        }
        binding.option3.setOnClickListener {
            try {
                val answer = binding.option3.text.toString().toIntOrNull() ?: return@setOnClickListener
                checkAnswer(answer)
            } catch (e: Exception) {
                Log.e("GameActivity", "Error processing option3 click: ${e.message}", e)
            }
        }

        binding.refreshAnimationView.setOnClickListener {
            try {
                binding.refreshAnimationView.visibility = View.VISIBLE
                binding.refreshAnimationView.setAnimation("refresh_animation.json")
                binding.refreshAnimationView.playAnimation()
                binding.refreshAnimationView.postDelayed({
                    restartGame()
                }, 1000)
                Log.d("GameActivity", "Refresh animation triggered")
            } catch (e: Exception) {
                Log.e("GameActivity", "Error playing refresh animation: ${e.message}", e)
                restartGame()
            }
        }
    }

    private fun generateQuestions() {
        questions.clear()
        repeat(totalQuestions) {
            val num1 = Random.nextInt(1, 10)
            val num2 = Random.nextInt(1, 10)
            when (gameMode) {
                "MULTIPLICATION" -> {
                    val questionText = "$num1 × $num2 = ?"
                    val correctAnswer = num1 * num2
                    questions.add(Question(questionText = questionText, correctAnswer = correctAnswer))
                }
                "ADDITION_SUBTRACTION" -> {
                    if (Random.nextBoolean()) {
                        val questionText = "$num1 + $num2 = ?"
                        val correctAnswer = num1 + num2
                        questions.add(Question(questionText = questionText, correctAnswer = correctAnswer))
                    } else {
                        val num2Adjusted = Random.nextInt(1, num1 + 1)
                        val questionText = "$num1 - $num2Adjusted = ?"
                        val correctAnswer = num1 - num2Adjusted
                        questions.add(Question(questionText = questionText, correctAnswer = correctAnswer))
                    }
                }
            }
        }
        Log.d("GameActivity", "Generated ${questions.size} questions")
    }

    private fun displayQuestion() {
        if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]
            binding.tvQuestion.text = question.questionText
            binding.tvQuestionCount.text = "السؤال ${currentQuestionIndex + 1} من $totalQuestions"

            val options = mutableListOf<Int>()
            question.correctAnswer?.let { options.add(it) }
            while (options.size < 3) {
                val wrongAnswer = (question.correctAnswer ?: 0) + Random.nextInt(-10, 11)
                if (wrongAnswer != question.correctAnswer && wrongAnswer > 0 && !options.contains(wrongAnswer)) {
                    options.add(wrongAnswer)
                }
            }
            options.shuffle()

            binding.option1.text = options[0].toString()
            binding.option2.text = options[1].toString()
            binding.option3.text = options[2].toString()

            Log.d("GameActivity", "Displaying question: ${question.questionText}, Correct: ${question.correctAnswer}, Options: $options")

            binding.optionsLayout.visibility = View.VISIBLE
            binding.refreshAnimationView.visibility = View.GONE
        } else {
            showResult()
        }
    }

    private fun checkAnswer(selectedAnswer: Int) {
        try {
            val currentQuestion = questions[currentQuestionIndex]
            var animationPlayed = false

            if (selectedAnswer == currentQuestion.correctAnswer) {
                try {
                    binding.animationView.setAnimation("correct_animation.json")
                    binding.animationView.playAnimation()
                    correctSound?.start()
                    animationPlayed = true
                    Log.d("GameActivity", "Correct answer selected")
                } catch (e: Exception) {
                    Log.e("GameActivity", "Error playing correct animation/sound: ${e.message}", e)
                    binding.tvQuestion.text = "إجابة صحيحة!"
                }
                score++
            } else {
                try {
                    binding.animationView.setAnimation("wrong_animation.json")
                    binding.animationView.playAnimation()
                    wrongSound?.start()
                    animationPlayed = true
                    Log.d("GameActivity", "Wrong answer selected")
                } catch (e: Exception) {
                    Log.e("GameActivity", "Error playing wrong animation/sound: ${e.message}", e)
                    binding.tvQuestion.text = "إجابة خاطئة!"
                }
            }

            binding.root.postDelayed({
                try {
                    if (!animationPlayed) {
                        binding.tvQuestion.text = questions[currentQuestionIndex].questionText
                    }
                    currentQuestionIndex++
                    displayQuestion()
                } catch (e: Exception) {
                    Log.e("GameActivity", "Error moving to next question: ${e.message}", e)
                }
            }, if (animationPlayed) 1000 else 500)
        } catch (e: Exception) {
            Log.e("GameActivity", "Error in checkAnswer: ${e.message}", e)
        }
    }

    private fun showResult() {
        binding.tvQuestion.text = "انتهت اللعبة!"
        binding.tvQuestionCount.text = "نتيجتك: $score من $totalQuestions"
        binding.optionsLayout.visibility = View.GONE
        binding.refreshAnimationView.visibility = View.VISIBLE
        try {
            binding.animationView.setAnimation("end_game_animation.json")
            binding.animationView.playAnimation()
            binding.refreshAnimationView.setAnimation("refresh_animation.json")
            binding.refreshAnimationView.loop(true)
            binding.refreshAnimationView.playAnimation()
            Log.d("GameActivity", "Showing game result")
        } catch (e: Exception) {
            Log.e("GameActivity", "Error playing end game or refresh animation: ${e.message}", e)
            binding.tvQuestion.text = "انتهت اللعبة! انقر لإعادة المحاولة"
        }
    }

    private fun restartGame() {
        currentQuestionIndex = 0
        score = 0
        questions.clear()
        generateQuestions()
        displayQuestion()
        binding.refreshAnimationView.visibility = View.GONE
        Log.d("GameActivity", "Game restarted")
    }

    override fun onPause() {
        super.onPause()
        try {
            mediaPlayer?.pause()
            Log.d("GameActivity", "Paused media player")
        } catch (e: Exception) {
            Log.e("GameActivity", "Error pausing mediaPlayer: ${e.message}", e)
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            mediaPlayer?.start()
            Log.d("GameActivity", "Resumed media player")
        } catch (e: Exception) {
            Log.e("GameActivity", "Error resuming mediaPlayer: ${e.message}", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            mediaPlayer?.release()
            correctSound?.release()
            wrongSound?.release()
            mediaPlayer = null
            correctSound = null
            wrongSound = null
            Log.d("GameActivity", "Media players released")
        } catch (e: Exception) {
            Log.e("GameActivity", "Error releasing media players: ${e.message}", e)
        }
    }
}