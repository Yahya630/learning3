package com.example.learning3

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class  languagarabe: AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_languagarabe)

        // ضبط الحواف لتتناسب مع نافذة النظام
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // الصورة الأولى: تفتح صفحة اللغة العربية
        val learnCard = findViewById<CardView>(R.id.learnCard)
        learnCard.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        // الصورة الثانية: تفتح صفحة اللغة الإنجليزية
        val testCard = findViewById<CardView>(R.id.testCard)
        testCard.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }
        val nextActivity = findViewById<CardView>(R.id.colorsCard)
        nextActivity.setOnClickListener {
            val intent = Intent(this, NewDrawing::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // تحرير موارد الصوت
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}
