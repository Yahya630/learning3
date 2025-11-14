package com.example.learning3

import android.content.ClipData
import android.content.ClipDescription
import android.media.MediaPlayer
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.DragEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children

class FfffActivity : AppCompatActivity() {
    private lateinit var fruitContainer: LinearLayout
    private lateinit var numberContainer: LinearLayout
    private lateinit var restartButton: Button
    private lateinit var resultTextView: TextView

    private val fruits = listOf("banana", "apple", "orange", "pineapple", "watermelon")
    private val fruitDrawables = mapOf(
        "banana" to R.drawable.ic_banana,
        "apple" to R.drawable.ic_apple,
        "orange" to R.drawable.ic_orange,
        "pineapple" to R.drawable.ic_pineapple,
        "watermelon" to R.drawable.ic_watermelon
    )
    private val placedFruits = mutableMapOf<Int, String>()
    private val fruitCounts = mutableMapOf<String, Int>()

    // متغيرات لتشغيل الأصوات
    private var successSound: MediaPlayer? = null
    private var errorSound: MediaPlayer? = null
    private var winSound: MediaPlayer? = null
    private var backgroundMusic: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ffff)

        // تهيئة العناصر
        fruitContainer = findViewById(R.id.fruitContainer)
        numberContainer = findViewById(R.id.numberContainer)
        restartButton = findViewById(R.id.restartButton)
        resultTextView = findViewById(R.id.resultTextView)

        // تهيئة الأصوات
        successSound = MediaPlayer.create(this, R.raw.success)
        errorSound = MediaPlayer.create(this, R.raw.error)
        winSound = MediaPlayer.create(this, R.raw.correct_sound)
        backgroundMusic = MediaPlayer.create(this, R.raw.background_music)?.apply {
            isLooping = true // تكرار الموسيقى
        }

        // بدء الموسيقى الخلفية
        backgroundMusic?.start()

        setupGame()

        restartButton.setOnClickListener {
            setupGame()
            resultTextView.text = ""
        }
    }

    override fun onResume() {
        super.onResume()
        // استئناف الموسيقى الخلفية إذا كانت متوقفة
        backgroundMusic?.start()
    }

    override fun onPause() {
        super.onPause()
        // إيقاف الموسيقى الخلفية مؤقتًا
        backgroundMusic?.pause()
    }

    private fun setupGame() {
        fruitContainer.removeAllViews()
        numberContainer.removeAllViews()
        placedFruits.clear()
        fruitCounts.clear()

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        val fruitBoxWidth = (screenWidth / 2) - 40
        val fruitBoxHeight = (screenHeight / 4) - 40
        val numberBoxSize = (screenWidth / 2) - 40
        val imagesPerRow = 3 // عدد الصور في كل صف
        val fruitImageSize = (fruitBoxWidth / imagesPerRow) - 4 // حجم الصورة بناءً على عدد الصور في الصف

        // توليد خمس أرقام مميزة من 1 إلى 9
        val uniqueCounts = (1..9).shuffled().take(5)
        fruits.forEachIndexed { index, fruit ->
            fruitCounts[fruit] = uniqueCounts[index]
        }

        fruitCounts.forEach { (fruit, count) ->
            val fruitLayout = LinearLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(fruitBoxWidth, fruitBoxHeight).apply {
                    setMargins(4, 4, 4, 4)
                }
                orientation = LinearLayout.VERTICAL
                setBackgroundResource(android.R.drawable.btn_default)
            }

            // تقسيم الصور إلى صفوف
            var currentRow: LinearLayout? = null
            repeat(count) { index ->
                if (index % imagesPerRow == 0) {
                    currentRow = LinearLayout(this).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        orientation = LinearLayout.HORIZONTAL
                    }
                    fruitLayout.addView(currentRow)
                }

                val fruitImage = ImageView(this).apply {
                    layoutParams = LinearLayout.LayoutParams(fruitImageSize, fruitImageSize).apply {
                        setMargins(2, 2, 2, 2)
                    }
                    setImageResource(fruitDrawables[fruit]!!)
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    setBackgroundResource(0)
                }
                currentRow?.addView(fruitImage)
            }

            fruitLayout.setOnLongClickListener { view ->
                val clipText = fruit
                val item = ClipData.Item(clipText)
                val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
                val data = ClipData(clipText, mimeTypes, item)
                val dragShadow = View.DragShadowBuilder(view)
                view.startDragAndDrop(data, dragShadow, view, 0)
                view.visibility = View.INVISIBLE
                true
            }
            fruitContainer.addView(fruitLayout)
        }

        val numbers = fruitCounts.values.shuffled()
        numbers.forEachIndexed { index, number ->
            val numberLayout = LinearLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(numberBoxSize, numberBoxSize).apply {
                    setMargins(4, 4, 4, 4)
                }
                setBackgroundResource(android.R.drawable.btn_default)
                setOnDragListener { _, event ->
                    when (event.action) {
                        DragEvent.ACTION_DROP -> {
                            val fruit = event.clipData.getItemAt(0).text.toString()
                            val correctCount = fruitCounts[fruit]!!
                            if (number == correctCount && !placedFruits.containsValue(fruit)) {
                                placedFruits[number] = fruit
                                (event.localState as LinearLayout).visibility = View.GONE
                                resultTextView.text = "تم! اسحب المزيد."
                                successSound?.start()
                                checkWinCondition()
                            } else {
                                resultTextView.text = "غير صحيح! العدد لا يتطابق أو تم استخدام هذا النوع."
                                (event.localState as LinearLayout).visibility = View.VISIBLE
                                errorSound?.start()
                            }
                            true
                        }
                        DragEvent.ACTION_DRAG_ENDED -> {
                            if (!event.result) {
                                (event.localState as LinearLayout).visibility = View.VISIBLE
                            }
                            true
                        }
                        else -> true
                    }
                }
            }

            val numberView = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                text = number.toString()
                textSize = 48f
                gravity = android.view.Gravity.CENTER
                setTextColor(android.graphics.Color.BLACK)
            }
            numberLayout.addView(numberView)
            numberContainer.addView(numberLayout)
        }
    }

    private fun checkWinCondition() {
        val allFruitsPlaced = fruitContainer.childCount == 0 || fruitContainer.children.all { it.visibility == View.GONE }
        if (allFruitsPlaced) {
            resultTextView.text = "مبروك! لقد فزت! اضغط على إعادة اللعبة لبدء جولة جديدة."
            winSound?.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // تحرير موارد الأصوات
        successSound?.release()
        errorSound?.release()
        winSound?.release()
        backgroundMusic?.release()
        successSound = null
        errorSound = null
        winSound = null
        backgroundMusic = null
    }
}